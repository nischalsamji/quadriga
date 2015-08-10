package edu.asu.spring.quadriga.service.impl.workspace;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.spring.quadriga.dao.workbench.IProjectDAO;
import edu.asu.spring.quadriga.dao.workspace.IWorkspaceDAO;
import edu.asu.spring.quadriga.dao.workspace.IWorkspaceEditorDAO;
import edu.asu.spring.quadriga.domain.workspace.IWorkSpace;
import edu.asu.spring.quadriga.dto.ProjectDTO;
import edu.asu.spring.quadriga.dto.ProjectWorkspaceDTO;
import edu.asu.spring.quadriga.dto.WorkspaceCollaboratorDTO;
import edu.asu.spring.quadriga.dto.WorkspaceDTO;
import edu.asu.spring.quadriga.dto.WorkspaceEditorDTO;
import edu.asu.spring.quadriga.email.IEmailNotificationManager;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.mapper.ProjectDTOMapper;
import edu.asu.spring.quadriga.mapper.WorkspaceCollaboratorDTOMapper;
import edu.asu.spring.quadriga.mapper.WorkspaceDTOMapper;
import edu.asu.spring.quadriga.service.workspace.IModifyWSManager;

/**
 * Class implements {@link IModifyWSManager} to
 * add/update/delete workspace associated with project.
 * @implements IListWSManager
 * @author Kiran Kumar Batna
 */
@Service
@Transactional(rollbackFor={Exception.class})
public class ModifyWSManager implements IModifyWSManager 
{
	@Autowired
	private IEmailNotificationManager emailManager;

	@Autowired
	private IWorkspaceDAO workspaceDao;
	
	@Autowired
	private IProjectDAO projectDao;
	
	@Autowired
	private IWorkspaceEditorDAO workspaceEditorDao;
	
	@Autowired
    private WorkspaceDTOMapper workspaceDTOMapper;
	
	@Autowired
	private WorkspaceCollaboratorDTOMapper collaboratorMapper;

	@Autowired
    private ProjectDTOMapper projectMapper;

	
	/**
	 * This inserts a workspace for a project into database.
	 * @param     workspace
	 * @param     projectId
	 * @return    String errmsg - blank on success and error message on failure
	 * @throws    QuadrigaStorageException
	 * @author    Julia Damerow, kiranbatna
	 */
	@Override
	public void addWorkspaceToProject(IWorkSpace workspace, String projectId) {
	    ProjectDTO projectDto = projectDao.getProjectDTO(projectId);
	    WorkspaceDTO workspaceDTO = workspaceDTOMapper.getWorkspaceDTO(workspace);
        workspaceDTO.setWorkspaceid(workspaceDao.generateUniqueID());

        ProjectWorkspaceDTO projectWorkspaceDTO = projectMapper.getProjectWorkspace(projectDto, workspaceDTO);
        workspaceDTO.setProjectWorkspaceDTO(projectWorkspaceDTO);
        workspaceDao.saveNewDTO(workspaceDTO);
        projectDao.updateDTO(projectDto);
	}

	/**
	 * This method deletes the requested workspace.
	 * @param   workspaceIdList - comma separated list of workspace ids to delete
	 * @return  boolean - return true if delete was successful, otherwise false
	 * @author  Julia Damerow, kiranbatna
	 */
	@Override
	public boolean deleteWorkspace(String workspaceIdList) {
		List<String> wsIds = Arrays.asList(workspaceIdList.split(","));
		boolean success = true;
		for (String wsId : wsIds) {
		  success = success && workspaceDao.deleteWorkspace(wsId);
		}
		return success;
	}

	/**
	 * This method updates the workspace
	 * @param workspace
	 * @return String - errmsg blank on success and error message on failure
	 * @throws QuadrigaStorageException
	 * @author Julia Damerow, kiranbatna
	 */
	@Override
	@Transactional
	public void updateWorkspace(IWorkSpace workspace) throws QuadrigaStorageException 
	{
	    WorkspaceDTO workspaceDTO = workspaceDao.getWorkspaceDTO(workspace.getWorkspaceId());
	    workspaceDTO.setWorkspacename(workspace.getWorkspaceName());
        workspaceDTO.setDescription(workspace.getDescription());
        workspaceDTO.setUpdateddate(new Date());
        workspaceDTO.setUpdatedby(workspace.getOwner().getName());
	    
		workspaceDao.updateDTO(workspaceDTO);
	}

	/**
	 * This method assigns a new owner to the workspace
	 * @param projectId
	 * @param oldOwner
	 * @param newOwner
	 * @param collabRole
	 * @throws QuadrigaStorageException
	 * @author kiranbatna, Julia Damerow
	 */
	@Override
	@Transactional
	public void transferWSOwnerRequest(String workspaceId,String oldOwner,String newOwner,String collabRole) {
	    WorkspaceDTO workspaceDTO = workspaceDao.getWorkspaceDTO(workspaceId);
        //set the new workspace owner
        workspaceDTO.setWorkspaceowner(workspaceDao.getUserDTO(newOwner));
        workspaceDTO.setUpdatedby(oldOwner);
        workspaceDTO.setUpdateddate(new Date());


        //delete new owner from collaborators list
        Iterator<WorkspaceCollaboratorDTO> workspaceCollaboratorIt = workspaceDTO.getWorkspaceCollaboratorDTOList().iterator();
        WorkspaceCollaboratorDTO collaborator = null;
        while(workspaceCollaboratorIt.hasNext()) {
            collaborator = workspaceCollaboratorIt.next();
            if(collaborator.getQuadrigaUserDTO().getUsername().equals(newOwner)) {
                workspaceCollaboratorIt.remove();
                break;
            }
        }

        //add the current owner as a collaborator
        collaborator = collaboratorMapper.getWorkspaceCollaboratorDTO(workspaceDTO, oldOwner, collabRole);
        workspaceDTO.getWorkspaceCollaboratorDTOList().add(collaborator);

        workspaceDao.updateDTO(workspaceDTO);
	}

	/**
	 * Method to assign the editor role to a user.
	 * @param workspaceId
	 * @param userName
	 * @return
	 * @throws QuadrigaStorageException
	 */
	@Override
	@Transactional
	public void assignEditorRole(String workspaceId,String userName) throws QuadrigaStorageException{
		WorkspaceDTO workspaceDto = workspaceDao.getWorkspaceDTO(workspaceId);
		WorkspaceEditorDTO workspaceEditorDTO = workspaceDTOMapper.getWorkspaceEditor(workspaceDto, userName);
		workspaceEditorDao.saveNewDTO(workspaceEditorDTO);
	}

	/**
	 * Manager for Assigning editor roles to owner for workspace level
	 * @param workspaceId
	 * @param userName username of the user that should be deleted as editor
	 * @return true if editor role was removed, otherwise false
	 * @throws QuadrigaStorageException
	 */
	@Override
	@Transactional
	public boolean deleteEditorRole(String workspaceId,String userName) {
	    WorkspaceEditorDTO workspaceEditorDTO = workspaceEditorDao.getWorkspaceEditorDTO(workspaceId, userName);
        if(workspaceEditorDTO != null) {
            workspaceEditorDao.deleteWorkspaceEditorDTO(workspaceEditorDTO);
            return true;
        } else {
            return false;
        }
	}

}
