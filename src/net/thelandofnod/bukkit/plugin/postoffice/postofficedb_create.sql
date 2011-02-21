-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.49-1ubuntu8.1


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema postofficedb
--

CREATE DATABASE IF NOT EXISTS postofficedb;
USE postofficedb;

--
-- Definition of table `postofficedb`.`po_mail`
--

DROP TABLE IF EXISTS `postofficedb`.`po_mail`;
CREATE TABLE  `postofficedb`.`po_mail` (
  `index` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto-generated unique IDs for each message we track in the system',
  `datetime` text NOT NULL COMMENT 'The timestamp for the message',
  `state` text NOT NULL COMMENT 'the state of the message, such as read, unread',
  `sender` varchar(50) NOT NULL COMMENT 'The player or mechanism that created the message',
  `recipient` varchar(50) NOT NULL COMMENT 'the player or mechanism that is to receive the message',
  `message` text NOT NULL COMMENT 'the payload of the message',
  PRIMARY KEY (`index`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COMMENT='net.thelandofnod.bukkit.plugin.postoffice';

--
-- Dumping data for table `postofficedb`.`po_mail`
--

/*!40000 ALTER TABLE `po_mail` DISABLE KEYS */;
LOCK TABLES `po_mail` WRITE;
INSERT INTO `postofficedb`.`po_mail` VALUES  (1,'02-20-2011 05:47:59 PM','READ','offstar','offstar','hello offstar!'),
 (2,'02-20-2011 05:48:11 PM','READ','offstar','offstar','this is a test message...'),
 (3,'02-20-2011 08:52:03 PM','READ','offstar','offstar','sfdwjerhoi2i34i5ojrkjskdjfwejtrlksjflksjdtkj4lktrjwer'),
 (4,'02-20-2011 10:17:18 PM','READ','offstar','offstar','test'),
 (5,'02-20-2011 10:31:28 PM','UNREAD','offstar','forestdweller','hello! How are you?'),
 (6,'02-21-2011 02:47:54 PM','READ','offstar','offstar','this is another test message.');
UNLOCK TABLES;
/*!40000 ALTER TABLE `po_mail` ENABLE KEYS */;


--
-- Definition of table `postofficedb`.`po_package`
--

DROP TABLE IF EXISTS `postofficedb`.`po_package`;
CREATE TABLE  `postofficedb`.`po_package` (
  `index` int(11) NOT NULL AUTO_INCREMENT,
  `datetime` text NOT NULL,
  `state` text NOT NULL,
  `sender` varchar(50) NOT NULL,
  `recipient` varchar(50) NOT NULL,
  `materialId` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`index`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `postofficedb`.`po_package`
--

/*!40000 ALTER TABLE `po_package` DISABLE KEYS */;
LOCK TABLES `po_package` WRITE;
INSERT INTO `postofficedb`.`po_package` VALUES  (1,'02-20-2011 07:21:25 PM','READ','offstar','offstar',46,64),
 (2,'02-20-2011 07:22:45 PM','READ','offstar','offstar',46,64),
 (3,'02-21-2011 11:07:01 AM','READ','offstar','offstar',46,64);
UNLOCK TABLES;
/*!40000 ALTER TABLE `po_package` ENABLE KEYS */;


--
-- Definition of table `postofficedb`.`po_userregistry`
--

DROP TABLE IF EXISTS `postofficedb`.`po_userregistry`;
CREATE TABLE  `postofficedb`.`po_userregistry` (
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `postofficedb`.`po_userregistry`
--

/*!40000 ALTER TABLE `po_userregistry` DISABLE KEYS */;
LOCK TABLES `po_userregistry` WRITE;
INSERT INTO `postofficedb`.`po_userregistry` VALUES  ('forestdweller'),
 ('offstar');
UNLOCK TABLES;
/*!40000 ALTER TABLE `po_userregistry` ENABLE KEYS */;


--
-- Definition of function `postofficedb`.`IsRegistered`
--

DROP FUNCTION IF EXISTS `postofficedb`.`IsRegistered`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` FUNCTION  `postofficedb`.`IsRegistered`(playerName TEXT) RETURNS int(1)
BEGIN
  RETURN (SELECT COUNT(*) FROM po_userregistry WHERE username=playerName);
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeMarkMessageReadEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeMarkMessageReadEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeMarkMessageReadEvent`(IN playerName TEXT, IN messageIndex INT)
BEGIN
  START TRANSACTION;
    IF IsRegistered(playerName)>0 THEN
      UPDATE po_mail SET `state`='READ' WHERE `index`=messageIndex AND `recipient`=playerName;
    END IF;
  COMMIT;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeMarkMessagesReadEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeMarkMessagesReadEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeMarkMessagesReadEvent`(IN recipientName TEXT)
BEGIN
  START TRANSACTION;
    IF IsRegistered(recipientName) > 0 THEN
      UPDATE po_mail SET state='READ' WHERE recipient=recipientName;
    END IF;
  COMMIT;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeMarkPackagesReadEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeMarkPackagesReadEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeMarkPackagesReadEvent`(IN recipientName TEXT)
BEGIN
    START TRANSACTION;
      IF IsRegistered(recipientName)>0 THEN
        UPDATE po_package SET state='READ' WHERE recipient=recipientName;
      END IF;
    COMMIT;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeReadMessageEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeReadMessageEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeReadMessageEvent`(IN recipientName TEXT)
BEGIN
    IF IsRegistered(recipientName)>0 THEN
      SELECT * FROM po_mail WHERE state='UNREAD' AND recipient=recipientName;
    END IF;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeReadMessageWithIdEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeReadMessageWithIdEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeReadMessageWithIdEvent`(IN recipientName TEXT, IN messageId INTEGER)
BEGIN
    IF IsRegistered(recipientName)>0 THEN
      SELECT * FROM po_mail WHERE state != 'DELETED' AND recipient=recipientName AND `index`=messageId;
    END IF;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeRecallPackageEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeRecallPackageEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeRecallPackageEvent`(IN recipientName TEXT)
BEGIN
  START TRANSACTION;
    IF IsRegistered(recipientName)>0 THEN
      UPDATE po_package SET recipient=recipientName WHERE sender=recipientName AND state='UNREAD';
    END IF;
  COMMIT;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeReceivePackageEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeReceivePackageEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeReceivePackageEvent`(IN recipientName TEXT)
BEGIN
  IF IsRegistered(recipientName)>0 THEN
    SELECT * FROM po_package WHERE state='UNREAD' AND recipient=recipientName;
  END IF;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeSendMessageEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeSendMessageEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeSendMessageEvent`(IN newTime TEXT, IN sender TEXT, IN recipient TEXT, IN message TEXT)
BEGIN
  START TRANSACTION;
    IF IsRegistered(sender)>0 AND IsRegistered(recipient)>0 THEN
      INSERT INTO po_mail (datetime, state, sender, recipient, message) VALUES (newTime, 'UNREAD', sender, recipient, message);
    END IF;
  COMMIT;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeSendPackageEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeSendPackageEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeSendPackageEvent`(IN newTime TEXT, IN sender TEXT, IN recipient TEXT, IN materialId TEXT, IN amount TEXT)
BEGIN
  START TRANSACTION;
    IF IsRegistered(sender)>0 AND IsRegistered(recipient)>0 THEN
      INSERT INTO po_package (datetime, state, sender, recipient, materialId, amount) VALUES (newTime, 'UNREAD', sender, recipient, materialId, amount);
    ELSE
      INSERT INTO po_package (datetime, state, sender, recipient, materialId, amount) VALUES (newTime, 'UNREAD', sender, sender, materialId, amount);
    END IF;
  COMMIT;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_onPostOfficeViewInboxEvent`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_onPostOfficeViewInboxEvent`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_onPostOfficeViewInboxEvent`(IN playerName TEXT)
BEGIN
    IF IsRegistered(playerName)>0 THEN
      (SELECT 'MAIL' AS 'type', `index`, datetime, `state`, sender, message FROM po_mail WHERE recipient=playerName AND `state` != 'DELETED')
      UNION
      (SELECT 'PACKAGE' AS 'type', `index`, datetime, `state`, sender, CONCAT(CONCAT(CAST(materialId AS CHAR)," of "),CAST(amount AS CHAR)) AS 'message' FROM po_package WHERE recipient=playerName AND `state` != 'DELETED') ORDER BY type, `index`, datetime, `state`;
    END IF;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `postofficedb`.`sp_RegisterUser`
--

DROP PROCEDURE IF EXISTS `postofficedb`.`sp_RegisterUser`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE  `postofficedb`.`sp_RegisterUser`(IN playerName TEXT)
BEGIN
  START TRANSACTION;
    IF IsRegistered(playerName)=0 THEN
      INSERT INTO po_userregistry (username) VALUES (playerName);
    END IF;
  COMMIT;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
