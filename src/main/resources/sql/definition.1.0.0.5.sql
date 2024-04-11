ALTER TABLE `transcriptions` 
CHANGE COLUMN `OriginalAudioDuration` `OriginalAudioDuration` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `OriginalVideoDuration` `OriginalVideoDuration` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `EditedAudioDuration` `EditedAudioDuration` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `EditedVideoDuration` `EditedVideoDuration` VARCHAR(100) NULL DEFAULT NULL ;

ALTER TABLE `transcriptions` 
ADD COLUMN `MediaNote` VARCHAR(250) NULL AFTER `ConvertToDigitalDate`,
ADD COLUMN `NarratorBio` TEXT NULL AFTER `GeneralNote`,
ADD COLUMN `IsPotentiallySensitiveContent` TINYINT(1) NULL AFTER `IsPublic`,
ADD COLUMN `SensitiveContentDescription` TEXT NULL AFTER `IsPotentiallySensitiveContent`,
ADD COLUMN `DigitalCameraUsed` VARCHAR(50) NULL AFTER `VideoEquipmentUsed`,
ADD COLUMN `UsageInformation` TEXT NULL AFTER `NarratorBio`,
CHANGE COLUMN `IsPotentiallySensitiveContent` `IsPotentiallySensitiveContent` TINYINT(1) NULL DEFAULT 0 ,
CHANGE COLUMN `Keywords` `Keywords` TEXT NULL DEFAULT NULL ;


INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'N/A');
INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'Canon PowerShot ELPH 100 HS\n');
INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'Canon PowerShot SD1300 IS');
INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'Canon PowerShot SD780 IS');
INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'Digital camera');
INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'Nikon D3100');
INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'Nikon D3500');
INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'Nikon D40x');
INSERT INTO `equipments` (`EquipmentType`, `EquipmentUsedName`) VALUES ('3', 'Nikon D7100');