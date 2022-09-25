const db = require("../models/supportFunctions/dbOperations");
const Transaction = require("../models/transactionModel");

const catchAsync = require("../utils/catchAsync");
const AppError = require("../utils/appError");

const tableCols =
  "(transactionID, accountNumber, date, transactionType, transactionAmount, transactionCharge, agentID)";
const tableName = "transaction";

exports.validateBody = async (req, res) => {
  // Check if all needed data are present in the request body
};

exports.createTransaction = async (req, res) => {
  try {
    const transactionArray = req.body.data;
    transactionArray.forEach((elem) => {
      const newTransaction = new Transaction(elem);
      var sqlStatement = newTransaction.statement;
      const result = db.query(sqlStatement);
    });

    res.status(200).json({
      status: "Successfully added",
      data: {},
    });
  } catch (err) {
    console.log(err);
    res.status(400).json({
      status: "Failed to add",
      data: {
        err,
      },
    });
  }
};

exports.getAllTransactions = async (req, res) => {
  try {
    const sqlStatement = `SELECT * FROM ${tableName}`;
    const result = await db.query(sqlStatement);

    res.status(200).json({
      status: "Success",
      data: {
        transactions: result,
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

/**Get transaction by ID
 * Input ID in req.params
 */
exports.getTransaction = async (req, res) => {
  try {
    const transactionID = req.params.id;
    const sqlStatement = `SELECT * FROM ${tableName} WHERE transactionID = ${transactionID}`;
    const result = await db.query(sqlStatement);

    res.status(200).json({
      status: "Success",
      data: {
        transactions: result.affectedRows,
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

// No need to update transaction details
exports.updateTransaction = async (req, res) => {
  res.status(400).json({
    status: "Why are yout trying update a Transaction???",
    data: {
      err,
    },
  });
};

/**
 *
 * @param {*} req
 * @param {*} res
 *
 * Don't Do this machang without permission
 */
exports.deleteTransaction = async (req, res) => {
  try {
    const transactionID = req.params.id;
    const sqlStatement = `DELETE FROM ${tableName} WHERE transactionID = ${transactionID}`;
    const result = await db.query(sqlStatement);

    res.status(200).json({
      status: "Success",
      data: {
        transactions: result[0],
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
 * @param {*} req pass accNo or agentID in the req.query object
 * @param {*} res res.data object has transactions array with all matching transactions
 */
exports.getAllTransactionsByAccAgent = async (req, res) => {
  try {
    const { accNo, agentID } = req.query;
    var result = [];

    if (!accNo) {
      const sqlStatement = `SELECT DISTINCT * FROM ${tableName} WHERE agentID = ${agentID} ORDER BY date`;
      result = await db.query(sqlStatement);
    } else {
      const sqlStatement = `SELECT DISTINCT * FROM ${tableName} WHERE accountNumber = ${accNo} ORDER BY date`;
      result = await db.query(sqlStatement);
    }

    res.status(200).json({
      status: "Success",
      data: {
        transactions: result,
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