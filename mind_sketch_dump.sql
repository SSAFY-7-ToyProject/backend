-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: mind_sketch
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `diary`
--

DROP TABLE IF EXISTS `diary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diary` (
  `diaryId` int NOT NULL,
  `userId` varchar(45) NOT NULL,
  `regTime` datetime NOT NULL,
  `category` varchar(45) NOT NULL,
  `emotion` varchar(45) NOT NULL,
  `secret` tinyint(1) NOT NULL DEFAULT '0',
  `title` varchar(50) NOT NULL,
  `content` varchar(10000) NOT NULL,
  `bgm` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`diaryId`),
  KEY `userId` (`userId`),
  CONSTRAINT `diary_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diary`
--

LOCK TABLES `diary` WRITE;
/*!40000 ALTER TABLE `diary` DISABLE KEYS */;
INSERT INTO `diary` VALUES (1,'user1','2022-06-05 15:18:11','여행','행복',0,'행복한 여행 행복한 하루','오늘은 여행을 와서 기분이 좋다. 일기 끝',NULL);
/*!40000 ALTER TABLE `diary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matching_diary`
--

DROP TABLE IF EXISTS `matching_diary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matching_diary` (
  `diaryId` int NOT NULL,
  `userId` varchar(45) NOT NULL,
  `friendId` varchar(45) NOT NULL,
  `regTime` datetime NOT NULL,
  `category` varchar(45) NOT NULL,
  `emotion` varchar(45) NOT NULL,
  `secret` tinyint(1) NOT NULL DEFAULT '0',
  `title` varchar(50) NOT NULL,
  `content` varchar(10000) NOT NULL,
  `bgm` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`diaryId`),
  KEY `userId` (`userId`),
  KEY `friendId` (`friendId`),
  CONSTRAINT `matching_diary_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `matching_diary_ibfk_2` FOREIGN KEY (`friendId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matching_diary`
--

LOCK TABLES `matching_diary` WRITE;
/*!40000 ALTER TABLE `matching_diary` DISABLE KEYS */;
/*!40000 ALTER TABLE `matching_diary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matching_note`
--

DROP TABLE IF EXISTS `matching_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matching_note` (
  `matchingId` int NOT NULL,
  `userId` varchar(45) NOT NULL,
  `age` int NOT NULL,
  `regTime` datetime NOT NULL,
  `image` varchar(45) DEFAULT NULL,
  `content` varchar(1000) NOT NULL,
  PRIMARY KEY (`matchingId`),
  KEY `userId` (`userId`),
  CONSTRAINT `matching_note_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matching_note`
--

LOCK TABLES `matching_note` WRITE;
/*!40000 ALTER TABLE `matching_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `matching_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reply`
--

DROP TABLE IF EXISTS `reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reply` (
  `replyId` int NOT NULL,
  `parentReplyId` int DEFAULT NULL,
  `diaryId` int DEFAULT NULL,
  `userId` varchar(45) DEFAULT NULL,
  `regTime` datetime NOT NULL,
  `secret` tinyint(1) NOT NULL DEFAULT '0',
  `content` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`replyId`),
  KEY `diaryId` (`diaryId`),
  KEY `userId` (`userId`),
  CONSTRAINT `reply_ibfk_1` FOREIGN KEY (`diaryId`) REFERENCES `diary` (`diaryId`),
  CONSTRAINT `reply_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reply`
--

LOCK TABLES `reply` WRITE;
/*!40000 ALTER TABLE `reply` DISABLE KEYS */;
/*!40000 ALTER TABLE `reply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `age` int NOT NULL,
  `gender` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('user1','1234','james','james@gmail.com',20,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-05 16:05:57
