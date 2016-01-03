-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: debill_schema
-- ------------------------------------------------------
-- Server version	5.7.9-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bills_summary`
--

DROP TABLE IF EXISTS `bills_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bills_summary` (
  `bill_summary_id` int(11) NOT NULL,
  `bill_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `fb_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`bill_summary_id`),
  KEY `fk_users_billssummary_fb_uid_idx` (`fb_id`),
  KEY `fk_bills_billssummary_bills_id_idx` (`bill_id`),
  KEY `fk_items_billssummary_item_id_idx` (`item_id`),
  CONSTRAINT `fk_bills_billssummary_bills_id` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`bill_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_items_billssummary_item_id` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_billssummary_fb_uid` FOREIGN KEY (`fb_id`) REFERENCES `users` (`fb_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills_summary`
--

LOCK TABLES `bills_summary` WRITE;
/*!40000 ALTER TABLE `bills_summary` DISABLE KEYS */;
/*!40000 ALTER TABLE `bills_summary` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-04 11:43:12
