var express = require("express");
const path = require("path"); // To generate paths

const cookieParser = require("cookie-parser");
const bodyParser = require("body-parser");
const rateLimit = require("express-rate-limit");

const app = express();
app.use(express.json());

// Set up pug templating for views
app.set("view engine", "pug");
app.set("views", path.join(__dirname, "views"));

// Limit requests from same API
const limiter = rateLimit({
  max: 100,
  windowMs: 60 * 60 * 1000,
  message: "Too many requests from this IP, please try again in an hour!",
});
app.use("/api", limiter);

app.use(cookieParser());

//ROUTERS
const AppError = require("./utils/appError");
const globalErrorHandler = require("./controllers/errorController");
const transactionRouter = require("./routes/transactionRoutes");
const userRouter = require("./routes/userRoutes");
const syncRouter = require("./routes/syncRoutes");
const accountRouter = require("./routes/accountRoutes");
const fdRouter = require("./routes/fdRoutes");
const reportRouter = require("./routes/reportRoutes");
const viewRouter = require("./routes/viewRoutes");

// Serving static files
app.use(express.static(path.join(__dirname, "public")));

// ROUTES

app.use("/", viewRouter);
app.use("/api/v1/transaction", transactionRouter);
app.use("/api/v1/user", userRouter);
app.use("/api/v1/sync", syncRouter);
app.use("/api/v1/account", accountRouter);
app.use("/api/v1/fd", fdRouter);
app.use("/api/v1/report", reportRouter);

app.all("*", (req, res, next) => {
  next(new AppError(`Can't find ${req.originalUrl} on this server!`, 404));
});

app.use(globalErrorHandler);

module.exports = app;
