-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 19, 2022 at 08:37 AM
-- Server version: 8.0.21
-- PHP Version: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `microbank`
--

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE IF NOT EXISTS `transaction` (
  `TransactionID` varchar(10) NOT NULL,
  `AccountNumber` varchar(15) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Time` date DEFAULT NULL,
  `TransactionType` enum('Withdraw','Deposit') DEFAULT NULL,
  `AccountType` enum('Children','Teen','Senior') DEFAULT NULL,
  `TransactionAmount` float DEFAULT NULL,
  `TransactionCharge` float DEFAULT NULL,
  PRIMARY KEY (`TransactionID`),
  KEY `AccountNumber` (`AccountNumber`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`TransactionID`, `AccountNumber`, `Date`, `Time`, `TransactionType`, `AccountType`, `TransactionAmount`, `TransactionCharge`) VALUES
('001', '1234567890', '2022-05-17', '2022-05-17', 'Withdraw', 'Children', 12, 1);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
