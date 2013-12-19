
ALTER TABLE tbl_dictionary_collaborator
ADD FOREIGN KEY(id) REFERENCES tbl_dictionary(id);

ALTER TABLE tbl_dictionary_collaborator
ADD FOREIGN KEY(collaboratoruser)  REFERENCES tbl_quadriga_user(username);

ALTER TABLE tbl_dictionary_items
ADD FOREIGN KEY(id) REFERENCES tbl_dictionary(id);

ALTER TABLE tbl_dictionary
ADD FOREIGN KEY(dictionaryowner) REFERENCES tbl_quadriga_user(username);

ALTER TABLE tbl_quadriga_user_denied
ADD FOREIGN KEY(deniedby) REFERENCES tbl_quadriga_user(username);

ALTER TABLE tbl_workspace_collaborator
ADD FOREIGN KEY(username) REFERENCES tbl_quadriga_user(username);

ALTER TABLE tbl_workspace_collaborator
ADD FOREIGN KEY(workspaceid) REFERENCES tbl_workspace(workspaceid);

ALTER TABLE tbl_workspace
ADD FOREIGN KEY(workspaceowner)  REFERENCES tbl_quadriga_user(username); 

/* Begin - Foreign key dependencies for Dspace data */
ALTER TABLE tbl_workspace_dspace
ADD FOREIGN KEY(workspaceid) REFERENCES tbl_workspace(workspaceid);

ALTER TABLE tbl_dspace_keys
ADD FOREIGN KEY(username) REFERENCES tbl_quadriga_user(username);
/* End - Foreign key dependencies for Dspace data */


/***************************************************************
 * Foreign key dependencies
 */
ALTER TABLE tbl_project
  ADD CONSTRAINT fk_tbl_project_projectowner   FOREIGN KEY(projectowner)
     REFERENCES tbl_quadriga_user(username);
     
ALTER TABLE tbl_project_workspace
 ADD CONSTRAINT fk_project_workspace_projectid FOREIGN KEY(projectid)
  REFERENCES tbl_project(projectid);

ALTER TABLE tbl_project_workspace
 ADD CONSTRAINT fk_project_workspace_workspaceid FOREIGN KEY(workspaceid)
 REFERENCES tbl_workspace(workspaceid);
 
ALTER TABLE tbl_project_editor
 ADD CONSTRAINT fk_project_editor_projectid FOREIGN KEY(projectid)
 REFERENCES tbl_project(projectid);

ALTER TABLE tbl_project_editor
 ADD CONSTRAINT fk_project_editor_editor FOREIGN KEY(editor)
 REFERENCES tbl_quadriga_user(username);
 
ALTER TABLE tbl_project_dictionary
 ADD CONSTRAINT fk_project_dictionary_projectid FOREIGN KEY(projectid)
 REFERENCES tbl_project(projectid);

ALTER TABLE tbl_project_dictionary
 ADD CONSTRAINT fk_project_dictionary_dictionaryid FOREIGN KEY(dictionaryid)
 REFERENCES tbl_dictionary(dictionaryid);
 
ALTER TABLE tbl_project_conceptcollection
 ADD CONSTRAINT fk_project_cc_projectid FOREIGN KEY(projectid)
 REFERENCES tbl_project(projectid);

ALTER TABLE tbl_project_conceptcollection
 ADD CONSTRAINT fk_project_cc_conceptcollectionid FOREIGN KEY(conceptcollectionid)
 REFERENCES tbl_conceptcollection(conceptcollectionid);
 
ALTER TABLE tbl_project_collaborator
 ADD CONSTRAINT fk_project_collaborator_projectid FOREIGN KEY(projectid)
  REFERENCES tbl_project(projectid);
  
ALTER TABLE tbl_conceptcollection
 ADD CONSTRAINT fk_tbl_conceptcollection_owner FOREIGN KEY(collectionowner)
 REFERENCES tbl_quadriga_user(username);
 
ALTER TABLE tbl_conceptcollection_collaborator
 ADD CONSTRAINT fk_cc_collaborator_conceptcollectionid FOREIGN KEY(conceptcollectionid)
 REFERENCES tbl_conceptcollection(conceptcollectionid);

ALTER TABLE tbl_conceptcollection_collaborator
 ADD CONSTRAINT fk_cc_collaborator_collaboratoruser FOREIGN KEY(collaboratoruser)
 REFERENCES tbl_quadriga_user(username);
  
ALTER TABLE tbl_conceptcollection_items
 ADD CONSTRAINT fk_cc_items FOREIGN KEY(conceptcollectionid)
 REFERENCES tbl_conceptcollection(conceptcollectionid);
 
