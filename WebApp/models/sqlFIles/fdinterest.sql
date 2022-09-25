-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 05, 2022 at 08:55 AM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.0

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
-- Table structure for table `fdinterest`
--

CREATE TABLE `fdinterest` (
  `fdType` varchar(16) NOT NULL,
  `duration` tinyint(4) NOT NULL,
  `interestRate` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `fdinterest`
--

INSERT INTO `fdinterest` (`fdType`, `duration`, `interestRate`) VALUES
('long', 36, 15),
('medium', 12, 14),
('short', 6, 13);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `fdinterest`
--
ALTER TABLE `fdinterest`
  ADD PRIMARY KEY (`fdType`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
