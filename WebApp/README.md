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

```
BEGIN
	DECLARE num VARCHAR(20) DEFAULT "";
    DECLARE actype VARCHAR(10) DEFAULT "";
    DECLARE bal FLOAT DEFAULT 0.00;
    
	DECLARE jointAccInterest FLOAT DEFAULT 0.00;
    DECLARE childAccInterest FLOAT DEFAULT 0.00;
    DECLARE teenAccInterest FLOAT DEFAULT 0.00;
    DECLARE adultAccInterest FLOAT DEFAULT 0.00;
    DECLARE seniorAccInterest FLOAT DEFAULT 0.00;
    
    DECLARE bdone INT;
    
    DECLARE cursor_1 CURSOR FOR 
    SELECT accountNumber,accountType,accountBalance FROM microbank.accounts;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET bdone=1;
    
    SET jointAccInterest = (SELECT interestRate FROM interestrates WHERE accountType = 'joint');
    SET teenAccInterest = (SELECT interestRate FROM interestrates WHERE accountType = 'teen');
    SET adultAccInterest = (SELECT interestRate FROM interestrates WHERE accountType = 'adult');
    SET seniorAccInterest = (SELECT interestRate FROM interestrates WHERE accountType = 'senior');
    SET childAccInterest = (SELECT interestRate FROM interestrates WHERE accountType = 'child');
    
    OPEN cursor_1;
    SET bdone = 0;
    REPEAT
    	FETCH cursor_1 INTO num,actype,bal;
        IF actype='child' THEN
        	SET bal = bal + bal*(childAccInterest/100);
        ELSEIF actype = 'teen' THEN
        	SET bal = bal + bal*(teenAccInterest/100);
        ELSEIF actype = 'adult' THEN
        	SET bal = bal + bal*(adultAccInterest/100);
        ELSEIF actype = 'senior' THEN
        	SET bal = bal + bal*(seniorAccInterest/100);   
        ELSEIF actype = 'joint' THEN
        	SET bal = bal + bal*(jointAccInterest/100);
       	END IF;
        
        UPDATE accounts SET accountBalance = bal WHERE accountNumber = num;
     UNTIL bdone END REPEAT;
     CLOSE cursor_1;   
END
```


#### 5. Calculating interest rates for FDs
The interest for an FD is calculated at a 30-day interval. The interest should be credited to the balance of the corresponding savings account. This procedure is also handled as an event in this database design.

```
BEGIN
	DECLARE id INT;
	DECLARE accNum VARCHAR(20) DEFAULT "";
    DECLARE accbal FLOAT DEFAULT 0;
    DECLARE val FLOAT DEFAULT 0;
    DECLARE opened DATE;
    DECLARE closed DATE;
    DECLARE today DATE;
    DECLARE fdtype_1 VARCHAR(10);
    DECLARE numdays INT;
    
    
    DECLARE longrate FLOAT DEFAULT 0;
    DECLARE mediumrate FLOAT DEFAULT 0;
    DECLARE shortrate FLOAT DEFAULT 0;
    
    DECLARE bdone INT;
    
    DECLARE cursor_2 CURSOR FOR 
    SELECT DISTINCT accountNumber,amount, openingDate, closingDate, planType,FD_ID FROM microbank.fixeddeposits WHERE closingDate>=NOW();
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET bdone = 1;
    
    SET longrate = (SELECT interestRate FROM microbank.fdinterest WHERE fdType = 'Type1');
    SET mediumrate  = (SELECT interestRate FROM microbank.fdinterest WHERE fdType = 'Type2');
    SET shortrate = (SELECT interestRate FROM microbank.fdinterest WHERE fdType = 'Type3');
    SELECT CAST(NOW() AS Date) INTO today ;
    OPEN cursor_2;
    SET bdone=0;
    
    REPEAT
		FETCH cursor_2 INTO accNum,val,opened,closed,fdtype_1,id;
        SET accbal = (SELECT accountBalance FROM microbank.accounts WHERE accountNumber = accNum);
        
        SET numdays = DATEDIFF(opened,today);
        IF (numdays%30 =0 AND fdtype_1='Type1') THEN
			SET accbal = accbal+val*(longrate/1200);
        ELSEIF (numdays%30 =0 AND fdtype_1='Type2') THEN
			SET accbal = accbal+val*(mediumrate/1200);
        ELSEIF (numdays%30 =0 AND fdtype_1='Type3') THEN
			SET accbal = accbal+val*(shortrate/1200);
		END IF;
        
        UPDATE accounts SET accountBalance = accbal WHERE accountNumber = accNum;
	UNTIL bdone END REPEAT;
    CLOSE cursor_2;
END
```

