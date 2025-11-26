-- 作成したDBに接続
\c mydatabase;
-- テーブル作成
DROP TABLE IF EXISTS shopInfo;
CREATE TABLE shopInfo (
	shopId varchar(64) NOT NULL PRIMARY KEY,
	shopName varchar(64) NOT NULL,
	shopAddress varchar(64) NOT NULL
);
-- サンプルデータの登録
INSERT INTO shopInfo (shopId, shopName, shopAddress) VALUES('shopId1', '丸スーパー', '〇県');
INSERT INTO shopInfo (shopId, shopName, shopAddress) VALUES('shopId2', '三角スーパー', '△県');
INSERT INTO shopInfo (shopId, shopName, shopAddress) VALUES('shopId3', '四角スーパー', '□県');