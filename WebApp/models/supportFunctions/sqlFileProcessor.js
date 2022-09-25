const mysql = require("mysql2");
const fs = require("fs");
const readline = require("readline");
const db = require("./dbOperations");

var fileName = "../models/sqlFiles/customer.sql";

var rl = readline.createInterface({
  input: fs.createReadStream(fileName),
  terminal: false,
});
rl.on("line", function (chunk) {
  db.query(chunk.toString("ascii"), function (err, sets, fields) {
    if (err) console.log(err);
  });
});
rl.on("close", function () {
  console.log("finished");
});
