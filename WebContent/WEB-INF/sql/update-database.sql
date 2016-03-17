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

ALTER TABLE bsframework.tSession
ADD INDEX Index_Token (cToken ASC);
