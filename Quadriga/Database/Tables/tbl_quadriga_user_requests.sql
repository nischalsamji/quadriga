/*******************************************
Name          : tbl_quadriga_user_requests

Description   : Store the details of users requests.

Called By     : DBConnectionManager.java

Create By     : Ram Kumar Kumaresan

Modified Date : 05/24/2013

********************************************/
CREATE TABLE IF NOT EXISTS tbl_quadriga_user_requests
(

  fullname      VARCHAR(50)  DEFAULT NULL,
  username      VARCHAR(50)  NOT NULL,
  passwd        VARCHAR(20)  DEFAULT NULL,
  email         VARCHAR(50)  DEFAULT NULL,
  updatedby     VARCHAR(20)  NOT NULL,
  updateddate   TIMESTAMP    NOT NULL,
  createdby     VARCHAR(20)  NOT NULL,
  createddate   DATETIME     NOT NULL,
  PRIMARY KEY(username)
)