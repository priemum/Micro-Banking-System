const dotenv = require("dotenv");
dotenv.config({ path: "./config.env" });

const hostname = process.env.MYSQL_HOST || "localhost";
const username = process.env.MYSQL_USER || "root";
const sqlPassword = process.env.MYSQL_PASSWORD || "";
const dbName = process.env.MYSQL_DB || "mircobank";

const dbConfig = {
  db: {
    host: hostname,
    user: username,
    password: sqlPassword,
    database: dbName,
  },
};

module.exports = dbConfig;