const express = require("express");
const userController = require("../controllers/userController");
const accountController = require("../controllers/accountController");

const router = express.Router();

router.route("/").get(userController.getAllLoginInfoByAgentID);
router.route("/special-request/").get(userController.getUserAndAccByID);

module.exports = router;
