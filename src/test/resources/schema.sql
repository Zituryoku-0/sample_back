CREATE TABLE userInfo (
	userId char(32) NOT NULL PRIMARY KEY ,
	userName char(64) NOT NULL,
	userPassword char(64) NOT NULL,
	latest_access_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);