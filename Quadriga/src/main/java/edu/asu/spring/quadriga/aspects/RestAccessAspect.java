package edu.asu.spring.quadriga.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import edu.asu.spring.quadriga.aspects.annotations.AccessPolicies;
import edu.asu.spring.quadriga.aspects.annotations.ElementAccessPolicy;
import edu.asu.spring.quadriga.aspects.annotations.NoAuthorizationCheck;
import edu.asu.spring.quadriga.aspects.annotations.RestAccessPolicies;
import edu.asu.spring.quadriga.exceptions.QuadrigaAccessException;
import edu.asu.spring.quadriga.exceptions.RestException;
import edu.asu.spring.quadriga.web.rest.ProjectRestController;

@Aspect
@Component
public class RestAccessAspect 
{
	private static final Logger logger = LoggerFactory
			.getLogger(RestAccessAspect.class);
	
	@Autowired
	private IAuthorizationManager authorizationManager;
	
	@Around("within(edu.asu.spring.quadriga.web..*) && @annotation(noCheck)")
	public Object chkProjectAuthorization(ProceedingJoinPoint pjp, NoAuthorizationCheck noCheck) throws Throwable 
	{
		return pjp.proceed();
	}
	

	@Around("within(edu.asu.spring.quadriga.web..*) && @annotation(checks)")
	public Object chkAuthorization(ProceedingJoinPoint pjp, RestAccessPolicies checks) throws Throwable  
	{
		boolean haveAccess;
		String userName;
		String accessObjectId;
		IAuthorization authorization;
		
		
		haveAccess = true;
		
		//retrieve the logged in User name
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 userName = auth.getName();
		//Loop through all the access policies specified
		ElementAccessPolicy[] policies = checks.value();
		
		for(ElementAccessPolicy policy : policies)
		{
			//retrieve the authorization object based on the type
			authorization = authorizationManager.getAuthorizationObject(policy.type());
			
			//calling the object
			if(policy.paramIndex() > 0)
			{
			accessObjectId = pjp.getArgs()[policy.paramIndex()-1].toString();
			haveAccess = authorization.chkAuthorization(userName,accessObjectId, policy.userRole());
			}
			else
			{
				haveAccess = authorization.chkAuthorizationByRole(userName, policy.userRole());
			}
			
			if(haveAccess){
				logger.info("I am here, who am i, i am rest access policy");
				break;
			}
		}
		
		if(!haveAccess)
		{
			logger.info("I am here, who am i, i am restaccesspolicy");
			throw new RestException(400);
		}
		Object retVal = pjp.proceed();
		return retVal;
	}
	
}