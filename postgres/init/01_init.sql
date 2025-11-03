-- DB作成
CREATE DATABASE mydatabase;
-- 作成したDBに接続
\c mydatabase;
-- テーブル作成
DROP TABLE IF EXISTS sample;
CREATE TABLE sample (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	userId char(32) NOT NULL,
	userName char(64) NOT NULL,
	latest_access_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- サンブルデータの登録
INSERT INTO sample (userId, userName) VALUES('sampleUserId1', 'sample UserName1');
