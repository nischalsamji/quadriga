/*******************************************
Name          : tbl_workspace_collaborator

Description   : Stores the workspace collaborator details.

Called By     : 

Create By     : Kiran Kumar Batna

Modified Date : 05/30/2013

********************************************/
CREATE TABLE IF NOT EXISTS tbl_workspace_collaborator
(
  workspaceid        VARCHAR(150)  NOT NULL ,
  collaboratoruser   VARCHAR(50)   NOT NULL,
  collaboratorrole   VARCHAR(50)   NOT NULL,
  updatedby          VARCHAR(20)   NOT NULL,
  updateddate        TIMESTAMP     NOT NULL,
  createdby          VARCHAR(20)   NOT NULL,
  createddate        DATETIME      NOT NULL,
  PRIMARY KEY(workspaceid,collaboratoruser,collaboratorrole) 
)