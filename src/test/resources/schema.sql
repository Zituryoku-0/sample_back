CREATE TABLE userInfo (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	userId char(32) NOT NULL,
	userName char(64) NOT NULL,
	userPassword char(64) NOT NULL,
	latest_access_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);