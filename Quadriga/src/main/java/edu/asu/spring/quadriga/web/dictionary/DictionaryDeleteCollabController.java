package edu.asu.spring.quadriga.web.dictionary;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import edu.asu.spring.quadriga.domain.dictionary.IDictionary;
import edu.asu.spring.quadriga.domain.factories.ICollaboratorFactory;
import edu.asu.spring.quadriga.domain.factories.IUserFactory;
import edu.asu.spring.quadriga.domain.factories.impl.ModifyCollaboratorFormFactory;
import edu.asu.spring.quadriga.exceptions.QuadrigaAccessException;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.service.IQuadrigaRoleManager;
import edu.asu.spring.quadriga.service.dictionary.IDictionaryCollaboratorManager;
import edu.asu.spring.quadriga.service.dictionary.IDictionaryManager;
import edu.asu.spring.quadriga.validator.CollaboratorFormDeleteValidator;
import edu.asu.spring.quadriga.web.login.RoleNames;
import edu.asu.spring.quadriga.web.workbench.backing.ModifyCollaborator;
import edu.asu.spring.quadriga.web.workbench.backing.ModifyCollaboratorForm;
import edu.asu.spring.quadriga.web.workbench.backing.ModifyCollaboratorFormManager;

@Controller
public class DictionaryDeleteCollabController {


	@Autowired
	private ModifyCollaboratorFormManager collaboratorFormManager;

	@Autowired
	private IDictionaryManager dictionaryManager;
	
	@Autowired
	private IDictionaryCollaboratorManager dictCollaboratorManager;

	@Autowired
	private CollaboratorFormDeleteValidator validator;

	@Autowired
	ICollaboratorFactory  collaboratorFactory;

	@Autowired
	IUserFactory userFactory;

	@Autowired
	ModifyCollaboratorFormFactory collaboratorFormFactory;

	@Autowired
	IQuadrigaRoleManager collaboratorRoleManager;


	@InitBinder
	protected void initBinder(WebDataBinder validateBinder) throws Exception {
		validateBinder.setValidator(validator);
	}

	/**
	 * This method deletes the selected collaborators associated with the given workspace
	 * @param dictionaryId
	 * @param collaboratorForm
	 * @param result
	 * @param model
	 * @param principal
	 * @return ModelAndView
	 * @throws QuadrigaStorageException
	 * @throws QuadrigaAccessException
	 */
	@AccessPolicies({ @ElementAccessPolicy(type = CheckedElementType.DICTIONARY,paramIndex = 1, userRole = {RoleNames.ROLE_DICTIONARY_COLLABORATOR_ADMIN} )})
	@RequestMapping(value="auth/dictionaries/{dictionaryid}/deleteCollaborators", method = RequestMethod.POST)
	public ModelAndView deleteCollaborators(@PathVariable("dictionaryid") String dictionaryId, 
			@Validated @ModelAttribute("collaboratorForm") ModifyCollaboratorForm collaboratorForm, BindingResult result,
			ModelMap model, Principal principal) throws QuadrigaStorageException,QuadrigaAccessException
			{

		ModelAndView modelAndView ;
		List<ModifyCollaborator>collaborators;
		String userName;

		modelAndView = new ModelAndView("auth/dictionaries/showDeleteCollaborators");

		if(result.hasErrors())
		{
			//fetch the dictionary details
			IDictionary dictionary = dictionaryManager.getDictionaryDetails(dictionaryId);
			collaborators = collaboratorFormManager.modifyDictCollaboratorManager(dictionaryId);
			collaboratorForm.setCollaborators(collaborators);

			modelAndView.getModelMap().put("collaboratorForm", collaboratorForm);
			modelAndView.getModelMap().put("success", 0);
			modelAndView.getModelMap().put("error", 1);
			modelAndView.getModelMap().put("dictionaryid", dictionaryId);
			modelAndView.getModelMap().put("dictionaryname", dictionary.getDictionaryName());
			modelAndView.getModelMap().put("dictionarydesc", dictionary.getDescription());
		} else {
			collaborators = collaboratorForm.getCollaborators();
			for(ModifyCollaborator collaborator: collaborators)
			{
				userName = collaborator.getUserName();
				if(userName!=null) {
				    dictCollaboratorManager.deleteCollaborators(userName, dictionaryId);
				}
			}
			modelAndView.getModelMap().put("success", 1);
			modelAndView.getModelMap().put("dictionaryd", dictionaryId);
		}

		return modelAndView;
	}

	/**
	 * This method retrieves the collaborators associated with given workspace
	 * @param dictionaryId
	 * @param principal
	 * @return ModelAndView
	 * @throws QuadrigaStorageException
	 * @throws QuadrigaAccessException
	 */
	@AccessPolicies({ @ElementAccessPolicy(type = CheckedElementType.DICTIONARY,paramIndex = 1, userRole = {RoleNames.ROLE_DICTIONARY_COLLABORATOR_ADMIN} )})
	@RequestMapping(value="auth/dictionaries/{dictionaryid}/showDeleteCollaborators" , method = RequestMethod.GET)
	public ModelAndView displayCollaborators(@PathVariable("dictionaryid") String dictionaryId, Principal principal) throws QuadrigaStorageException
	,QuadrigaAccessException
	{

		ModelAndView modelAndView;
		ModifyCollaboratorForm collaboratorForm;

		//fetch the dictionary details
		IDictionary dictionary = dictionaryManager.getDictionaryDetails(dictionaryId);


		modelAndView = new ModelAndView("auth/dictionaries/showDeleteCollaborators");

		collaboratorForm = collaboratorFormFactory.createCollaboratorFormObject();

		List<ModifyCollaborator> modifyCollaborator = collaboratorFormManager.modifyDictCollaboratorManager(dictionaryId);


		collaboratorForm.setCollaborators(modifyCollaborator);

		modelAndView.getModelMap().put("collaboratorForm", collaboratorForm);
		modelAndView.getModelMap().put("dictionaryid", dictionaryId);
		modelAndView.getModelMap().put("dictionaryname", dictionary.getDictionaryName());
		modelAndView.getModelMap().put("dictionarydesc", dictionary.getDescription());

		modelAndView.getModelMap().put("success", 0);

		return modelAndView;
	}
}
