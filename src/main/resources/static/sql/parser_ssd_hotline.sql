CREATE DATABASE  IF NOT EXISTS `parser` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `parser`;
-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: parser
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ssd_hotline`
--

DROP TABLE IF EXISTS `ssd_hotline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ssd_hotline` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avg_price` double DEFAULT NULL,
  `capacity` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `manufacturer` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `prices` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `propositions_quantity` int DEFAULT NULL,
  `reading_speed` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `writing_speed` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssd_hotline`
--

LOCK TABLES `ssd_hotline` WRITE;
/*!40000 ALTER TABLE `ssd_hotline` DISABLE KEYS */;
INSERT INTO `ssd_hotline` VALUES (291,3008.5,'960','Kingston','A400 960 GB (SA400S37/960G)','2 221 – 3 796 грн',99,'500 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/kingston-ssdnow-a400-960-gb-sa400s37960g/','450 МБ/с'),(292,4125,'1000','Samsung','870 EVO 1 TB (MZ-77E1T0BW)','3 690 – 4 560 грн',49,'560 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-870-evo-1-tb-mz-77e1t0bw/','530 МБ/с'),(293,5007.5,'1000','Kingston','KC600 1 TB (SKC600/1024G)','4 040 – 5 975 грн',95,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/kingston-kc600-1-tb-skc6001024g/','520 МБ/с'),(294,3635.5,'1000','Transcend','SSD230S 1 TB (TS1TSSD230S)','2 807 – 4 464 грн',52,'560 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/transcend-ssd230s-1-tb-ts1tssd230s/','500 МБ/с'),(295,4399,'1000','Samsung','870 QVO 1 TB (MZ-77Q1T0BW)','3 419 – 5 379 грн',55,'560 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-870-qvo-1-tb-mz-77q1t0bw/','530 МБ/с'),(296,4619.5,'1000','Samsung','870 EVO 1 TB (MZ-77E1T0B)','3 599 – 5 640 грн',67,'560 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-870-evo-1-tb-mz-77e1t0b/','530 МБ/с'),(297,7221.5,'960','Kingston','DC600M 960 GB ( SEDC600M/960G)','5 651 – 8 792 грн',59,'560 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/kingston-dc600m-960-gb-sedc600m960g/','530 МБ/с'),(298,7638.5,'960','Samsung','PM883 Enterprise 960 GB (MZ7LH960HAJR)','5 315 – 9 962 грн',59,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-pm883-enterprise-960-gb-mz7lh960hajr/','520 МБ/с'),(299,6934.5,'1000','Samsung','860 EVO 2.5 1 TB (MZ-76E1T0BW)','6 285 – 7 584 грн',2,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-860-evo-25-1-tb-mz-76e1t0bw/','520 МБ/с'),(300,6887.5,'1000','Samsung','860 QVO 1 TB (MZ-76Q1T0BW)','6 575 – 7 200 грн',2,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-860-qvo-1-tb-mz-76q1t0bw/','520 МБ/с'),(301,10761.5,'960','Samsung','PM893 960 GB (MZ7L3960HCJR-00A07)','9 072 – 12 451 грн',61,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-pm893-960-gb-mz7l3960hcjr-00a07/','520 МБ/с'),(302,9360,'1000','Samsung','850 EVO MZ-75E1T0B','9 360 грн',1,'540 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-850-evo-mz-75e1t0b/','520 МБ/с'),(303,21395.5,'1024','Transcend','TS1TSSD370S','19 256 – 23 535 грн',8,'560 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/transcend-ts1tssd370s/','460 МБ/с'),(304,12720,'960','Samsung','SM863 960 GB (MZ7KM960HAHP-00005)','12 720 грн',1,'520 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-sm863-960-gb-mz7km960hahp-00005/','485 МБ/с'),(305,11760,'960','Samsung','SM863a 960 GB (MZ7KM960HMJP-00005)','11 760 грн',1,'510 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-sm863a-960-gb-mz7km960hmjp-00005/','485 МБ/с'),(306,16817.5,'1000','Samsung','860 PRO 1 TB (MZ-76P1T0BW)','15 875 – 17 760 грн',2,'560 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-860-pro-1-tb-mz-76p1t0bw/','530 МБ/с'),(307,12275,'960','Kingston','UV500 2.5 960 GB (SUV500/960G)','12 275 грн',1,'520 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/kingston-uv500-25-960-gb-suv500960g/','500 МБ/с'),(308,14305,'960','Samsung','883 DCT 960 GB (MZ-7LH960NE)','14 305 грн',1,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-883-dct-960-gb-mz-7lh960ne/','520 МБ/с'),(309,8784,'960','Kingston','DC500R 960 GB (SEDC500R/960G)','8 784 грн',1,'555 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/kingston-dc500r-960-gb-sedc500r960g/','525 МБ/с'),(310,10464,'960','Kingston','DC500M 960 GB (SEDC500M/960G)','10 464 грн',1,'555 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/kingston-dc500m-960-gb-sedc500m960g/','520 МБ/с'),(311,6897.5,'960','Kingston','DC450R 960 GB (SEDC450R/960G)','5 299 – 8 496 грн',13,'560 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/kingston-dc450r-960-gb-sedc450r960g/','530 МБ/с'),(312,7584,'1000','Kingston','KC600 1 TB Upgrade Bundle Kit (SKC600B/1024G)','7 584 грн',1,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/kingston-kc600-1-tb-upgrade-bundle-kit-skc600b1024g/','520 МБ/с'),(313,3427.5,'960','Transcend','SSD220S 960 GB (TS960GSSD220S)','3 351 – 3 504 грн',2,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/transcend-ssd220s-960-gb-ts960gssd220s/','500 МБ/с'),(314,3284,'1000','Transcend','SSD220Q 1 TB (TS1TSSD220Q)','2 632 – 3 936 грн',19,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/transcend-ssd220q-1tb-ts1tssd220q/','500 МБ/с'),(315,13824,'960','Samsung','SM883 960 GB (MZ7KH960HAJR)','13 824 грн',1,'540 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-sm883-960-gb-mz7kh960hajr/','520 МБ/с'),(316,16036.5,'960','Samsung','PM897 960 GB (MZ7L3960HBLT)','13 161 – 18 912 грн',51,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-pm897-960-gb-mz7l3960hblt-00a07/','470 МБ/с'),(317,4179.5,'1000','Transcend','SSD225S 1 TB (TS1TSSD225S)','2 599 – 5 760 грн',43,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/transcend-ssd225s-1-tb-ts1tssd225s/','500 МБ/с'),(318,6934.5,'1000','Samsung','860 EVO 2.5 1 TB (MZ-76E1T0B)','6 285 – 7 584 грн',2,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-860-evo-25-1-tb-mz-76e1t0bam/','520 МБ/с'),(319,11808,'960','Samsung','860 DCT 960 GB (MZ-76E960E)','11 808 грн',1,'550 МБ/с','SSD','https://hotline.ua/ua/computer-diski-ssd/samsung-860-dct-960-gb-mz-76e960e/','520 МБ/с');
/*!40000 ALTER TABLE `ssd_hotline` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-30 23:58:55
