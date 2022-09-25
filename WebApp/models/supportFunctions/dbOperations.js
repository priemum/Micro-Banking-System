const mysql = require("mysql2/promise");
const mysqlSync = require("mysql2");
const config = require("../../dbConfig");

exports.query = async (sql, params) => {
  const connection = await mysql.createConnection(config.db);
  // Use pool if needed to inncrease performance.
  // But saves the DB connection
  // const pool = mysql.createPool(config.db);
  const [results] = await connection.execute(sql, params);
  return results;
};

exports.querySync = (sql, params) => {
  //var results = new Array();
  var con = mysql.createConnection(config.db);
  con.query(sql, (err, res) => {
    if (err) {
      return new AppError("Couldn't get users", 400);
    }
    return res;
  });
};