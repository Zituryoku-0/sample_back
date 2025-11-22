-- 作成したDBに接続
\c mydatabase;
-- テーブル作成
DROP TABLE IF EXISTS shopInfo;
CREATE TABLE shopInfo (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	shopId char(64) NOT NULL,
	shopName char(64) NOT NULL,
	shopPrice int NOT NULL,
	shopStock int,
);
-- サンプルデータの登録
INSERT INTO shopInfo (shopId, shopName, shopPrice, shopStock) VALUES('s001', 'ティッシュ（5箱）', 500, 100);
INSERT INTO shopInfo (shopId, shopName, shopPrice, shopStock) VALUES('s002', 'トイレットペーパー（12ロール）', 1200, 100);
INSERT INTO shopInfo (shopId, shopName, shopPrice, shopStock) VALUES('s003', 'ウェットティッシュ(100枚)', 370, 100);
INSERT INTO shopInfo (shopId, shopName, shopPrice, shopStock) VALUES('s004', '柔軟剤(1900mL)', 1300, 100);
INSERT INTO shopInfo (shopId, shopName, shopPrice, shopStock) VALUES('s005', 'サランラップ(3本パック)', 1300, 100);
INSERT INTO shopInfo (shopId, shopName, shopPrice, shopStock) VALUES('s006', 'ボディソープ(450mL)', 950, 100);

