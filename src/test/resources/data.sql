INSERT INTO userInfo (userId, userName, userPassword) VALUES('sampleUserId1', 'sample UserName1', 'abcdef');
INSERT INTO userInfo (userId, userName, userPassword) VALUES('ExistsByUserId1', 'ExistsByUserName1', 'ExistsByPassword');
INSERT INTO userInfo (userId, userName, userPassword) VALUES('NotLoginUserId', 'NotLoginUserName', 'NotLoginUserPassword');

-- NotLoginUserの削除フラグを立てる
UPDATE userInfo SET delete_flg = TRUE WHERE userId = 'NotLoginUserId';
