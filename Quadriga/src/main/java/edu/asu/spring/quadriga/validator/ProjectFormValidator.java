package edu.asu.spring.quadriga.validator;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.spring.quadriga.web.workbench.backing.ModifyProject;
import edu.asu.spring.quadriga.web.workbench.backing.ModifyProjectForm;
@Service
public class ProjectFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return arg0.isAssignableFrom(ModifyProjectForm.class);
	}

	@Override
	public void validate(Object obj, Errors err) 
	{
		List<ModifyProject> deleteProjectList;
		
		ModifyProjectForm projectForm = (ModifyProjectForm)obj;
		deleteProjectList = projectForm.getProjectList();
		
		projectFormValidation(deleteProjectList,err);
	}
	
	public void projectFormValidation(List<ModifyProject> deleteProjectList,Errors err)
	{
		for(int i = 0;i<deleteProjectList.size();i++)
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "projectList["+i+"].internalid", "project_selection.required");
		}
	}
}