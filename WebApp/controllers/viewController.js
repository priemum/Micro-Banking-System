const catchAsync = require("../utils/catchAsync");
const db = require("../models/supportFunctions/dbOperations");

exports.getHome = (req, res) => {
  res.status(200).render("home", {
    title: "Welcome",
  });
};

exports.getOverview = catchAsync(async (req, res) => {
  // 1) Get accounts of the User
  const customerID = req.query.id;
  var sqlStatement = `SELECT * FROM accounts NATURAL JOIN accountholders WHERE customerID = ${customerID}`;
  const accounts = await db.query(sqlStatement);

  sqlStatement = `SELECT * FROM fixeddeposits NATURAL JOIN accountholders WHERE customerID = ${customerID}`;
  const fixedDeposits = await db.query(sqlStatement);


  // 2) Build card Template
  // 3) Display as cards
  console.log(customerID);
  res.status(200).render("overview", {
    title: "User Overview",
    accounts: accounts,
    fds: fixedDeposits,
    customerID: customerID,
  });
});

exports.accountView = catchAsync(async (req, res) => {
  // 1) Get data
  const accNo = req.query.id;
  var sqlStatement = `SELECT * FROM accounts NATURAL JOIN accountholders WHERE accountNumber = ${accNo}`;
  const result = await db.query(sqlStatement);

  sqlStatement = `SELECT DISTINCT * FROM transaction WHERE accountNumber = ${accNo} ORDER BY date`;
  const transactions = await db.query(sqlStatement);

  // 2) Build Template

  // 3) Display
  res.status(200).render("account", {
    title: result[0].accountNumber,
    account: result[0],
    transactions: transactions,
  });
});


exports.getLogin = catchAsync(async (req, res) => {
  res.status(200).render("loginForm", {
    title: "Login",
  });
});

exports.getLogout = catchAsync(async (req, res) => {
  res.status(200).render("logoutForm", {
    title: "Logout",
  });
});

exports.getSignup = catchAsync(async (req, res) => {
  res.status(200).render("signup", {
    title: "Sign Up",
  });
});

exports.getReport = catchAsync(async (req, res) => {
  // 1) Get data
  try {
    const { accNo, agentID } = req.query;
    var result = [];

    if (!accNo) {
      const sqlStatement = `SELECT DISTINCT * FROM transaction WHERE agentID = ${agentID} ORDER BY date`;
      result = await db.query(sqlStatement);
    } else {
      const sqlStatement = `SELECT DISTINCT * FROM transaction WHERE accountNumber = ${accNo} ORDER BY date`;
      result = await db.query(sqlStatement);
    }

    var noOfRecords = result.length;

    res.status(200).render("report", {
      title: "report",
      transactions: result,
      accNo: accNo,
      agentID: agentID,
      noOfRecords: noOfRecords,
    });
  } catch (err) {
    console.log(err);
  }
});

exports.getAllAccounts = catchAsync(async (req, res) => {
  // 1) Get accounts of the User
  var sqlStatement = `SELECT * FROM accounts NATURAL JOIN accountholders`;
  const accounts = await db.query(sqlStatement);

  // 2) Build card Template
  // 3) Display as cards
  res.status(200).render("all-accounts", {
    title: "All Accounts",
    accounts: accounts,
  });
});

exports.getAllAgents = catchAsync(async (req, res) => {
  // 1) Get accounts of the User
  const customerID = req.query.id;
  var sqlStatement = `SELECT agentID FROM customer`;
  const agents = await db.query(sqlStatement);

  agents.forEach(async (agent) => {
    var tagentSQL = `SELECT COUNT(transactionID) as c FROM transaction WHERE agentID = ${agent.agentID}`;
    var numberOfTransactions = await db.query(tagentSQL);

    agent.noOfTransactions = numberOfTransactions[0].c;
    const placeholder = await db.query(tagentSQL);
  });
  const placeholder3 = await db.query(sqlStatement);

  agents.forEach(async (agent) => {
    var agentSql = `SELECT COUNT(customerID) as c FROM customer WHERE agentID = ${agent.agentID}`;
    var numberOfCustomers = await db.query(agentSql);

    agent.noOfCustomers = numberOfCustomers[0].c;
    const placeholder2 = await db.query(agentSql);
  });
  const placeholder4 = await db.query(sqlStatement);
  agents.forEach(async (agent) => {
    var tagentSQL = `SELECT transactionID as c FROM transaction WHERE agentID = ${agent.agentID} ORDER BY date`;
    var transactions = await db.query(tagentSQL);

    agent.lastTransaction = transactions[0].c;
    const placeholder = await db.query(tagentSQL);
  });

  // 2) Build card Template
  // 3) Display as cards
  const placeholder = await db.query(sqlStatement);

  res.status(200).render("agents-overview", {
    title: "Agents Overview",
    agents: agents,
  });
});

exports.getFD = catchAsync(async (req, res) => {
  const accountNumber = req.query.accNo;
  res.status(200).render("fd", {
    title: "Fixed Deposit",
    accNo: accountNumber,
  });
});

exports.withdrawFD = catchAsync(async (req, res) => {
  const FD_ID = req.query.id;
  const sqlStatement = `SELECT DISTINCT * FROM fixeddeposits WHERE FD_ID = ${FD_ID}`;
  result = await db.query(sqlStatement);
  res.status(200).render("fdWithdraw", {
    title: "WithDraw Fixed Deposit",
    FD_ID: FD_ID,
    fd: result,
  });
});