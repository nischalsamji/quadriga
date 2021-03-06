package edu.asu.spring.quadriga.web.login;

/**
 *  @description   : Constant string values representing special Quariga roles.
 *  
 *  @Called by     : UserManager.java
 *                 : CollaboratorController.java
 *  
 *  @author        : Kiran Kumar Batna
 *  
 */
public interface RoleNames {

	//Quadriga Role Constants
	public static final String ROLE_QUADRIGA_NOACCOUNT = "ROLE_QUADRIGA_NOACCOUNT";
	public static final String ROLE_QUADRIGA_DEACTIVATED = "ROLE_QUADRIGA_DEACTIVATED";
	public static final String ROLE_QUADRIGA_RESTRICTED = "ROLE_QUADRIGA_USER_RESTRICTED_USER";
	public static final String ROLE_QUADRIGA_ADMIN = "ROLE_QUADRIGA_USER_ADMIN";
	public static final String DB_ROLE_QUADRIGA_NOACCOUNT = "role_noaccount";
	public static final String ROLE_QUADRIGA_USER_STANDARD = "ROLE_QUADRIGA_USER_STANDARD";
	public static final String ROLE_QUADRIGA_USER_COLLABORATOR = "ROLE_QUADRIGA_USER_COLLABORATOR";
	
	//project collaborator roles constants
	public static final String ROLE_COLLABORATOR_ADMIN = "ADMIN";
	public static final String ROLE_PROJ_COLLABORATOR_ADMIN = "PROJECT_ADMIN";
	public static final String ROLE_PROJ_COLLABORATOR_CONTRIBUTOR = "CONTRIBUTOR";
	public static final String ROLE_PROJ_COLLABORATOR_EDITOR = "EDITOR";
	
	//Workspace collaborator roles constants
	public static final String ROLE_WORKSPACE_COLLABORATOR_ADMIN = "SINGLE_WORKSPACE_ADMIN";
	public static final String ROLE_WORKSPACE_COLLABORATOR_CONTRIBUTOR = "SINGLE_WORKSPACE_CONTRIBUTOR";
	public static final String ROLE_WORKSPACE_COLLABORATOR_EDITOR = "EDITOR";
	
	//dictionary collaborator roles constants
	public static final String ROLE_DICTIONARY_COLLABORATOR_ADMIN = "ADMIN";
	public static final String ROLE_DICTIONARY_COLLABORATOR_READ = "DICT_READ_ACCESS";
	public static final String ROLE_DICTIONARY_COLLABORATOR_READ_WRITE = "DICT_READ/WRITE_ACCESS";
	
	//concept collection roles constants
	public static final String ROLE_CC_COLLABORATOR_ADMIN = "ADMIN";
	public static final String ROLE_CC_COLLABORATOR_READ_WRITE = "CC_READ_WRITE_ACCESS";
	public static final String ROLE_CC_COLLABORATOR_READ = "CC_READ_ACCESS";
	
}
