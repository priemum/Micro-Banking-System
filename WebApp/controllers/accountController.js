const db = require("../models/supportFunctions/dbOperations");
const Account = require("../models/accountModel");
const tableName = "accounts";

exports.createAccount = async (req, res) => {
  try {
    // Check if the user exists

    const accountCols = `accountNumber, accountType, accountBalance`;
    const accountHoldersCols = `accountNumber, customerID`;

    // Inserting into Accounts table
    var statement = `INSERT INTO accounts VALUES `;
    statement += `('${req.body.data.accNo}', '${req.body.data.accType}', ${req.body.data.balance});`;
    const result = await db.query(statement);

    // Inserting into the accountholders table
    statement = `INSERT INTO accountholders VALUES `;
    statement += `('${req.body.data.accNo}', '${req.body.data.customerID}');`;
    const result1 = await db.query(statement);

    res.status(200).json({
      status: "Successfully added",
    });
  } catch (err) {
    res.status(400).json({
      status: "Failed to Create Account",
      data: {
        err,
      },
    });
  }
};

exports.getAllAccounts = async (req, res) => {
  try {
    const sqlStatement = `SELECT * FROM accounts`;
    const result = await db.query(sqlStatement);

    res.status(200).json({
      status: "Success",
      data: {
        accounts: result,
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
 * @param {*} req Pass the request object. Params in the request should be set to AgentID
 * @param {*} res Pass the response object
 *
 * Returns an array containing all accounts assigned to the agent with the passed AgentID
 */
exports.getAllAccByAgentID = async (req, res) => {
  try {
    const agentID = req.params.id;
    const sqlStatement = `SELECT DISTINCT accountNumber, accountType, accountBalance FROM accounts NATURAL JOIN accountholders NATURAL JOIN customer WHERE agentID = ${agentID}`;
    const result = await db.query(sqlStatement);

    res.json({
      status: "Success",
      data: {
        accounts: result,
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

exports.getAllAccByNIC = async (req, res) => {
  try {
    const customerID = req.query.id;
    const sqlStatement = `SELECT * FROM accounts NATURAL JOIN accountholders WHERE customerID = ${customerID}`;
    const result = await db.query(sqlStatement);

    res.json({
      status: "Success",
      data: {
        accounts: result,
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
 * Returns an array containing one element. ELement at index [0] is the requested account
 */
exports.getAccount = async (req, res) => {
  try {
    const accNo = req.query.id;
    const sqlStatement = `SELECT * FROM accounts WHERE accountNumber = ${accNo}`;
    const result = await db.query(sqlStatement);

    res.status(200).json({
      status: "Success",
      data: {
        accounts: result[0],
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

exports.updateAccount = async (req, res) => {
  res.status(400).json({
    status: "Failed",
    data: {
      message: "Don't Do this machang",
    },
  });
};

exports.deleteAccount = async (req, res) => {
  try {
    const accNo = req.params.id;
    const sqlStatement = `DELETE FROM ${tableName} WHERE accNo = ${accNo}`;
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
