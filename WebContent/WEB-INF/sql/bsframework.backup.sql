-- MySQL dump 10.13  Distrib 5.6.20, for Win64 (x86_64)
--
-- Host: localhost    Database: bsframework
-- ------------------------------------------------------
-- Server version	5.6.20

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
-- Table structure for table `tconfig`
--

DROP TABLE IF EXISTS `tconfig`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tconfig` (
  `cId` bigint(20) NOT NULL AUTO_INCREMENT,
  `cKey` varchar(20) NOT NULL,
  `cValue` varchar(300) NOT NULL,
  PRIMARY KEY (`cId`),
  UNIQUE KEY `cKey` (`cKey`),
  UNIQUE KEY `cId` (`cId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tconfig`
--

LOCK TABLES `tconfig` WRITE;
/*!40000 ALTER TABLE `tconfig` DISABLE KEYS */;
INSERT INTO `tconfig` VALUES (1,'DALEA_CONTEXT','/dalea-web'),(2,'TIMECTRL_CONTEXT','/timectrl-web'),(3,'STATIC_CONTEXT','http://www.buildersoft.cl/dalea');
/*!40000 ALTER TABLE `tconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tdomain`
--

DROP TABLE IF EXISTS `tdomain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tdomain` (
  `cId` bigint(20) NOT NULL AUTO_INCREMENT,
  `cName` varchar(100) DEFAULT NULL,
  `cDatabase` varchar(15) NOT NULL DEFAULT '' COMMENT 'Indica la base de datos del dominio',
  PRIMARY KEY (`cId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tdomain`
--

LOCK TABLES `tdomain` WRITE;
/*!40000 ALTER TABLE `tdomain` DISABLE KEYS */;
INSERT INTO `tdomain` VALUES (1,'rsa','rsa'),(2,'ossa','ossa'),(3,'enlasa','enlasa'),(4,'enlasa','enlasa');
/*!40000 ALTER TABLE `tdomain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tdomainattribute`
--

DROP TABLE IF EXISTS `tdomainattribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tdomainattribute` (
  `cId` bigint(20) NOT NULL AUTO_INCREMENT,
  `cDomain` bigint(20) NOT NULL,
  `cKey` varchar(20) DEFAULT NULL,
  `cName` varchar(20) DEFAULT NULL,
  `cValue` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`cId`),
  KEY `domainAttribute_index_domain` (`cDomain`),
  CONSTRAINT `domainAttributeToDomain` FOREIGN KEY (`cDomain`) REFERENCES `tdomain` (`cId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tdomainattribute`
--

LOCK TABLES `tdomainattribute` WRITE;
/*!40000 ALTER TABLE `tdomainattribute` DISABLE KEYS */;
INSERT INTO `tdomainattribute` VALUES (1,4,'database.driver','Driver','org.gjt.mm.mysql.Driver'),(2,4,'database.server','Server','localhost'),(3,4,'database.database','Database','enlasa'),(4,4,'database.username','User','root'),(5,4,'database.password','Password','admin');
/*!40000 ALTER TABLE `tdomainattribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_userdomain`
--

DROP TABLE IF EXISTS `tr_userdomain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_userdomain` (
  `cUser` bigint(20) NOT NULL,
  `cDomain` bigint(20) NOT NULL,
  PRIMARY KEY (`cUser`,`cDomain`),
  KEY `index_domain` (`cDomain`),
  KEY `index_user` (`cUser`),
  CONSTRAINT `r_UserDomainToDomain` FOREIGN KEY (`cDomain`) REFERENCES `tdomain` (`cId`),
  CONSTRAINT `r_UserDomainToUser` FOREIGN KEY (`cUser`) REFERENCES `tuser` (`cId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_userdomain`
--

LOCK TABLES `tr_userdomain` WRITE;
/*!40000 ALTER TABLE `tr_userdomain` DISABLE KEYS */;
INSERT INTO `tr_userdomain` VALUES (1,1),(4,1),(1,2),(4,2),(1,4);
/*!40000 ALTER TABLE `tr_userdomain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tsession`
--

DROP TABLE IF EXISTS `tsession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tsession` (
  `cId` bigint(20) NOT NULL AUTO_INCREMENT,
  `cToken` varchar(50) NOT NULL,
  `cLastAccess` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cId`),
  UNIQUE KEY `cSessionId` (`cToken`),
  KEY `Index_Token` (`cToken`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tsession`
--

LOCK TABLES `tsession` WRITE;
/*!40000 ALTER TABLE `tsession` DISABLE KEYS */;
INSERT INTO `tsession` VALUES (23,'332AA1F3F8428BC0847C228C679915281458526137009','2016-03-21 01:08:57'),(24,'332AA1F3F8428BC0847C228C679915281458526174807','2016-03-21 01:09:35'),(25,'332AA1F3F8428BC0847C228C679915281458526207427','2016-03-21 01:10:08'),(42,'CA02CEF17F3793685E8CCFAFBC5C2A841458704975147','2016-03-23 02:49:38'),(46,'CE527050B70C20EAE6BD67B6B458731A1459123464985','2016-03-27 23:49:25'),(47,'58196EE649BEABDFD4A0447D66FC39801459221040215','2016-03-29 02:11:26'),(56,'6A15C9BB33C8A1D194BB8B64FF141E4C1459301640182','2016-03-30 00:36:25'),(60,'66B309FD81CCB0B5D28A4CB084675CD61459654189604','2016-04-03 02:31:40'),(64,'226AB8185A8B1916444EBA251D009EF91459824280345','2016-04-05 02:02:31'),(65,'400401E17977A19D70E855CCBF400B5D1460324189347','2016-04-10 20:59:11');
/*!40000 ALTER TABLE `tsession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tsessiondata`
--

DROP TABLE IF EXISTS `tsessiondata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tsessiondata` (
  `cId` bigint(20) NOT NULL AUTO_INCREMENT,
  `cSession` bigint(20) NOT NULL,
  `cName` varchar(250) NOT NULL,
  `cData` mediumtext,
  PRIMARY KEY (`cId`),
  KEY `Index_Session` (`cSession`),
  CONSTRAINT `SessionData_To_Session` FOREIGN KEY (`cSession`) REFERENCES `tsession` (`cId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tsessiondata`
--

LOCK TABLES `tsessiondata` WRITE;
/*!40000 ALTER TABLE `tsessiondata` DISABLE KEYS */;
/*!40000 ALTER TABLE `tsessiondata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tuser`
--

DROP TABLE IF EXISTS `tuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tuser` (
  `cId` bigint(20) NOT NULL AUTO_INCREMENT,
  `cMail` varchar(50) NOT NULL COMMENT 'Campo con el cual el usuario hace login, ideal es que sea el mail para que no se le olvide',
  `cName` varchar(100) NOT NULL COMMENT 'El nombre real del usuario',
  `cPassword` varchar(64) DEFAULT NULL COMMENT 'Codificada en md5',
  `cAdmin` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Este campo indica si tiene accedo de administrador, de manera de poder modificar usuarios de todos los dominios',
  `cLastChangePass` date DEFAULT NULL,
  PRIMARY KEY (`cId`),
  UNIQUE KEY `cMail` (`cMail`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tuser`
--

LOCK TABLES `tuser` WRITE;
/*!40000 ALTER TABLE `tuser` DISABLE KEYS */;
INSERT INTO `tuser` VALUES (1,'admin','Administrador','21232f297a57a5a743894a0e4a801fc3','','2016-05-25'),(2,'SYSTEM','* System Processor','','','2016-05-25'),(3,'ANONYMOUS','* Unknown user','','\0','2016-05-25'),(4,'claudio.moscoso@gmail.com','Claudio Moscoso','81dc9bdb52d04dc20036dbd8313ed055','','2016-05-13'),(6,'cl@cl.cl','Usuario de Pruebas',NULL,'',NULL);
/*!40000 ALTER TABLE `tuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vuseradmin`
--

DROP TABLE IF EXISTS `vuseradmin`;
/*!50001 DROP VIEW IF EXISTS `vuseradmin`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vuseradmin` AS SELECT 
 1 AS `cId`,
 1 AS `cMail`,
 1 AS `cName`,
 1 AS `cAdmin`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'bsframework'
--
/*!50003 DROP PROCEDURE IF EXISTS `pListDomainAttributes` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pListDomainAttributes`(IN domain BIGINT)
BEGIN
	SELECT		da.cId 
	FROM 		bsframework.tDomainAttribute AS da
	WHERE		da.cDomain = domain;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `pListDomainsForUser` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pListDomainsForUser`(IN user BIGINT)
BEGIN
	SELECT		d.cId 
	FROM 		bsframework.tDomain AS d
	LEFT JOIN	tR_UserDomain AS r ON r.cDomain = d.cId
	WHERE		r.cUser = user;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `pSaveRUserDomain` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pSaveRUserDomain`(IN pUserId BIGINT, IN pDomainId BIGINT)
BEGIN
	IF NOT EXISTS(	SELECT	cUser 
				FROM	bsframework.tR_UserDomain
				WHERE	cUser = pUserId AND cDomain = pDomainId) THEN
				
		INSERT INTO bsframework.tR_UserDomain(cUser, cDomain) 
		VALUES(pUserId, pDomainId);
		
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `pSaveUser` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pSaveUser`(IN pId BIGINT, IN pMail VARCHAR(50), 
	IN pName VARCHAR(100))
BEGIN
	IF EXISTS(	SELECT	cId 
				FROM	bsframework.tUser
				WHERE	cId = pId) THEN

		UPDATE	bsframework.tUser
		SET		cMail = pMail,
				cName = pName
		WHERE	cId = pId;

		SELECT pId AS cId;
	ELSE
		INSERT INTO bsframework.tUser(cMail, cName, cAdmin) 
		VALUES(pMail, pName, false);
		
		SELECT LAST_INSERT_ID() AS cId;

	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `pSaveUserAdmin` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pSaveUserAdmin`(IN pId BIGINT, IN pMail VARCHAR(50), 
	IN pName VARCHAR(100), IN pAdmin BIT)
BEGIN

	IF EXISTS(	SELECT	cId 
				FROM	bsframework.tUser
				WHERE	cId = pId) THEN

		UPDATE	bsframework.tUser
		SET		cMail = pMail,
				cName = pName,
				cAdmin= pAdmin
		WHERE	cId = pId;
		
		SELECT pId AS cId;
	ELSE
		INSERT INTO bsframework.tUser(cMail, cName, cAdmin) 
		VALUES(pMail, pName, pAdmin);
		
		SELECT LAST_INSERT_ID() AS cId;
		
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `vuseradmin`
--

/*!50001 DROP VIEW IF EXISTS `vuseradmin`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vuseradmin` AS select `tuser`.`cId` AS `cId`,`tuser`.`cMail` AS `cMail`,`tuser`.`cName` AS `cName`,`tuser`.`cAdmin` AS `cAdmin` from `tuser` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-15 22:43:00
