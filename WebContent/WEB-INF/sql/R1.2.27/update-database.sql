drop table if exists bsframework.tSessionData;
drop table if exists bsframework.tSession;

create table bsframework.tSession (
	cId		BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	cSessionId 	VARCHAR(32) NOT NULL UNIQUE,
	cLastAccess	TIMESTAMP NOT NULL
) ENGINE=innoDB;

create table bsframework.tSessionData (
	cId		BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	cSession 	BIGINT  NOT NULL,
	cName		VARCHAR(250) NOT NULL,
	cData		TEXT
) ENGINE=innoDB;

ALTER TABLE bsframework.tSessionData
ADD INDEX Index_Session (cSession ASC),
ADD CONSTRAINT SessionData_To_Session FOREIGN KEY (cSession) REFERENCES tSession(cId);

ALTER TABLE bsframework.tSession MODIFY COLUMN cSessionId varchar(50) NOT NULL;
ALTER TABLE bsframework.tsession CHANGE cSessionId cToken varchar(50) NOT NULL;

ALTER TABLE bsframework.tSession ADD INDEX Index_Token (cToken ASC);

CREATE TABLE if not exists bsframework.tConfig (
  cId bigint(20) NOT NULL AUTO_INCREMENT,
  cKey varchar(20) NOT NULL UNIQUE,
  cValue varchar(300) NOT NULL,
  PRIMARY KEY (cId),
  UNIQUE KEY cId (cId)
) ENGINE=InnoDB;

DELIMITER $$
create procedure pUpdateData_Temp()
begin
	IF(NOT EXISTS(SELECT cId FROM bsframework.tConfig WHERE cKey = 'DALEA_CONTEXT')) THEN
		INSERT INTO bsframework.tConfig(cKey, cValue) VALUES('DALEA_CONTEXT', '/dalea-web');
	END IF;

	IF(NOT EXISTS(SELECT cId FROM bsframework.tConfig WHERE cKey = 'TIMECTRL_CONTEXT')) THEN
		INSERT INTO bsframework.tConfig(cKey, cValue) VALUES('TIMECTRL_CONTEXT', '/timectrl-web');
	END IF;
	IF(NOT EXISTS(SELECT cId FROM bsframework.tConfig WHERE cKey = 'STATIC_CONTEXT')) THEN
		INSERT INTO bsframework.tConfig(cKey, cValue) VALUES('STATIC_CONTEXT', '/dalea');
	END IF;
	
END$$
DELIMITER ;

call pUpdateData_Temp;
drop procedure if exists pUpdateData_Temp;

ALTER TABLE bsframework.tSessionData MODIFY cData MEDIUMTEXT;
UPDATE tVersion SET cVersion='1.2.27', cUpdated=NOW() WHERE cKey = 'DBT';

