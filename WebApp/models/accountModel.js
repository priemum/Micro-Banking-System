const db = require("../models/supportFunctions/dbOperations");
const accountCols = `accountNumber, accountType, accountBalance`;
const accountHoldersCols = `accountNumber, customerID`;

class Account {
  // Pass req.data to the constructor
  // needed data accNo, customerID, accType, balance
  constructor(data) {
    this.accNo = data.accNo;
    this.customerID = data.customerID;
    this.accType = data.accType;
    this.balance = data.balance;
    this.statement = this.generateInsertStatement();
  }

  generateInsertStatement() {
    // Inserting into the accounts table
    var statement = `INSERT INTO accounts ${accountCols} VALUES `;
    statement += `('${this.accNo}', '${this.accType}', '${this.balance});`;
    // Inserting into the accountholders table
    statement += `INSERT INTO accountholders ${accountHoldersCols} VALUES`;
    statement += `('${this.accNo}', '${this.customerID}');`;

    return `${statement}`;
  }
}
