package edu.asu.spring.quadriga.mapper;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.spring.quadriga.domain.enums.EProjectAccessibility;
import edu.asu.spring.quadriga.domain.impl.workbench.Project;
import edu.asu.spring.quadriga.domain.workbench.IProject;
import edu.asu.spring.quadriga.dto.ConceptCollectionDTO;
import edu.asu.spring.quadriga.dto.DictionaryDTO;
import edu.asu.spring.quadriga.dto.ProjectConceptCollectionDTO;
import edu.asu.spring.quadriga.dto.ProjectConceptCollectionDTOPK;
import edu.asu.spring.quadriga.dto.ProjectDTO;
import edu.asu.spring.quadriga.dto.ProjectDictionaryDTO;
import edu.asu.spring.quadriga.dto.ProjectDictionaryDTOPK;
import edu.asu.spring.quadriga.dto.ProjectEditorDTO;
import edu.asu.spring.quadriga.dto.ProjectEditorDTOPK;
import edu.asu.spring.quadriga.dto.ProjectWorkspaceDTO;
import edu.asu.spring.quadriga.dto.ProjectWorkspaceDTOPK;
import edu.asu.spring.quadriga.dto.QuadrigaUserDTO;
import edu.asu.spring.quadriga.dto.WorkspaceDTO;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.service.IUserManager;

@Service
public class ProjectDTOMapper extends BaseMapper {

	@Autowired
    private IUserManager userManager;
	
	public IProject getProject(ProjectDTO projectDTO)  throws QuadrigaStorageException {
		IProject project = new Project();
		project.setProjectName(projectDTO.getProjectname());
		project.setDescription(projectDTO.getDescription());
		project.setUnixName(projectDTO.getUnixname());
		project.setProjectId(projectDTO.getProjectid());
		project.setOwner(userManager.getUser(projectDTO.getProjectowner().getUsername()));
		project.setProjectAccess(EProjectAccessibility.valueOf(projectDTO.getAccessibility()));
		return project;
	}
	
	public ProjectDTO getProjectDTO(IProject project, String userName) {
		QuadrigaUserDTO quadrigaUser = getUserDTO(userName);
		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setProjectname(project.getProjectName());
		projectDTO.setDescription(project.getDescription());
		projectDTO.setUnixname(project.getUnixName());
		projectDTO.setProjectid(project.getProjectId());
		projectDTO.setProjectowner(quadrigaUser);
		projectDTO.setCreatedby(project.getOwner().getUserName());
		projectDTO.setCreateddate(new Date());
		projectDTO.setUpdatedby(project.getOwner().getUserName());
		projectDTO.setUpdateddate(new Date());
		if(project.getProjectAccess() != null) {
			projectDTO.setAccessibility(project.getProjectAccess().name());
		}
		return projectDTO;
	}
	
	public ProjectEditorDTO getProjectEditor(ProjectDTO project,String userName) throws QuadrigaStorageException
	{
		ProjectEditorDTO projectEditor = null;
		ProjectEditorDTOPK projectEditorKey = null;
		
		String projectId = project.getProjectid();
		Date date = new Date();
		projectEditor = new ProjectEditorDTO();
		projectEditorKey = new ProjectEditorDTOPK(projectId,userName);
		projectEditor.setProjectEditorDTOPK(projectEditorKey);
		projectEditor.setProject(project);
		projectEditor.setQuadrigaUserDTO(getUserDTO(userName));
		projectEditor.setUpdatedby(userName);
		projectEditor.setUpdateddate(date);
		projectEditor.setCreatedby(userName);
		projectEditor.setCreateddate(date);
		return projectEditor;
	}
	
	/**
	 * This method associated the dictionary with the specified project.
	 * @param project
	 * @param dictionary
	 * @param userName
	 * @return ProjectDictionaryDTO object
	 */
	public ProjectDictionaryDTO getProjectDictionary(ProjectDTO project,DictionaryDTO dictionary,String userName)
	{
		ProjectDictionaryDTO projectDictionary = null;
		ProjectDictionaryDTOPK projectDictionaryKey = null;
		Date date = new Date();
		projectDictionary = new ProjectDictionaryDTO();
		projectDictionaryKey = new ProjectDictionaryDTOPK(project.getProjectid(),dictionary.getDictionaryid());
		projectDictionary.setProjectDictionaryDTOPK(projectDictionaryKey);
		projectDictionary.setProject(project);
		projectDictionary.setDictionary(dictionary);
		projectDictionary.setCreatedby(userName);
		projectDictionary.setCreateddate(date);
		projectDictionary.setUpdatedby(userName);
		projectDictionary.setUpdateddate(date);
		return projectDictionary;
	}
	
	/**
	 * This method associates the concept collection to the given project.
	 * @param project
	 * @param conceptCollection
	 * @param userName
	 * @return ProjectConceptCollectionDTO object
	 */
	public ProjectConceptCollectionDTO getProjectConceptCollection(ProjectDTO project,ConceptCollectionDTO conceptCollection,String userName)
	{
		ProjectConceptCollectionDTO projectConceptCollection = null;
		ProjectConceptCollectionDTOPK projectConceptCollectionKey = null;
		Date date = new Date();
		projectConceptCollectionKey = new ProjectConceptCollectionDTOPK(project.getProjectid(),conceptCollection.getConceptCollectionid());
        projectConceptCollection = new ProjectConceptCollectionDTO();
        
        projectConceptCollection.setProjectConceptcollectionDTOPK(projectConceptCollectionKey);
        projectConceptCollection.setProjectDTO(project);
        projectConceptCollection.setConceptCollection(conceptCollection);
        projectConceptCollection.setCreatedby(userName);
        projectConceptCollection.setCreateddate(date);
        projectConceptCollection.setUpdatedby(userName);
        projectConceptCollection.setUpdateddate(date);
        
		return projectConceptCollection;
	}
	
	/**
	 * This method associates the workspace with the given project 
	 * @param project
	 * @param workspace
	 * @return ProjectWorkspaceDTO object
	 */
	public ProjectWorkspaceDTO getProjectWorkspace(ProjectDTO project, WorkspaceDTO workspace)
	{
		ProjectWorkspaceDTO projectWorkspace = null;
		ProjectWorkspaceDTOPK projectWorkspaceKey = null;
		String userName = workspace.getCreatedby();
		Date date = new Date();
		projectWorkspaceKey = new ProjectWorkspaceDTOPK(project.getProjectid(),workspace.getWorkspaceid());
		projectWorkspace = new ProjectWorkspaceDTO();
		projectWorkspace.setProjectWorkspaceDTOPK(projectWorkspaceKey);
		projectWorkspace.setProjectDTO(project);
		projectWorkspace.setWorkspaceDTO(workspace);
		projectWorkspace.setCreatedby(userName);
		projectWorkspace.setCreateddate(date);
		projectWorkspace.setUpdatedby(userName);
		projectWorkspace.setUpdateddate(date);
		
		
		return projectWorkspace;
	}
}
