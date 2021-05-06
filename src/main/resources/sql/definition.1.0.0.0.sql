-- MySQL dump 10.13  Distrib 8.0.20, for macos10.15 (x86_64)
--
-- Host: 156.110.192.86    Database: oralcmsdb_dev
-- ------------------------------------------------------
-- Server version	5.7.19-0ubuntu0.16.04.1

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
-- Table structure for table `audioequipmentused`
--

DROP TABLE IF EXISTS `audioequipmentused`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audioequipmentused` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `AudioEquipmentUsedName` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collections`
--

DROP TABLE IF EXISTS `collections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collections` (
  `Id` smallint(6) NOT NULL AUTO_INCREMENT,
  `RepositoryId` smallint(6) DEFAULT NULL,
  `CollectionName` varchar(250) NOT NULL,
  `Number` varchar(50) DEFAULT NULL,
  `Dates` varchar(50) DEFAULT NULL,
  `Note` text,
  `Subjects` varchar(500) DEFAULT NULL,
  `Keywords` varchar(500) DEFAULT NULL,
  `Description` text,
  `ScopeAndContent` text,
  `CustodialHistory` text,
  `Size` varchar(250) DEFAULT NULL,
  `Acquisitioninfo` text,
  `Language` varchar(100) DEFAULT NULL,
  `PreservationNote` text,
  `Rights` text,
  `AccessRestrictions` text,
  `PublicationRights` text,
  `PreferredCitation` varchar(500) DEFAULT NULL,
  `RelatedCollection` varchar(500) DEFAULT NULL,
  `SeparatedMaterial` varchar(500) DEFAULT NULL,
  `OriginalLocation` varchar(500) DEFAULT NULL,
  `CopiesLocation` varchar(500) DEFAULT NULL,
  `PublicationNote` text,
  `Creator` text,
  `Contributors` text,
  `ProcessedBy` text,
  `Sponsors` text,
  `CreatedBy` int(11) NOT NULL,
  `CreatedDate` datetime NOT NULL,
  `UpdatedBy` int(11) NOT NULL,
  `UpdatedDate` datetime NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `keywords`
--

DROP TABLE IF EXISTS `keywords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `keywords` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `KeywordName` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `predefineduser`
--

DROP TABLE IF EXISTS `predefineduser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `predefineduser` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `usertype` tinyint(1) NOT NULL,
  `Name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=273 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `repositories`
--

DROP TABLE IF EXISTS `repositories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repositories` (
  `Id` smallint(1) NOT NULL AUTO_INCREMENT,
  `RepositoryName` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='			';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subjects` (
  `Id` smallint(6) NOT NULL AUTO_INCREMENT,
  `SubjectName` varchar(250) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subseries`
--

DROP TABLE IF EXISTS `subseries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subseries` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `CollectionId` smallint(6) NOT NULL,
  `SubseriesName` varchar(250) NOT NULL,
  `Number` varchar(50) DEFAULT NULL,
  `Dates` varchar(50) DEFAULT NULL,
  `Note` text,
  `Subjects` varchar(500) DEFAULT NULL,
  `Keywords` varchar(500) DEFAULT NULL,
  `Description` text,
  `ScopeAndContent` text,
  `CustodialHistory` text,
  `Size` varchar(250) DEFAULT NULL,
  `Acquisitioninfo` text,
  `Language` varchar(100) DEFAULT NULL,
  `PreservationNote` text,
  `Rights` text,
  `AccessRestrictions` text,
  `PublicationRights` text,
  `PreferredCitation` varchar(500) DEFAULT NULL,
  `RelatedCollection` varchar(500) DEFAULT NULL,
  `SeparatedMaterial` varchar(500) DEFAULT NULL,
  `OriginalLocation` varchar(500) DEFAULT NULL,
  `CopiesLocation` varchar(500) DEFAULT NULL,
  `PublicationNote` text,
  `Creator` text,
  `Contributors` text,
  `ProcessedBy` text,
  `Sponsors` text,
  `CreatedBy` int(11) NOT NULL,
  `CreatedDate` datetime NOT NULL,
  `UpdatedBy` int(11) NOT NULL,
  `UpdatedDate` datetime NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transcriptions`
--

DROP TABLE IF EXISTS `transcriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transcriptions` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectCode` varchar(50) NOT NULL,
  `IsPriority` tinyint(1) NOT NULL DEFAULT '0',
  `ReasonForPriority` varchar(50) DEFAULT NULL,
  `InitialNote` varchar(250) DEFAULT NULL,
  `CollectionId` smallint(6) NOT NULL,
  `SubseriesId` int(11) NOT NULL,
  `Interviewee` varchar(250) NOT NULL,
  `Interviewer` varchar(250) NOT NULL,
  `InterviewDate` varchar(10) NOT NULL,
  `InterviewDate1` varchar(10) DEFAULT NULL,
  `InterviewDate2` varchar(10) DEFAULT NULL,
  `Title` varchar(250) NOT NULL,
  `Description` text,
  `Keywords` varchar(250) DEFAULT NULL,
  `Subject` varchar(300) DEFAULT NULL,
  `InterviewerDescription` text,
  `InterviewerKeywords` varchar(500) DEFAULT NULL,
  `InterviewerSubjects` varchar(300) DEFAULT NULL,
  `ScopeAndContents` text,
  `Format` varchar(100) DEFAULT NULL,
  `Type` varchar(100) DEFAULT NULL,
  `Publisher` varchar(250) DEFAULT NULL,
  `RelationIsPartOf` varchar(100) DEFAULT NULL,
  `CoverageSpatial` varchar(250) DEFAULT NULL,
  `CoverageTemporal` varchar(250) DEFAULT NULL,
  `Rights` varchar(500) DEFAULT NULL,
  `Language` varchar(20) DEFAULT NULL,
  `Identifier` varchar(50) DEFAULT NULL,
  `Transcript` varchar(250) DEFAULT NULL,
  `FileName` varchar(100) DEFAULT NULL,
  `IsOnline` tinyint(1) NOT NULL DEFAULT '0',
  `IsRosetta` tinyint(1) NOT NULL DEFAULT '0',
  `IsRosettaForm` tinyint(1) NOT NULL DEFAULT '0',
  `IsRestriction` tinyint(1) NOT NULL DEFAULT '0',
  `RestrictionNote` varchar(250) DEFAULT NULL,
  `IsDarkArchive` tinyint(1) NOT NULL DEFAULT '0',
  `DarkArchiveNote` varchar(250) DEFAULT NULL,
  `AudioEquipmentUsed` varchar(50) DEFAULT NULL,
  `VideoEquipmentUsed` varchar(50) DEFAULT NULL,
  `EquipmentNumber` varchar(20) NOT NULL,
  `InterviewerNote` varchar(250) DEFAULT NULL,
  `GeneralNote` varchar(250) DEFAULT NULL,
  `ReleaseForm` tinyint(1) NOT NULL DEFAULT '0',
  `Place` varchar(100) DEFAULT NULL,
  `TranscriberAssigned` varchar(50) DEFAULT NULL,
  `AuditCheckCompleted` varchar(50) DEFAULT NULL,
  `FirstEditCompleted` varchar(50) DEFAULT NULL,
  `SecondEditCompleted` varchar(50) DEFAULT NULL,
  `ThirdEditCompleted` varchar(50) DEFAULT NULL,
  `DraftSentDate` datetime(6) DEFAULT NULL,
  `EditWithCorrectionCompleted` varchar(50) DEFAULT NULL,
  `FinalEditCompleted` varchar(50) DEFAULT NULL,
  `FinalSentDate` datetime(6) DEFAULT NULL,
  `TranscriptStatus` tinyint(1) NOT NULL DEFAULT '0',
  `MetadataDraft` varchar(50) DEFAULT NULL,
  `TranscriptNote` varchar(250) DEFAULT NULL,
  `IsAudioFormat` tinyint(1) NOT NULL DEFAULT '0',
  `IsVideoFormat` tinyint(1) NOT NULL DEFAULT '0',
  `IsBornDigital` tinyint(1) NOT NULL DEFAULT '0',
  `OriginalMediumType` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `OriginalMedium` varchar(50) DEFAULT NULL,
  `IsConvertToDigital` tinyint(1) NOT NULL DEFAULT '0',
  `ConvertToDigitalDate` datetime(6) DEFAULT NULL,
  `IsAccessMediaStatus` tinyint(1) NOT NULL DEFAULT '0',
  `TechnicalSpecification` varchar(250) DEFAULT NULL,
  `MasterFileLocation` varchar(150) DEFAULT NULL,
  `AccessFileLocation` varchar(150) DEFAULT NULL,
  `TranscriptLocation` varchar(150) DEFAULT NULL,
  `SentOut` tinyint(1) NOT NULL DEFAULT '0',
  `CreatedBy` int(11) NOT NULL,
  `CreatedDate` datetime(6) NOT NULL,
  `UpdatedBy` int(11) NOT NULL,
  `UpdatedDate` datetime(6) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3005 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `roleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usertype`
--

DROP TABLE IF EXISTS `usertype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usertype` (
  `Id` smallint(6) NOT NULL AUTO_INCREMENT,
  `UserTypeName` varchar(50) NOT NULL,
  `IsHorizontalMenu` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `videoequipmentused`
--

DROP TABLE IF EXISTS `videoequipmentused`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videoequipmentused` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `VideoEquipmentUsedName` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-22 18:38:01

ALTER TABLE `transcriptions` 
CHANGE COLUMN `EquipmentNumber` `EquipmentNumber` VARCHAR(20) NULL ;

ALTER TABLE `transcriptions` 
CHANGE COLUMN `InterviewDate` `InterviewDate` VARCHAR(50) NOT NULL ;

ALTER TABLE `transcriptions` 
CHANGE COLUMN `InterviewDates` `InterviewDates` VARCHAR(50) NULL DEFAULT NULL AFTER `Interviewer`;

-- before run this command "SELECT  ConvertToDigitalDate,DraftSentDate,FinalSentDate FROM oralcmsdb_dev.transcriptions;"
-- 
ALTER TABLE `transcriptions`  
CHANGE COLUMN `ConvertToDigitalDate` `ConvertToDigitalDate` VARCHAR(100) NULL DEFAULT NULL,
CHANGE COLUMN `DraftSentDate` `DraftSentDate` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `FinalSentDate` `FinalSentDate` VARCHAR(100) NULL DEFAULT NULL ;

UPDATE transcriptions SET ConvertToDigitalDate = DATE_FORMAT(ConvertToDigitalDate, '%m/%d/%Y');
UPDATE transcriptions SET DraftSentDate = DATE_FORMAT(DraftSentDate, '%m/%d/%Y');
UPDATE transcriptions SET FinalSentDate = DATE_FORMAT(FinalSentDate, '%m/%d/%Y');

CHANGE COLUMN `ConvertToDigitalDate` `ConvertToDigitalDate` VARCHAR(10) NULL DEFAULT NULL,
CHANGE COLUMN `DraftSentDate` `DraftSentDate` VARCHAR(10) NULL DEFAULT NULL ,
CHANGE COLUMN `FinalSentDate` `FinalSentDate` VARCHAR(10) NULL DEFAULT NULL ;


ALTER TABLE `oralcmsdb`.`transcriptions` 
DROP COLUMN `IsRosettaForm`;

