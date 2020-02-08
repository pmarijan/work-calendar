--CREATE DATABASE  IF NOT EXISTS work_calendar /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_slovenian_ci */ /*!80016 DEFAULT ENCRYPTION=N */;
--USE work_calendar;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: work_calendar
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE=+00:00 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE=NO_AUTO_VALUE_ON_ZERO */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table holiday
--

DROP TABLE IF EXISTS holiday;
CREATE TABLE holiday (
  id int NOT NULL,
  name varchar(200) NOT NULL,
  date date NOT NULL,
  work_free tinyint NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_holiday_UNIQUE (id)
);
--
-- Table structure for table work_calendar
--

DROP TABLE IF EXISTS work_calendar;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE work_calendar (
  id int NOT NULL,
  name varchar(45) NOT NULL,
  description varchar(256) DEFAULT NULL,
  workdays varchar(1024),
  year int NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_calendar_UNIQUE (id),
 -- UNIQUE KEY name_year_UNIQUE (name,year)
); --ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table work_calendar2holiday
--

DROP TABLE IF EXISTS work_calendar2holiday;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE work_calendar2holiday (
  work_calendar_id int NOT NULL,
  holiday_id int NOT NULL,
  PRIMARY KEY (work_calendar_id,holiday_id),
  --UNIQUE KEY work_calendar_holiday_unique (work_calendar_id,holiday_id),
  --KEY fk_work_calendar_has_holiday_holiday1_idx (holiday_id),
  --KEY fk_work_calendar_has_holiday_work_calendar1_idx (work_calendar_id) /*!80000 INVISIBLE */,
  CONSTRAINT fk_work_calendar_has_holiday_holiday1 FOREIGN KEY (holiday_id) REFERENCES holiday (id),
  CONSTRAINT fk_work_calendar_has_holiday_work_calendar1 FOREIGN KEY (work_calendar_id) REFERENCES work_calendar (id)
);-- ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table workweek
--

DROP TABLE IF EXISTS workweek;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE workweek (
  id int NOT NULL,
  description varchar(256) NOT NULL,
  week_number int DEFAULT NULL,
  work_calendar_id int NOT NULL,
  PRIMARY KEY (id,work_calendar_id),
  UNIQUE KEY id_workweek_UNIQUE (id),
  UNIQUE KEY week_number_calendar_unique (week_number,work_calendar_id),
  --KEY fk_workweek_work_calendar_idx (work_calendar_id),
  CONSTRAINT fk_workweek_work_calendar FOREIGN KEY (work_calendar_id) REFERENCES work_calendar (id)
);-- ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-08 13:50:19
