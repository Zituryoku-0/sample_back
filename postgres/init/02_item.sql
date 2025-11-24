-- 作成したDBに接続
\c mydatabase;
-- テーブル作成
DROP TABLE IF EXISTS itemInfo;
CREATE TABLE itemInfo (
	itemId varchar(64) NOT NULL PRIMARY KEY,
	itemName varchar(64) NOT NULL,
	itemPrice int NOT NULL
);
-- サンプルデータの登録
INSERT INTO itemInfo (itemId, itemName, itemPrice) VALUES('itemId1', 'ティッシュ（5箱）', 500);
INSERT INTO itemInfo (itemId, itemName, itemPrice) VALUES('itemId2', 'トイレットペーパー（12ロール）', 1200);
INSERT INTO itemInfo (itemId, itemName, itemPrice) VALUES('itemId3', 'ウェットティッシュ(100枚)', 370);
INSERT INTO itemInfo (itemId, itemName, itemPrice) VALUES('itemId4', '柔軟剤(1900mL)', 1300);
