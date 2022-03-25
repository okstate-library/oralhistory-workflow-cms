-- ALTER TABLE `collections`
-- CHANGE COLUMN `PreferredCitation` `PreferredCitation` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `ProjectProcess` `ProjectProcess` TEXT NULL DEFAULT NULL,
-- CHANGE COLUMN `Publisher` `Publisher` TEXT NULL DEFAULT NULL ;

-- ALTER TABLE `series` 
-- CHANGE COLUMN `Publisher` `Publisher` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `PreferredCitation` `PreferredCitation` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `ProjectProcess` `ProjectProcess` TEXT NULL DEFAULT NULL ;

-- ALTER TABLE `transcriptions` 
-- ADD COLUMN `IsNarratorRevisionReceived` TINYINT(1) NOT NULL DEFAULT 0 AFTER `UpdatedDate`,
-- ADD COLUMN `RelatedInterviews` VARCHAR(250) NULL AFTER `SentOut`;

-- ALTER TABLE `transcriptions` 
-- CHANGE COLUMN `ReasonForPriority` `ReasonForPriority` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `InitialNote` `InitialNote` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `TranscriptNote` `TranscriptNote` TEXT NULL DEFAULT NULL,
-- CHANGE COLUMN `IsNarratorRevisionReceived` `IsNarratorRevisionReceived` TINYINT(1) NOT NULL DEFAULT '0' AFTER `IsProductionMasterFileStatus`,
-- CHANGE COLUMN `Rights` `Rights` TEXT NULL DEFAULT NULL,
-- CHANGE COLUMN `InterviewerKeywords` `InterviewerKeywords` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `InterviewerSubjects` `InterviewerSubjects` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `RestrictionNote` `RestrictionNote` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `DarkArchiveNote` `DarkArchiveNote` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `InterviewerNote` `InterviewerNote` TEXT NULL DEFAULT NULL ,
-- CHANGE COLUMN `GeneralNote` `GeneralNote` TEXT NULL DEFAULT NULL;