const bcrypt = require("bcryptjs");

/* Required input fields are
 * accountNumber
 * date
 * transactionType
 * transactionAmount
 * agentID
 *
 * transactionID is AI
 * transaction charge default is 30.0
 * */
exports.validateTransaction = async function (req, res, next) {
  // Use a validator at the front end too
  console.log("Inside validator");
  if (
    req.body.data.accountNumber === NULL ||
    req.body.data.date === NULL ||
    req.body.data.transactionType === NULL ||
    req.body.data.transactionAMount === NULL ||
    req.body.data.agentID === NULL
  ) {
    console.log("Inside if");
    res.status(400).json({
      status: "Invalid Data",
      data: {
        err,
      },
    });
    next();
  } else {
    next();
  }
};

// Encrypt password middleware
exports.encryptPass = async function (req, res, next) {
  // Additional functionality
  // TODO  No need to run this if the password field is not modified
  // Check if pass = confirm pass
  //console.log("encrpting");
  req.body.data.password = await bcrypt.hash(req.body.data.password, 12);
  //console.log("hashed");
  // remove confirm pass from here
  next();
};

exports.validateCustomer = async function (req, res, next) {
  // Use a validator at the front end too
  // set customer ID to AI
  console.log("Inside validator");
  if (
    req.body.data.customerID === "NULL" ||
    req.body.data.password === "NULL" ||
    req.body.data.confirmPass === "NULL" ||
    req.body.data.customerNIC === "NULL" ||
    req.body.data.name === "NULL" ||
    req.body.data.contactNumber === "NULL" ||
    req.body.data.address === "NULL" ||
    req.body.data.birthday === "NULL"
  ) {
    //console.log("Inside if");
    res.status(400).json({
      status: "Invalid Data",
      data: {
        err,
      },
    });
    //next();
  } else if (req.body.data.password === req.body.data.confirmPass) {
    // console.log("passwords match");
    next();
  } else {
    // console.log("pass no match");
    res.status(400).json({
      status: "confirm password not matching",
      data: {},
    });
  }
};
