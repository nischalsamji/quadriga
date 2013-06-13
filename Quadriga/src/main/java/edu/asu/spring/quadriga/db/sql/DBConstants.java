package edu.asu.spring.quadriga.db.sql;

/**
 * @Description   : interface to hold the DB string constants.
 * 
 * @Called By     : DBConnectionManager.java
 * 
 * @author        : Kiran Kumar Batna
 * @author 		  : Ram Kumar Kumaresan
 */
public interface DBConstants 
{
	public final static String SP_CALL = "call";
	
	public final static String USER_DETAILS = "sp_getUserDetails";	
	public final static String ACTIVE_USER_DETAILS="sp_getActiveUsers";
	public final static String INACTIVE_USER_DETAILS="sp_getInActiveUsers";
	public final static String DEACTIVATE_USER="sp_deactivateUser";
	public final static String UPDATE_USER_ROLES="sp_updateUserRoles";
	public final static String GET_USER_REQUESTS="sp_getUserRequests";
	public final static String APPROVE_USER_REQUEST="sp_approveUserRequest";
	public final static String DENY_USER_REQUEST="sp_denyUserRequest";
	public final static String ADD_USER_REQUEST="sp_addUserRequest";
	
	public static final String PROJECT_LIST="sp_getProjectList";
	public static final String PROJECT_DETAILS="sp_getProjectDetails";
	public static final String PROJECT_COLLABORATORS="sp_getProjectCollaborators";

	public static final String ADD_PROJECT_REQUEST = "sp_addProjectDetails";
	public static final String ADD_COLLABORATOR_REQUEST = "sp_addProjectCollaborators";
	
	public static final String SHOW_COLLABORATOR_REQUEST = "sp_showProjectCollaborators";

	
	public static final String ADD_DICTIONARY = "sp_addDictionaryDetails";
	public static final String GET_DICTIONARY_DETAILS = "sp_getDictionaryDetails";

	public static final String GET_CCOWNED_DETAILS = "sp_getConceptCollections";
	public static final String GET_CCCOLLABORATIONS_DETAILS = "sp_getUserCollectionCollaborations";
	public static final String VALIDATE_COLLECTIONID = "sp_validatecollectionid";
	public static final String GET_COLLECTION_DETAILS = "sp_getConceptCollectionDetails";
	public static final String ADD_COLLECTION_ITEM = "sp_addCollectionItems";
	public static final String GET_DICTIONARY_ITEMS_DETAILS = "sp_getDictionaryItems";
	public static final String GET_DICTIONARY_NAME = "sp_getDictionaryName";
	public static final String ADD_DICTIONARY_ITEM = "sp_addDictionaryItems";
	public static final String DELETE_DICTIONARY_ITEM = "sp_deleteDictionaryItems";
	public static final String UPDATE_DICTIONARY_ITEM = "sp_updateDictionaryItems";
	public static final String ADD_CONCEPTCOLLECTION ="sp_addConceptCollections";
	public static final String DELETE_COLLECTION_ITEM ="sp_deleteCollectionItem";
	public static final String UPDATE_COLLECTION_ITEM ="sp_updateCollectionItem";

}
