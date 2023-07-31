ALTER TABLE `transcriptions` 
ADD COLUMN `InterviewContext` TEXT NULL AFTER `InterviewerNote`;

ALTER TABLE `transcriptions` 
CHANGE COLUMN `InterviewContext` `InterviewContext` TEXT NULL DEFAULT NULL AFTER `InterviewerNote`;

ALTER TABLE `transcriptions` 
CHANGE COLUMN `InterviewDates` `InterviewDates` VARCHAR(250) NULL DEFAULT NULL ;


ALTER TABLE `transcriptions` 
DROP COLUMN `AccessFileLocation`,
DROP COLUMN `MasterFileLocation`,
DROP COLUMN `Identifier`;

ALTER TABLE `transcriptions` 
ADD COLUMN `OnlineDate` VARCHAR(10) NULL AFTER `IsOnline`;

ALTER TABLE `transcriptions` 
ADD COLUMN `OHMSURL` VARCHAR(200) NULL AFTER `CONTENTdmURL`,
ADD COLUMN `YouTubeURL` VARCHAR(200) NULL AFTER `OHMSURL`;

ALTER TABLE `transcriptions` 
ADD COLUMN `IsPublic` TINYINT(1) NOT NULL DEFAULT 0 AFTER `UpdatedDate`;

ALTER TABLE `transcriptions` 
CHANGE COLUMN `IsPublic` `IsPublic` TINYINT(1) NOT NULL DEFAULT '0' AFTER `RelatedInterviews`;

ALTER TABLE `transcriptions` 
CHANGE COLUMN `Subject` `Subject` TEXT NULL DEFAULT NULL ;

ALTER TABLE `transcriptions` 
ADD COLUMN `DigitizationNote` VARCHAR(250) NULL AFTER `ConvertToDigitalDate`;

ALTER TABLE `transcriptions` 
ADD COLUMN `OriginalAudioDuration` VARCHAR(10) NULL AFTER `OriginalMedium`,
ADD COLUMN `OriginalVideoDuration` VARCHAR(10) NULL AFTER `OriginalAudioDuration`;

ALTER TABLE `transcriptions` 
ADD COLUMN `EditedAudioDuration` VARCHAR(10) NULL AFTER `OriginalVideoDuration`,
ADD COLUMN `EditedVideoDuration` VARCHAR(10) NULL AFTER `EditedAudioDuration`;

ALTER TABLE `transcriptions` 
ADD COLUMN `NumberOfImages` VARCHAR(10) NULL AFTER `DigitizationNote`,
ADD COLUMN `ImagesNote` VARCHAR(100) NULL AFTER `NumberOfImages`,
ADD COLUMN `NumberOfDocuments` VARCHAR(10) NULL AFTER `ImagesNote`,
ADD COLUMN `DocumentsNote` VARCHAR(100) NULL AFTER `NumberOfDocuments`;

ALTER TABLE `transcriptions` 
ADD COLUMN `TranscriptSize` VARCHAR(10) NULL AFTER `DigitizationNote`;

ALTER TABLE `transcriptions` 
DROP COLUMN `TranscriptStatus`;