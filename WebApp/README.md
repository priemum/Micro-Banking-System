# Web App for Central Server
This is the interface for accessing the central server database and its functions.

## Methodology

The central database contains all the information on all customers and their accounts. The tables in this database are:

* AccountHolders - Maps the accounts with the corresponding accountholder(customer)
* Accounts - Contains details of all accounts
* Admins - Details of all administrative officers in the bank. Only these admins can access the central database.
* AgentDetails - Details of the microbank agents.
* Customer - Contains all details of the customers
* FdInterest - There are various plans for FDs and these plans have different interest rates accordingly. The details of these interest rates are entered here.
* FixedDeposits - Details of the FDs and the corresponding savings accounts.
* InterestRates - Each type of savings account has a certain interest rate. These details are contained in this table.
* Transaction - Infromation of all the transactions.

The backend design follows the MVC architecture. The 'controller' directory contains all the controller classes, which have the necessary functions for the entities in this project. 
The files in the 'routes' folder are responsible for handling the API calls and communicating with the microbank agents.

### Important functions
#### 1. Creating new customer accounts

```javascript

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
 ```

#### 2. Creating FDs

```javascript

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
 ```

#### 3. Viewing reports

The 2 types of reports that can be viewed are :
* Agent-wise total transaction report
* account-wise total transaction report


```javascript

router
  .route("/monthly-by-agent")
  .get(
    authController.protect,
    transactionController.getAllTransactionsByAccAgent
  );
router
  .route("/monthly-by-account")
  .get(
    authController.protect,
    transactionController.getAllTransactionsByAccAgent
  );
``` 

#### 4. Calculating interest rates of normal savings accounts
Interests are calculated at the end of each month and the rates are different per the account type. Therefore this the interest calculation and the updating of the account balances are handled as events in this database design.



#### 5. Calculating interest rates for FDs
The interest for an FD is calculated at a 30-day interval. The interest should be credited to the balance of the corresponding savings account. This procedure is also handled as an event in this database design.


