const express = require("express");
const authController = require("../controllers/authController");
const transactionController = require("../controllers/transactionController");

const router = express.Router();

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

module.exports = router;
