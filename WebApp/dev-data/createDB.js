const mysql = require("mysql");
const dotenv = require("dotenv");
dotenv.config({ path: "../config.env" });

const fs = require("fs");

const port = process.env.port || 3000;
const hostname = process.env.mysql_host;
const username = process.env.mysql_user;
const sqlPassword = process.env.mysql_password;

//console.log(hostname, username, sqlPassword);

// Create a mySQL DB connection
var dbCon = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "",
});

// Connect to the DB
dbCon.connect((err) => {
  if (err) {
    throw err;
  }
  console.log(`Connected to DB ${hostname}`);
});

// sql statements to create the tables
const dbName = "microbank";
var createDBStatement = `CREATE DATABASE ${dbName}`;

dbCon.query(createDBStatement, (err, result) => {
  if (err) throw err;
});

var accTable =
  "CREATE TABLE `Accounts` ( `AccountNumber` VarChar(20), `CustomerNIC` VarChar(10), `AccountType` Enum ('Children', 'Teen', 'Adult', 'Senior'), `AgentID` VarChar(10), `AccountBalance` float, PRIMARY KEY (`AccountNumber`), FOREIGN KEY (`AccountType`) REFERENCES `InterestRates`(`AccountType`), FOREIGN KEY (`AgentID`) REFERENCES `AgentDetails`(`AgentID`), FOREIGN KEY (`CustomerNIC`) REFERENCES `Customer`(`CustomerNIC`) );";
var agentTable =
  "CREATE TABLE `AgentDetails` ( `AgentID` VarChar(10), `AgentNIC` VarChar(10), `ContactNo.` VarChar(10), `Name` VarChar(50), PRIMARY KEY (`AgentID`) );";
var customerTable =
  "CREATE TABLE `Customer` ( `CustomerNIC` VarChar(10), `Name` VarChar(50), `ContactNumber` VarChar(10), `Address` VarChar(100), `Birthday` Date, `Password` VarChar(20), PRIMARY KEY (`CustomerNIC`) );";
var fdIntTable =
  "CREATE TABLE `FDInterest` ( `Period` Date, `Rate` Float, PRIMARY KEY (`Period`) );";
var fdTable =
  "CREATE TABLE `FixedDeposits` ( `AccountNumber` VarChar(20), `{CustomerNIC}` VarChar(10), `Amount` float, `OpeningDate` Date, `Period` Date, `PlanType` Enum ('Type1', 'Type2', 'Type3'), PRIMARY KEY (`AccountNumber`), FOREIGN KEY (`Period`) REFERENCES `FDInterest`(`Period`) );";
var intRatesTable =
  "CREATE TABLE `InterestRates` ( `AccountType` Enum ('Children', 'Teen', 'Senior', 'Joint'), `InterestRate` Float, `MinimumAmount` float, PRIMARY KEY (`AccountType`) );";
var JATable =
  "CREATE TABLE `JointAccounts` ( `AccountNumber` VarChar(20), `{CustomerNIC}` VarChar(10), `AccountBalance` float, PRIMARY KEY (`AccountNumber`) );";
var localAccTable =
  "CREATE TABLE `LocalAccount` ( `AccountNumber` VarChar(20), `CustomerNIC` VarChar(20), `Password` VarChar(20), `Balance` float, PRIMARY KEY (`AccountNumber`) );";
var localLog =
  "CREATE TABLE `LocalTransactionLog` ( `TransactionID` VarChar(10), `AccountNumber` VarChar(15), `Date` Date, `Time` Time, `TransactionType` Enum ('Withdraw', 'Deposit'), `AccountType` Enum ('Children', 'Teen', 'Senior'), `TransactionAmount` float, `TransactionCharge` float, PRIMARY KEY (`TransactionID`), FOREIGN KEY (`AccountNumber`) REFERENCES `LocalAccount`(`AccountNumber`) );";
var transactionTable =
  "CREATE TABLE `Transaction` ( `TransactionID` VarChar(10), `AccountNumber` VarChar(15), `Date` Date, `Time` date, `TransactionType` Enum ('Withdraw', 'Deposit'), `AccountType` Enum ('Children', 'Teen', 'Senior'), `TransactionAmount` float, `TransactionCharge` float, PRIMARY KEY (`TransactionID`), FOREIGN KEY (`AccountNumber`) REFERENCES `Accounts`(`AccountNumber`) );";

dbCon = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "",
  database: dbName,
});

// Connect to the DB
dbCon.connect((err) => {
  if (err) {
    throw err;
  }
  console.log(`Connected to DB ${hostname}`);
});

dbCon.query(accTable, (err, results) => {
  if (err) throw err;
});
dbCon.query(agentTable, (err, results) => {
  if (err) throw err;
});
dbCon.query(customerTable, (err, results) => {
  if (err) throw err;
});
dbCon.query(fdIntTable, (err, results) => {
  if (err) throw err;
});
dbCon.query(fdTable, (err, results) => {
  if (err) throw err;
});
dbCon.query(intRatesTable, (err, results) => {
  if (err) throw err;
});
dbCon.query(JATable, (err, results) => {
  if (err) throw err;
});
dbCon.query(localAccTable, (err, results) => {
  if (err) throw err;
});
dbCon.query(localLog, (err, results) => {
  if (err) throw err;
});
dbCon.query(transactionTable, (err, results) => {
  if (err) throw err;
});
