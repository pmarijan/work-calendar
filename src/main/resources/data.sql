--insert sample data for work calendar
INSERT INTO `work_calendar`.`work_calendar`(`id`,`name`,`description`,`workdays`,`year`) VALUES(1, 'Calendar 2020', 'Calendar for year 2020', 'MON,TUE,WED,THU,FRI', 2019);
INSERT INTO `work_calendar`.`work_calendar`(`id`,`name`,`description`,`workdays`,`year`) VALUES(2, 'Calendar 2021', 'Calendar for year 2021', 'WED,THU,FRI', 2019);
INSERT INTO `work_calendar`.`work_calendar`(`id`,`name`,`description`,`workdays`,`year`) VALUES(3, 'Calendar 2022', 'Calendar for year 2022', 'MON,TUE,WED,THU,FRI,SAT,SUN', 2019);

--insert sample data for work week
INSERT INTO `work_calendar`.`workweek` (`id`, `description`, `week_number`, `work_calendar_id`) VALUES(1, 'Work week 1', 1, 1);
INSERT INTO `work_calendar`.`workweek` (`id`, `description`, `week_number`, `work_calendar_id`) VALUES(2, 'Work week 2', 2, 1);
INSERT INTO `work_calendar`.`workweek` (`id`, `description`, `week_number`, `work_calendar_id`) VALUES(3, 'Work week 3', 3, 1);
INSERT INTO `work_calendar`.`workweek` (`id`, `description`, `week_number`, `work_calendar_id`) VALUES(4, 'Work week 4', 4, 1);
INSERT INTO `work_calendar`.`workweek` (`id`, `description`, `week_number`, `work_calendar_id`) VALUES(5, 'Work week 5', 5, 1);
INSERT INTO `work_calendar`.`workweek` (`id`, `description`, `week_number`, `work_calendar_id`) VALUES(6, 'Work week 6', 6, 1);
INSERT INTO `work_calendar`.`workweek` (`id`, `description`, `week_number`, `work_calendar_id`) VALUES(7, 'Work week 7', 7, 1);

--insert sample holiday data
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(1, 'Novo leto', '2020-01-01', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(2, 'Novo leto', '2020-01-02', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(3, 'Prešernov dan', '2020-02-08', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(4, 'Velika noč', '2020-04-12', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(5, 'Velikonočni ponedeljek', '2020-04-13', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(6, 'Dan upora proti okupatorju', '2020-04-27', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(7, 'Praznik dela', '2020-05-01', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(8, 'Praznik dela', '2020-05-02', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(9, 'Binkošti', '2020-05-31', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(10, 'Dan Primoža Trubarja', '2020-06-08', false);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(11, 'Dan državnosti', '2020-06-25', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(12, 'Marijino vnebozetje', '2020-08-15', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(13, 'Združitev Primorskih Slovencev z maticnim narodom', '2020-08-17', false);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(14, 'Vrnitev Primorske k matični domovini', '2020-09-15', false);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(15, 'Dan suverenosti', '2020-10-25', false);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(16, 'Dan reformacije', '2020-10-31', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(17, 'Dan spomina na mrtve', '2020-11-01', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(18, 'Dan Rudolfa Maistra', '2020-11-23', false);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(19, 'Božič', '2020-12-25', true);
INSERT INTO `work_calendar`.`holiday`(`id`, `name`, `date`, `work_free`) VALUES(20, 'Dan samostojnosti in enotnosti', '2020-12-26', true);