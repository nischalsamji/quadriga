use quadriga;

INSERT tbl_project_collaborator(projectid,collaboratoruser,collaboratorrole,updatedby,updateddate,createdby,createddate)
VALUES ('1','test','collaborator_role1,collaborator_role4',SUBSTRING_INDEX(USER(),'@',1),CURDATE(),SUBSTRING_INDEX(USER(),'@',1),CURDATE());

INSERT tbl_project_collaborator(projectid,collaboratoruser,collaboratorrole,updatedby,updateddate,createdby,createddate)
VALUES ('1','jdoe','collaborator_role1',SUBSTRING_INDEX(USER(),'@',1),CURDATE(),SUBSTRING_INDEX(USER(),'@',1),CURDATE());

INSERT tbl_project_collaborator(projectid,collaboratoruser,collaboratorrole,updatedby,updateddate,createdby,createddate)
VALUES ('2','dexter','collaborator_role3',SUBSTRING_INDEX(USER(),'@',1),CURDATE(),SUBSTRING_INDEX(USER(),'@',1),CURDATE());

INSERT tbl_project_collaborator(projectid,collaboratoruser,collaboratorrole,updatedby,updateddate,createdby,createddate)
VALUES ('2','deb','collaborator_role1',SUBSTRING_INDEX(USER(),'@',1),CURDATE(),SUBSTRING_INDEX(USER(),'@',1),CURDATE());

INSERT tbl_project_collaborator(projectid,collaboratoruser,collaboratorrole,updatedby,updateddate,createdby,createddate)
VALUES ('3','leonard','collaborator_role1,collaborator_role4',SUBSTRING_INDEX(USER(),'@',1),CURDATE(),SUBSTRING_INDEX(USER(),'@',1),CURDATE());

INSERT tbl_project_collaborator(projectid,collaboratoruser,collaboratorrole,updatedby,updateddate,createdby,createddate)
VALUES ('3','test','collaborator_role3',SUBSTRING_INDEX(USER(),'@',1),CURDATE(),SUBSTRING_INDEX(USER(),'@',1),CURDATE());

INSERT tbl_project_collaborator(projectid,collaboratoruser,collaboratorrole,updatedby,updateddate,createdby,createddate)
VALUES ('4','jdoe','collaborator_role2',SUBSTRING_INDEX(USER(),'@',1),CURDATE(),SUBSTRING_INDEX(USER(),'@',1),CURDATE());

INSERT tbl_project_collaborator(projectid,collaboratoruser,collaboratorrole,updatedby,updateddate,createdby,createddate)
VALUES ('4','penny','collaborator_role1',SUBSTRING_INDEX(USER(),'@',1),CURDATE(),SUBSTRING_INDEX(USER(),'@',1),CURDATE());