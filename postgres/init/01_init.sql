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
	latest_access_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	delete_flg char(1) NOT NULL DEFAULT '0'
);
-- サンプルデータの登録
INSERT INTO userInfo (userId, userName, userPassword) VALUES('sampleUserId1', 'sample UserName1', 'abcdefgh');
INSERT INTO userInfo (userId, userName, userPassword) VALUES('sampleUserId2', 'sample UserName2', 'ijklmnop');

-- サンプルデータのアカウント無効化用
update userInfo set latest_access_time = '2000-01-01 00:00:00 ' where userId = 'sampleUserId2';