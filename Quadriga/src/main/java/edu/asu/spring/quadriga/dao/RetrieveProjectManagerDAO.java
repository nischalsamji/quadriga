package edu.asu.spring.quadriga.dao;

import java.util.List;

import edu.asu.spring.quadriga.domain.IProject;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;

public interface RetrieveProjectManagerDAO {

	public abstract IProject getProjectDetails(String projectId)
			throws QuadrigaStorageException;

	List<IProject> getProjectList(String sUserName)
			throws QuadrigaStorageException;

	List<IProject> getCollaboratorProjectList(String sUserName)
			throws QuadrigaStorageException;

	List<IProject> getProjectListAsWorkspaceOwner(String sUserName)
			throws QuadrigaStorageException;

}