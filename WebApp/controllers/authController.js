const { promisify } = require("util");
const Customer = require("../models/customerModel");
const jwt = require("jsonwebtoken");
const db = require("../models/supportFunctions/dbOperations");
const bcrypt = require("bcryptjs");

const catchAsync = require("../utils/catchAsync");
const AppError = require("../utils/appError");
//const validator = require("../models/supportFunctions/validators");

const signToken = (userID) => {
  return jwt.sign({ userID }, process.env.JWT_SECRET, {
    expiresIn: process.env.JWT_EXPIRES_IN,
  });
};

const signTokenLogout = (userID) => {
  return jwt.sign({ userID }, process.env.JWT_SECRET, {
    expiresIn: process.env.JWT_COOKIE_EXPIRES_LOGOUT,
  });
};

const createSendToken = (user, statusCode, req, res) => {
  var token;
  if (user.customerID) {
    token = signToken(user.customerID);
  } else {
    token = signToken(user.username);
  }

  res.cookie("jwt", token, {
    expires: new Date(
      Date.now() + process.env.JWT_COOKIE_EXPIRES_IN * 24 * 60 * 60 * 1000
    ),
    httpOnly: true,
    secure: req.secure || req.headers["x-forwarded-proto"] === "https",
  });

  // Remove password from output
  user.password = undefined;

  res.status(statusCode).json({
    status: "success",
    token,
    data: {
      user,
    },
  });
};

const createLogoutToken = (user, statusCode, req, res) => {
  var token;
  if (user.customerID) {
    token = signTokenLogout(user.customerID);
  } else {
    token = signTokenLogout(user.username);
  }

  res.cookie("jwt", token, {
    expires: new Date(Date.now() + process.env.JWT_COOKIE_EXPIRES_LOGOUT),
    httpOnly: true,
    secure: req.secure || req.headers["x-forwarded-proto"] === "https",
  });

  // Remove password from output
  user.password = undefined;

  res.status(statusCode).json({
    status: "success",
    token,
    data: {
      user,
    },
  });
};

//signup function
exports.signUp = async (req, res, next) => {
  const newCustomer = new Customer(req.body.data);

  // Using jwt token
  const token = signToken(newCustomer.customerID);

  var sqlStatement = newCustomer.statement;
  //console.log(sqlStatement);
  var result = await db.query(sqlStatement);

  // Get the created user from the database
  sqlStatement = `SELECT * FROM customer WHERE CustomerID = ${newCustomer.customerID}`;
  result = await db.query(sqlStatement);

  // console.log(result);
  res.status(201).json({
    status: "success",
    token: token,
    data: {
      user: result,
    },
  });
};

exports.login = async (req, res, next) => {
  // Uses object destructuring to get the required fields from the passed object
  var { customerID, password } = req.body;

  // 1) check if the customerID and password is valid
  if (!customerID || !password) {
    res.status(400).json({
      status: "fill the details",
      data: {},
    });
    return;
  }
  // 2) check if user exists and if the password matches
  var sqlStatement = `SELECT DISTINCT customerID, password FROM customer WHERE customerID = '${customerID}'`;
  var result = await db.query(sqlStatement);

  sqlStatement = `SELECT DISTINCT username, password, firstName FROM admins WHERE username = '${customerID}'`;
  var admins = await db.query(sqlStatement);

  result.push(admins[0]);
  var placeholder = await db.query(sqlStatement);
  // Check for errors
  if (!result) {
    res.status(400).json({
      status: "No such user",
    });
    return;
  }

  // If a result is found,
  const isValid = bcrypt.compare(password, result[0].password, (err, valid) => {
    if (err) {
      res.status(400).json({
        status: "Error",
      });
      return;
    }
    if (!valid) {
      res.status(400).json({
        status: "Wrong Password",
      });
      return;
    }
    {
      // 3) pass the JWT to the client
      createSendToken(result[0], 200, req, res);
    }
  });
};

exports.logout = (req, res) => {
  console.log("auth logout");
  res.cookie("jwt", "loggedout", {
    expires: new Date(Date.now() + 100),
    httpOnly: true,
    secure: req.secure || req.headers["x-forwarded-proto"] === "https",
  });
  res.status(200).json({
    status: "Success",
  });
};

exports.protect = catchAsync(async (req, res, next) => {
  //1) Get token if exists
  // Token is normally in the header
  var token;
  if (
    req.headers.authorization &&
    req.headers.authorization.startsWith("Bearer")
  ) {
    token = req.headers.authorization.split(" ")[1];
  } else if (req.cookies.jwt) {
    token = req.cookies.jwt;
  }

  if (!token) {
    return next(new AppError("No token given in the header", 401));
  }
  //2) Validate token (Verification step)
  const decoded = await promisify(jwt.verify)(token, process.env.JWT_SECRET);

  //3) Check if user exists
  const customerID = decoded.userID;
  var findUserSQLstatement = `SELECT DISTINCT customerID, firstName FROM customer WHERE customerID = ${customerID}`;
  const curUser = await db.query(findUserSQLstatement);

  // If there is no user
  if (!curUser[0]) {
    return next(new AppError("No user with this ID found", 401));
  }
  //4) Check if User changed pass after JWT issued
  //5) Give access to the route
  req.user = curUser[0];
  res.locals.user = curUser[0];
  next();
});

exports.adminProtect = catchAsync(async (req, res, next) => {
  //1) Get token if exists
  // Token is normally in the header
  try {
    var token;
    if (
      req.headers.authorization &&
      req.headers.authorization.startsWith("Bearer")
    ) {
      token = req.headers.authorization.split(" ")[1];
    } else if (req.cookies.jwt) {
      token = req.cookies.jwt;
    }

    if (!token) {
      return next(new AppError("No token given in the header", 401));
    }
    //2) Validate token (Verification step)
    const decoded = await promisify(jwt.verify)(token, process.env.JWT_SECRET);

    //3) Check if user exists
    const userID = decoded.userID;
    const findUserSQLstatement = `SELECT DISTINCT username, firstName FROM admins WHERE username = '${userID}'`;
    const curUser = await db.query(findUserSQLstatement);

    // If there is no user
    if (!curUser[0]) {
      return next(new AppError("No user with this ID found", 401));
    }
    //4) Check if User changed pass after JWT issued
    //5) Give access to the route
    curUser[0].isAdmin = true;
    req.user = curUser[0];
    res.locals.user = curUser[0];
    next();
  } catch (err) {
    return new AppError("Error in admin protect", 400);
  }
});

exports.isLoggedIn = async (req, res, next) => {
  if (req.cookies.jwt) {
    try {
      // 1) verify token
      const decoded = await promisify(jwt.verify)(
        req.cookies.jwt,
        process.env.JWT_SECRET
      );

      // 2) Check if user still exists
      var sqlStatement = `SELECT DISTINCT customerID, password, firstName FROM customer WHERE customerID = '${decoded.userID}'`;
      var currentUser = await db.query(sqlStatement);
      if (!currentUser[0]) {
        sqlStatement = `SELECT DISTINCT username, password, firstName FROM admins WHERE username = '${decoded.userID}'`;
        var admins = await db.query(sqlStatement);
        if (!admins[0]) {
          return next();
        } else {
          currentUser = admins;
          currentUser[0].isAdmin = true;
        }
      }
      currentUser.password = undefined;

      // THERE IS A LOGGED IN USER
      res.locals.user = currentUser[0];
      return next();
    } catch (err) {
      return next();
    }
  }
  next();
};