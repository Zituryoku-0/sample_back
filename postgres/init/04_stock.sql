-- 作成したDBに接続
\c mydatabase;
-- テーブル作成
DROP TABLE IF EXISTS stockInfo;
CREATE TABLE stockInfo (
	shopId varchar(64) NOT NULL,
	itemId varchar(64) NOT NULL,
	stock int,

    PRIMARY KEY (shopId, itemId),
	FOREIGN KEY (shopId) REFERENCES shopInfo(shopId),
	FOREIGN KEY (itemId) REFERENCES itemInfo(itemId)
);
-- サンプルデータの登録
INSERT INTO stockInfo (shopId, itemId, stock) VALUES('shopId1', 'itemId1', 100);
INSERT INTO stockInfo (shopId, itemId, stock) VALUES('shopId1', 'itemId2', 100);
INSERT INTO stockInfo (shopId, itemId, stock) VALUES('shopId2', 'itemId3', 100);
INSERT INTO stockInfo (shopId, itemId, stock) VALUES('shopId2', 'itemId4', 100);
INSERT INTO stockInfo (shopId, itemId, stock) VALUES('shopId3', 'itemId1', 100);
INSERT INTO stockInfo (shopId, itemId, stock) VALUES('shopId3', 'itemId3', 100);
INSERT INTO stockInfo (shopId, itemId, stock) VALUES('shopId3', 'itemId4', 100);