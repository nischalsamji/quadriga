package edu.asu.spring.quadriga.web.workbench;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.spring.quadriga.aspects.annotations.AccessPolicies;
import edu.asu.spring.quadriga.aspects.annotations.CheckedElementType;
import edu.asu.spring.quadriga.aspects.annotations.ElementAccessPolicy;
import edu.asu.spring.quadriga.domain.IUser;
import edu.asu.spring.quadriga.domain.factories.IUserFactory;
import edu.asu.spring.quadriga.domain.impl.User;
import edu.asu.spring.quadriga.domain.workbench.IProject;
import edu.asu.spring.quadriga.domain.workbench.IProjectCollaborator;
import edu.asu.spring.quadriga.exceptions.QuadrigaAccessException;
import edu.asu.spring.quadriga.exceptions.QuadrigaException;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.service.IQuadrigaRoleManager;
import edu.asu.spring.quadriga.service.workbench.IModifyProjectManager;
import edu.asu.spring.quadriga.service.workbench.IProjectCollaboratorManager;
import edu.asu.spring.quadriga.service.workbench.IRetrieveProjectManager;
import edu.asu.spring.quadriga.validator.UserValidator;
import edu.asu.spring.quadriga.web.login.RoleNames;

@Controller
public class TransferProjectOwnerController 
{
	@Autowired
	private IModifyProjectManager projectManager;

	@Autowired
	private IRetrieveProjectManager retrieveProjectManager;
	
	@Autowired
	private IProjectCollaboratorManager collabManager;

	@Autowired
	private UserValidator validator;

	@Autowired
	IUserFactory userFactory;

	@Autowired
	private IQuadrigaRoleManager roleManager;

	@InitBinder
	protected void initBinder(WebDataBinder validateBinder){
		validateBinder.setValidator(validator);
	}

	/**
	 * This method is used to load the project ownership transfer form
	 * @param projectid
	 * @param principal
	 * @return ModelAndView object
	 * @throws QuadrigaStorageException
	 * @throws QuadrigaAccessException
	 * @author kiranbatna
	 */
	@AccessPolicies({ @ElementAccessPolicy(type = CheckedElementType.PROJECT,paramIndex = 1,userRole = {})})
	@RequestMapping(value = "auth/workbench/transferprojectowner/{projectid}", method = RequestMethod.GET)
	public ModelAndView transferProjectOwnerRequestForm(@PathVariable("projectid") String projectid) throws QuadrigaStorageException, QuadrigaAccessException
	{
		ModelAndView model;
		IProject project;
		List<IProjectCollaborator> projectcollaborators = new ArrayList<IProjectCollaborator>();
		List<IUser> userList = null;

		//create a view
		model = new ModelAndView("auth/workbench/transferprojectowner");

		//retrieve the project details
		project = retrieveProjectManager.getProjectDetails(projectid);


		//create a model
		model.getModelMap().put("user", userFactory.createUserObject());
		model.getModelMap().put("projectname", project.getProjectName());
		model.getModelMap().put("projectowner", project.getOwner().getUserName());
		model.getModelMap().put("projectid", projectid);

		//fetch the collaborators
		projectcollaborators = project.getProjectCollaborators();
		if(projectcollaborators != null)
		{
			if(projectcollaborators!=null){
				
				userList = new ArrayList<IUser>();
				for(IProjectCollaborator projectCollaborator : projectcollaborators)
				{
					userList.add(projectCollaborator.getCollaborator().getUserObj());
				}
			}

			
			
			model.getModelMap().put("collaboratinguser", userList);
		}

		//create model attribute
		model.getModelMap().put("success", 0);
		return model;
	}

	/**
	 * This method submits the transfer request form
	 * @param projectid
	 * @param principal
	 * @param collaborator
	 * @param result
	 * @return ModelAndView object
	 * @throws QuadrigaStorageException
	 * @throws QuadrigaAccessException
	 * @throws QuadrigaException 
	 */
	@AccessPolicies({ @ElementAccessPolicy(type = CheckedElementType.PROJECT,paramIndex = 1, userRole = {} )})
	@RequestMapping(value = "auth/workbench/transferprojectowner/{projectid}", method = RequestMethod.POST)
	public ModelAndView transferProjectOwnerRequest(@PathVariable("projectid") String projectid,Principal principal,
			@Validated @ModelAttribute("user")User collaboratorUser,BindingResult result) throws QuadrigaStorageException, QuadrigaAccessException, QuadrigaException
			{
		ModelAndView model;
		String userName;
		String newOwner;
		String collaboratorRole;
		IProject project;
		List<IProjectCollaborator> projectCollaborators = new ArrayList<IProjectCollaborator>();
		List<IUser> userList = new ArrayList<IUser>();

		//create a view
		model = new ModelAndView("auth/workbench/transferprojectowner");
		userName = principal.getName();

		//retrieve the project details
		project = retrieveProjectManager.getProjectDetails(projectid);

		model.getModelMap().put("projectid", projectid);

		if(result.hasErrors())
		{
			model.getModelMap().put("user", collaboratorUser);

			//create a model
			model.getModelMap().put("projectname", project.getProjectName());
			model.getModelMap().put("projectowner", project.getOwner().getUserName());

			//fetch the collaborators
			projectCollaborators = project.getProjectCollaborators();

			for(IProjectCollaborator projectCollaborator : projectCollaborators)
			{
				userList.add(projectCollaborator.getCollaborator().getUserObj());
			}

			model.getModelMap().put("collaboratinguser", userList);

			model.getModelMap().put("success", 0);
		}
		else
		{
			//fetch the new owner
			newOwner = collaboratorUser.getUserName();

			collaboratorRole = roleManager.getQuadrigaRoleById(IQuadrigaRoleManager.PROJECT_ROLES, RoleNames.ROLE_PROJ_COLLABORATOR_ADMIN).getDBid();

			//call the method to transfer the ownership
			collabManager.transferOwnership(projectid, userName, newOwner,collaboratorRole);
			
			model.getModelMap().put("success", 1);
			model.getModelMap().put("user", userFactory.createUserObject());
		}
		return model;
			}

}
