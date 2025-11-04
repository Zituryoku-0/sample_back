-- DB作成
CREATE DATABASE mydatabase;
-- 作成したDBに接続
\c mydatabase;
-- テーブル作成
DROP TABLE IF EXISTS userInfo;
CREATE TABLE userInfo (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	userId char(32) NOT NULL,
	userName char(64) NOT NULL,
	userPassword char(64) NOT NULL,
	latest_access_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- サンブルデータの登録
INSERT INTO userInfo (userId, userName, userPassword) VALUES('sampleUserId1', 'sample UserName1', 'abcdef');
