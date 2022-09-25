const db = require("../models/supportFunctions/dbOperations");

/* Required input fields are
 * accountNumber
 * date
 * transactionType
 * transactionAmount
 * agentID
 * customerID
 * closingDate
 *
 * transactionID is AI
 * transaction charge default is 30.0
 * */

// Does transaction need an account type?
class Transaction {
  constructor(data) {
    this.tableName = "transaction";

    this.transactionID = data.transactionID;
    this.customerID = data.customerID;
    this.accountNumber = data.accountNumber;
    this.dateTime = new Date(data.date);
    this.sqlDate = this.dateTime.toISOString().slice(0, 19).replace("T", " ");
    this.reference = data.reference;

    this.transactionType = data.transactionType;
    this.transactionAmount = data.transactionAmount;
    this.transactionCharge = data.transactionCharge;
    this.agentID = data.agentID;
    this.customerID = data.customerID;

    this.statement = this.generateInsertStatement();
    this.updateBalance();
  }

  // To insert the transaction to the transaction table
  generateInsertStatement() {
    const cols =
      "(transactionID, customerID, accountNumber, date, transactionType, transactionAmount, transactionCharge, agentID, reference)";
    var statement = `INSERT INTO ${this.tableName} ${cols} VALUES`;
    var values = `('${this.transactionID}', '${this.customerID}', '${this.accountNumber}', '${this.sqlDate}', '${this.transactionType}', '${this.transactionAmount}', '${this.transactionCharge}', '${this.agentID}', '${this.reference}');`;
    const state = statement + " " + values;
    return state;
  }
  // To update the accounts table - account balance
  async updateBalance() {
    try {
      var accountStatement = `SELECT accountBalance FROM accounts WHERE accountNumber = ${this.accountNumber}`;
      var result = await db.query(accountStatement);
      // result is in the form [{accountBalance: xxx}]
      // Array of objects
      var balance = result[0].accountBalance;
      balance -= this.transactionCharge;
      if (this.transactionType === "DEPOSIT") {
        balance += this.transactionAmount;
      } else if (this.transactionType === "WITHDRAW") {
        balance -= this.transactionAmount;
      } else {
        balance += 0;
        // Should be error handled
      }
      accountStatement = `UPDATE accounts SET accountBalance = ${balance} WHERE accountNumber = ${this.accountNumber}`;
      result = await db.query(accountStatement);
    } catch (err) {}
  }
}

module.exports = Transaction;
