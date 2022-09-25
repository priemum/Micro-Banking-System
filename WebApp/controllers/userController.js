const db = require("../models/supportFunctions/dbOperations");
const Customer = require("../models/customerModel.js");

const catchAsync = require("../utils/catchAsync");
const AppError = require("../utils/appError");

const tableCols =
  "(`customerID`, `password`, `customerNIC`, `firstName`, `lastName`, `contactNumber`, `address`, `birthday`, `agentID`)";
const tableName = "customer";

exports.getAllUsers = async (req, res) => {
  try {
    const sqlStatement = `SELECT * FROM ${tableName}`;
    const result = await db.query(sqlStatement);

    result.forEach(function (el) {
      el.password = "NULL";
    });

    const rand = await db.query(sqlStatement);

    res.status(200).json({
      status: "Success",
      data: {
        customers: result,
      },
    });
  } catch (err) {
    res.status(400).json({
      status: "Failed to get",
      data: {
        err,
      },
    });
  }
};

/**
 *
 * @param {*} req Set agentID as the query parameter in the method
 * @param {*} res res.body contains three data arrays namely accounts, users and accountholders
 */
exports.getAllLoginInfoByAgentID = async (req, res) => {
  try {
    const agentID = req.query.id;
    var sqlStatement = `SELECT DISTINCT customerID FROM accounts NATURAL JOIN accountholders NATURAL JOIN customer WHERE agentID = ${agentID} ORDER BY customerID`;
    const resultAccounts = await db.query(sqlStatement);

    sqlStatementJoint = `SELECT DISTINCT customerID FROM accounts NATURAL JOIN accountholders NATURAL JOIN customer WHERE accountType = 'Joint' ORDER BY customerID`;
    const jointAccounts = await db.query(sqlStatementJoint);

    // AgentID and Customer ID is ommited in the following sql queries
    // Used to get distinct elements without duplicates for joint accounts
    sqlStatement = `SELECT DISTINCT accountNumber, accountType, accountBalance FROM accounts NATURAL JOIN accountholders NATURAL JOIN customer WHERE agentID = ${agentID} AND accountType != 'Joint' ORDER BY customerID`;
    var accounts = await db.query(sqlStatement);

    sqlStatementJoint = `SELECT DISTINCT accountNumber, accountType, accountBalance FROM accounts NATURAL JOIN accountholders NATURAL JOIN customer WHERE accountType = 'Joint' ORDER BY customerID`;
    const jointAccounts1 = await db.query(sqlStatementJoint);

    const holderStatement = `SELECT DISTINCT accountNumber, customerID FROM accounts NATURAL JOIN accountholders NATURAL JOIN customer WHERE agentID = ${agentID} ORDER BY customerID`;
    const accountholders = await db.query(holderStatement);

    const userSqlStatement = `SELECT DISTINCT customerID, password, firstName, lastName, agentID FROM customer NATURAL JOIN accountholders NATURAL JOIN accounts WHERE agentID = ${agentID} ORDER BY customerID`;
    const users = await db.query(userSqlStatement);

    jointAccounts1.forEach((el) => {
      accounts.push(el);
    });

    // Just to give some time
    const result1 = await db.query(sqlStatement);

    res.status(200).json({
      status: "Success",
      data: {
        accountholders: accountholders,
        accounts: accounts,
        users: users,
      },
    });
  } catch (err) {
    res.status(400).json({
      status: "Failed to GET",
      data: {
        err,
      },
    });
  }
};

/** Get customer NIC as param in req */
exports.getUser = async (req, res) => {
  try {
    const customerID = req.query.id;
    const sqlStatement = `SELECT * FROM ${tableName} WHERE customerID = ${customerID}`;
    const result = await db.query(sqlStatement);

    res.status(200).json({
      status: "Success",
      data: {
        customers: result[0],
      },
    });
  } catch (err) {
    res.status(400).json({
      status: "Failed to get",
      data: {
        err,
      },
    });
  }
};

exports.updateUser = async (req, res) => {
  res.status(400).json({
    status: "Failed",
    data: {
      message: "Ee miss ada wada na",
    },
  });
};

exports.deleteUser = async (req, res) => {
  try {
    const customerID = req.params.id;
    const sqlStatement = `DELETE FROM ${tableName} WHERE customerID = ${customerID}`;
    const result = await db.query(sqlStatement);

    res.status(200).json({
      status: "Deleted Successfully",
      data: {
        customers: result,
      },
    });
  } catch (err) {
    res.status(400).json({
      status: "Failed to Delete",
      data: {
        err,
      },
    });
  }
};

/**
 * 
 * @param {*} req Pass customerID in the req.query.customerID field. 
 * eg: url/?customerID=1234567
 * @param {*} res res.body contains customer and account elements
 */
exports.getUserAndAccByID = async (req, res) => {
  try {
    const customerID = req.query.customerID;
    var sqlStatement = `SELECT * FROM ${tableName} WHERE customerID = ${customerID}`;
    const resultUser = await db.query(sqlStatement);

    sqlStatement = `SELECT * FROM accounts NATURAL JOIN accountholders WHERE customerID = ${customerID}`;
    const resultAccounts = await db.query(sqlStatement);

    if (!resultUser[0]) {
      return new AppError("No such user found");
    }

    res.status(200).json({
      status: "Success",
      data: {
        customer: resultUser[0],
        account: resultAccounts,
      },
    });
  } catch (err) {
    res.status(400).json({
      status: "Failed to get",
      data: {
        err,
      },
    });
  }
};