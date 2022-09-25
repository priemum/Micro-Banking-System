const db = require("../models/supportFunctions/dbOperations");

const tableName = "fixeddeposits";
const tableCols = `(accountNumber, customerID, amount, openingDate, planType, closingDate)`;

exports.createFD = async (req, res) => {
  try {
    console.log("createFD");
    console.log(req.body);
    const {
      accountNumber,
      customerID,
      amount,
      openingDate,
      planType,
      closingDate,
    } = req.body;
    var sqlStatement = `INSERT INTO ${tableName} ${tableCols} VALUES `;
    sqlStatement += `('${accountNumber}', '${customerID}', ${amount}, '${openingDate}', '${planType}', '${closingDate}');`;
    const result = await db.query(sqlStatement);

    if (result) {
      res.status(200).json({
        status: "success",
      });
    } else {
      res.status(400).json({
        status: "Failed",
      });
    }
  } catch (err) {
    console.log(err);
    res.status(400).json({
      status: "Failed",
    });
  }
};

exports.withdrawFD = async (req, res) => {
  try {
    const id = req.body.FD_ID;
    // const account = getFdByID(id);
    // // Get the FD balance
    // const balance = account[0].amount;
    // const accNo = account[0].accountNumber;
    // Add the balance to the account Number as a transaction

    // Delete the FD record
    console.log(id);
    var sqlStatement = `DELETE FROM ${tableName} WHERE FD_ID = ${id}`;
    const deleteRes = await db.query(sqlStatement);

    res.status(200).json({
      status: "success",
      data: {
        deleted: deleteRes,
      },
    });
  } catch (err) {
    res.status(400).json({
      status: "Failed",
      data: err,
    });
  }
};

exports.getFD = async (req, res) => {
  try {
    var fdList = [];
    const { id, accNo } = req.query;
    if (!id) {
      const sqlStatement = `SELECT * FROM ${tableName} WHERE accountNumber = ${accNo}`;
      fdList = await db.query(sqlStatement);
    } else {
      const sqlStatement = `SELECT * FROM ${tableName} WHERE FD_ID = ${id}`;
      fdList = await db.query(sqlStatement);
    }

    res.status(200).json({
      status: "Success",
      data: {
        fd: fdList,
      },
    });
  } catch (err) {
    res.status(400).json({
      status: "Failed",
      data: err,
    });
  }
};
