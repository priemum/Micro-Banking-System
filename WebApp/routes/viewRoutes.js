const express = require("express");
const viewController = require("../controllers/viewController");
const authController = require("../controllers/authController");

const router = express.Router();

router.use(authController.isLoggedIn);

// Login/Signup
router.route("/login").get(viewController.getLogin);
router.route("/logout").get(viewController.getLogout);
router.route("/signup").get(viewController.getSignup);

// Customer Views
router.route("/").get(viewController.getHome);
router
  .route("/account-overview")
  .get(authController.protect, viewController.getOverview);
router
  .route("/account")
  .get(authController.protect, viewController.accountView);
router.route("/fd").get(authController.protect, viewController.getFD);
router
  .route("/fdwithdraw")
  .get(authController.protect, viewController.withdrawFD);

// ADMIN Views
router
  .route("/report")
  .get(authController.adminProtect, viewController.getReport);
router
  .route("/all-accounts")
  .get(authController.adminProtect, viewController.getAllAccounts);
router
  .route("/all-agents")
  .get(authController.adminProtect, viewController.getAllAgents);

module.exports = router;
