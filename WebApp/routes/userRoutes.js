const express = require("express");
const userController = require("../controllers/userController");
const authController = require("../controllers/authController");
const router = express.Router();
const validator = require("../models/supportFunctions/validators");

router.post(
  "/signup", // Check if all needed fields are present
  validator.validateCustomer,
  // encrypts the password if pass = confirm pass
  validator.encryptPass,
  authController.signUp
);
router.post("/login", authController.login);
router.get("/logout", authController.logout);

router.route("/").get(userController.getAllUsers);
router
  .route("/:id")
  .get(userController.getUser)
  .patch(userController.updateUser)
  .delete(userController.deleteUser);

module.exports = router;
