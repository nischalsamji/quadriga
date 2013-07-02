package edu.asu.spring.quadriga.db.workbench;

import java.util.List;

import javax.sql.DataSource;

import edu.asu.spring.quadriga.domain.IProject;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;

public interface IDBConnectionRetrieveProjectManager {

	public abstract IProject getProjectDetails(String projectId)
			throws QuadrigaStorageException;

	public abstract List<IProject> getProjectList(String sUserName)
			throws QuadrigaStorageException;

	public abstract void setDataSource(DataSource dataSource);
}