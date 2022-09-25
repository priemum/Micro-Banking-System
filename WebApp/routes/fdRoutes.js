const express = require("express");
const fdController = require("../controllers/fdController");

const router = express.Router();

router.route("/").post(fdController.createFD).get(fdController.getFD);
router.route("/withdraw").post(fdController.withdrawFD);

module.exports = router;
