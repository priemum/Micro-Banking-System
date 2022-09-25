const express = require("express");
const transactionController = require("../controllers/transactionController");

const router = express.Router();

router
  .route("/")
  .post(transactionController.createTransaction)
  .get(transactionController.getAllTransactions);
router
  .route("/:id")
  .post(transactionController.createTransaction)
  .get(transactionController.getTransaction)
  .patch(transactionController.updateTransaction)
  .delete(transactionController.deleteTransaction);

module.exports = router;
