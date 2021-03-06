/*******************************************
Name          : tbl_workspace

Description   : Stores the workspace details.

Called By     : 

Create By     : Kiran Kumar Batna

Modified Date : 05/30/2013

********************************************/
CREATE TABLE IF NOT EXISTS tbl_workspace
(
  workspacename   VARCHAR(50)   NOT NULL,
  description     TEXT          NULL,
  workspaceid     VARCHAR(150)  NOT NULL,
  workspaceowner  VARCHAR(50)   NOT NULL,
  isarchived      TINYINT       NOT NULL  DEFAULT 0,
  isdeactivated   TINYINT       NOT NULL  DEFAULT 0,
  updatedby       VARCHAR(20)   NOT NULL,
  updateddate     TIMESTAMP     NOT NULL,
  createdby       VARCHAR(20)   NOT NULL,
  createddate     DATETIME      NOT NULL,
  PRIMARY KEY(workspaceid)
)