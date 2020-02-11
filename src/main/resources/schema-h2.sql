--CREATE DATABASE  IF NOT EXISTS work_calendar /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_slovenian_ci */ /*!80016 DEFAULT ENCRYPTION=N */;
--USE work_calendar;

DROP TABLE IF EXISTS work_calendar2holiday;
DROP TABLE IF EXISTS workweek;
DROP TABLE IF EXISTS holiday;
DROP TABLE IF EXISTS work_calendar;
--
-- Table structure for table holiday
--

CREATE TABLE holiday (
  id int auto_increment NOT NULL,
  name varchar(200) NOT NULL,
  date date NOT NULL,
  work_free tinyint NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_holiday_UNIQUE (id)
);
--
-- Table structure for table work_calendar
--

CREATE TABLE work_calendar (
  id int auto_increment NOT NULL,
  name varchar(45) NOT NULL,
  description varchar(256) DEFAULT NULL,
  workdays varchar(1024),
  year int NOT NULL,
  PRIMARY KEY (id)
--  UNIQUE KEY id_calendar_UNIQUE (id)
 -- UNIQUE KEY name_year_UNIQUE (name,year)
);

--
-- Table structure for table work_calendar2holiday
--

CREATE TABLE work_calendar2holiday (
  work_calendar_id int NOT NULL,
  holiday_id int NOT NULL,
  PRIMARY KEY (work_calendar_id,holiday_id),
  --UNIQUE KEY work_calendar_holiday_unique (work_calendar_id,holiday_id),
  --KEY fk_work_calendar_has_holiday_holiday1_idx (holiday_id),
  --KEY fk_work_calendar_has_holiday_work_calendar1_idx (work_calendar_id) /*!80000 INVISIBLE */,
  CONSTRAINT fk_work_calendar_has_holiday_holiday1 FOREIGN KEY (holiday_id) REFERENCES holiday (id),
  CONSTRAINT fk_work_calendar_has_holiday_work_calendar1 FOREIGN KEY (work_calendar_id) REFERENCES work_calendar (id)
);

--
-- Table structure for table workweek
--

CREATE TABLE workweek (
  id int auto_increment NOT NULL,
  description varchar(256) NOT NULL,
  week_number int DEFAULT NULL,
  work_calendar_id int NOT NULL,
  PRIMARY KEY (id,work_calendar_id),
  UNIQUE KEY id_workweek_UNIQUE (id),
  UNIQUE KEY week_number_calendar_unique (week_number,work_calendar_id),
  --KEY fk_workweek_work_calendar_idx (work_calendar_id),
  CONSTRAINT fk_workweek_work_calendar FOREIGN KEY (work_calendar_id) REFERENCES work_calendar (id)
);