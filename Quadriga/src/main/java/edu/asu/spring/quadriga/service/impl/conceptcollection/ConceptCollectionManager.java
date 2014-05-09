/**
 * 
 */
package edu.asu.spring.quadriga.service.impl.conceptcollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import edu.asu.spring.quadriga.db.conceptcollection.IDBConnectionCCManager;
import edu.asu.spring.quadriga.db.workbench.IDBConnectionRetrieveProjectManager;
import edu.asu.spring.quadriga.db.workspace.IDBConnectionListWSManager;
import edu.asu.spring.quadriga.domain.ICollaboratorRole;
import edu.asu.spring.quadriga.domain.IUser;
import edu.asu.spring.quadriga.domain.conceptcollection.IConcept;
import edu.asu.spring.quadriga.domain.conceptcollection.IConceptCollection;
import edu.asu.spring.quadriga.domain.conceptcollection.IConceptCollectionCollaborator;
import edu.asu.spring.quadriga.domain.conceptcollection.IConceptCollectionConcepts;
import edu.asu.spring.quadriga.domain.factory.conceptcollection.IConceptFactory;
import edu.asu.spring.quadriga.domain.implementation.ConceptpowerReply;
import edu.asu.spring.quadriga.domain.workbench.IProject;
import edu.asu.spring.quadriga.domain.workspace.IWorkSpace;
import edu.asu.spring.quadriga.exceptions.QuadrigaAccessException;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.service.ICollaboratorRoleManager;
import edu.asu.spring.quadriga.service.conceptcollection.IConceptCollectionManager;
import edu.asu.spring.quadriga.service.conceptcollection.mapper.IConceptCollectionDeepMapper;
import edu.asu.spring.quadriga.service.conceptcollection.mapper.IConceptCollectionShallowMapper;
import edu.asu.spring.quadriga.service.workbench.mapper.IProjectShallowMapper;
import edu.asu.spring.quadriga.service.workspace.IListWSManager;
import edu.asu.spring.quadriga.service.workspace.mapper.IWorkspaceShallowMapper;

/**
 * 
 * This class has all the Concept collection service layer functions.
 * It includes the handling DB and controller services.  
 * @author satyaswaroop
 *
 */
@Service
public class ConceptCollectionManager implements IConceptCollectionManager {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ConceptCollectionManager.class);

	@Autowired
	private IDBConnectionCCManager dbConnect;
	
	@Autowired
	private IConceptFactory conceptFactory;
	
	@Inject
	@Named("restTemplate")
	RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("conceptPowerURL")
	private String conceptURL;
	
	@Autowired
	@Qualifier("searchConceptPowerURLPath")
	private String searchURL;
	
	@Autowired
	@Qualifier("updateConceptPowerURLPath")
	private String updateURL;
	
	@Autowired
	private IConceptCollectionDeepMapper conceptCollectionDeepMapper;
	
	@Autowired
	private ICollaboratorRoleManager roleMapper ;
	
	@Autowired
	private	IListWSManager wsManager;
	
	@Autowired
	private IDBConnectionRetrieveProjectManager projectManager;
	
	@Autowired
	private IDBConnectionListWSManager wsListManger;
	
	@Autowired
	private IConceptCollectionShallowMapper ccShallowMapper;
	
	@Autowired
	private IProjectShallowMapper projectShallowMapper;
	
	@Autowired
	private IWorkspaceShallowMapper wsShallowMapper;

//	/**
//	 * This method retrieves the concept collection owner by the submitted user
//	 * @param userId - logged in user
//	 * @throws QuadrigaStorageException
//	 * @return List<IConceptCollection> list of concept collection associated with the user as owner
//	 */
//	@Override
//	@Transactional
//	public List<IConceptCollection> getCollectionsOwnedbyUser(String sUserId) throws QuadrigaStorageException
//	{
//		List<IConceptCollection> conceptList = new ArrayList<IConceptCollection>();  
//		conceptList = dbConnect.getConceptsOwnedbyUser(sUserId);
//		return conceptList;
//	}

	/**
	 * This method retrieves the concept collection owner by the submitted user
	 * @param userId - logged in user
	 * @throws QuadrigaStorageException
	 * @return List<ConceptCollectionDTO> list of concept collection associated with the user as owner
	 */
	@Override
	@Transactional
	public List<IConceptCollection> getCollectionsOwnedbyUser(String sUserId) throws QuadrigaStorageException
	{
		List<IConceptCollection> conceptList = new ArrayList<IConceptCollection>();  
		conceptList = ccShallowMapper.getConceptCollectionList(sUserId);
		return conceptList;
	}


//	/**
//	 * This methods retrieves the concept collection associated with the user
//	 * as a collaborator
//	 * @param sUserID - logged in user id
//	 * @param List<IConceptCollection> - list of concept collection associated 
//	 * with user as a collaborator
//	 * @throws QuadrigatorageException
//	 */
//	@Override
//	@Transactional
//	public List<IConceptCollection> getUserCollaborations(String sUserId) throws QuadrigaStorageException {
//		
//		List<IConceptCollection> conceptList = new ArrayList<IConceptCollection>();  
//		conceptList = dbConnect.getCollaboratedConceptsofUser(sUserId);
//		return conceptList;
//	}
	/**
	 * This methods retrieves the concept collection associated with the user
	 * as a collaborator
	 * @param sUserID - logged in user id
	 * @param List<IConceptCollection> - list of concept collection associated 
	 * with user as a collaborator
	 * @throws QuadrigatorageException
	 */
	@Override
	@Transactional
	public List<IConceptCollection> getUserCollaborations(String sUserId) throws QuadrigaStorageException {
		
		List<IConceptCollection> conceptList = new ArrayList<IConceptCollection>();  
		conceptList = ccShallowMapper.getConceptCollectionListOfCollaborator(sUserId);
		return conceptList;
	}


	/**
	 * This method retrieves the concept collection details
	 * @param concept - concept collection object containing the id
	 * @param username - logged in user name
	 * @throws QuadrigaStorageException
	 */
	@Override
	@Transactional
	public void getCollectionDetails(IConceptCollection concept, String username) throws QuadrigaStorageException, QuadrigaAccessException {
		dbConnect.getCollectionDetails(concept, username);
	}

	/**
	 * This method searches the items and its part of speech in the concept power database
	 * @param item - concept collection item
	 * @param pos - part of speech of item word
	 */
	@Override
	public ConceptpowerReply search(String item, String pos) {
		
		ConceptpowerReply rep;
		
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("name", item);
		vars.put("pos", pos);
		
		if((item!=null && !item.isEmpty())  && (pos!=null && !pos.isEmpty())){
			rep = restTemplate.getForObject(conceptURL+searchURL+"{name}/{pos}", ConceptpowerReply.class, vars);
			return rep;
		}
		else
			return null;
	}
	
	/**
	 * This method updates the items associated to the concept collection
	 * @param id[] - array of items associated with the collection
	 * @param collection - concept collection object
	 * @param username - logged in user
	 * @throws QuadrigaStorageException
	 */
	@Override
	@Transactional
	public void update(String id[],IConceptCollection collection, String username) throws QuadrigaStorageException {
		
		IConcept concept;
		ConceptpowerReply rep;
		for(String i : id){
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("name", i);
		
		if((i!=null && !i.isEmpty())){
			rep = restTemplate.getForObject(conceptURL+updateURL+"{name}", ConceptpowerReply.class, vars);
			System.out.println("rep:: "+rep);
			System.out.println("getConceptEntry:: "+rep.getConceptEntry());
			System.out.println("conceptURL:: "+conceptURL);
			System.out.println("updateURL:: "+updateURL);
			System.out.println("name:: "+i);
			
			concept = conceptFactory.createConceptObject();
			concept.setConceptId(i);
			concept.setDescription(rep.getConceptEntry().get(0).getDescription());
			concept.setLemma(rep.getConceptEntry().get(0).getLemma());
			concept.setPos(rep.getConceptEntry().get(0).getPos());
			dbConnect.updateItem(concept,collection.getConceptCollectionId(),username);
		}
		}
	}
	
	/**
	 * This method returns Lemma for the given concept
	 * @param id - item id
	 * @return String - lemma associated with concept
	 */
	@Override
	public String getConceptLemmaFromConceptId(String id){
		
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("name", id);
		String Lemma=id;
		ConceptpowerReply rep = restTemplate.getForObject(conceptURL+updateURL+"{name}", ConceptpowerReply.class, vars);
		if(rep.getConceptEntry().size() == 0 ){
			return Lemma;
		}
		return rep.getConceptEntry().get(0).getLemma();
	}
	
	/**
	 * This method adds the items to the concept collection
	 * @param lemma
	 * @param id
	 * @param pos
	 * @param desc
	 * @param conceptcollectionId
	 * @param username - logged in user name
	 * @throws QuadrigaStorageException
	 * @throws QuadrigaAccessException
	 */
	@Override
	@Transactional
	public void addItems(String lemmma, String id, String pos, String desc, String conceptcollectionId, String username) throws QuadrigaStorageException, QuadrigaAccessException {
		dbConnect.saveItem(lemmma, id, pos, desc, conceptcollectionId,username);
	}

	/**
	 * This methods adds concept collection
	 * @param collection - Concept Collection object
	 * @throws QuadrigaStorageException
	 */
	@Override
	@Transactional
	public void addConceptCollection(IConceptCollection collection) throws QuadrigaStorageException {
		dbConnect.addCollection(collection);
	}

	/**
	 * This method deletes the item associated with the concept collection
	 * @param id - concept id
	 * @param collectionid - concept collection id
	 * @param username - logged in user name
	 * @throws QuadrigatorageException
	 */
	@Override
	@Transactional
	public void deleteItem(String id, String collectionId, String username) throws QuadrigaStorageException {
		dbConnect.deleteItems(id,collectionId,username);
		
	}
	
	/**
	 * @description	  retrieves non collaborating users from database
	 * @param  collectionid
	 * @throws QuadrigaStorageException
	 * @return list of non collaborating users
	 * @author rohit pendbhaje
	 */
	@Override
	@Transactional
	public List<IUser> showNonCollaboratingUsers(String collectionid) throws QuadrigaStorageException {
		List<IUser> nonCollaboratorList =  dbConnect.showNonCollaboratorRequest(collectionid);
		return nonCollaboratorList;
	}

	/**
	 * @description	  retrieves collaborating users from database
	 * @param  collectionid
	 * @throws QuadrigaStorageException
	 * @return list of collaborator objects
	 * @author rohit pendbhaje
	 */
	@Override
	@Transactional
	public List<IConceptCollectionCollaborator> showCollaboratingUsers(String collectionid) throws QuadrigaStorageException {
		//List<ICollaborator> collaboratorList = dbConnect.showCollaboratorRequest(collectionid);
		List<IConceptCollectionCollaborator>  ccCollaboratorList =  null;
		IConceptCollection conceptCollection =  conceptCollectionDeepMapper.getConceptCollectionDetails(collectionid);
		if(conceptCollection != null){
			ccCollaboratorList = conceptCollection.getConceptCollectionCollaborators(); 
		}
		return ccCollaboratorList;
	}

	/**
	 * @description	  retrieves collaborating users from database
	 * @param  collectioni
	 * @throws QuadrigaStorageException
	 * @author rohit pendbhaje
	 */
	@Override
	@Transactional
	public void getCollaborators(IConceptCollection collection) throws QuadrigaStorageException {
		
		IConceptCollection conceptCollection = conceptCollectionDeepMapper
				.getConceptCollectionDetails(collection
						.getConceptCollectionId());
		if (conceptCollection != null) {

			List<IConceptCollectionCollaborator> conceptCollectionCollaborators = conceptCollection
					.getConceptCollectionCollaborators();
			if (conceptCollectionCollaborators != null
					&& conceptCollectionCollaborators.size() > 0) {
				for (IConceptCollectionCollaborator conceptCollectionCollaborator : conceptCollectionCollaborators) {
					for (ICollaboratorRole collaboratorRole : conceptCollectionCollaborator
							.getCollaborator().getCollaboratorRoles()) {
						roleMapper
								.fillCollectionCollaboratorRole(collaboratorRole);
					}
				}
			}
		}
	}
	
	/**
	 * This method retrieves the concept collection id for the given concept collection name
	 * @param ccName - concept collection name
	 * @return String - concept collection id
	 * @throws QuadrigaStorageException
	 */
	@Override
	@Transactional
	public String getConceptCollectionId(String ccName) throws QuadrigaStorageException{
		String ccId = dbConnect.getConceptCollectionId(ccName);
		return ccId;
	}
	
	@Override
	@Transactional
	public String getProjectsTree(String userName,String ccId) throws JSONException{
		List<IProject> projectList = null;
		JSONObject core = new JSONObject();
		try {
			//projectList = projectManager.getProjectList(userName);
			projectList = projectShallowMapper.getProjectList(userName);
			JSONArray dataArray = new JSONArray();
			List<IProject> ccProjectsList = projectShallowMapper.getCollaboratorProjectListOfUser(ccId);
			List<IWorkSpace> ccWorkspaceList = wsListManger.getWorkspaceByConceptCollection(ccId);
			if(ccProjectsList != null) {
			for (IProject project : projectList) {
				// Each data
				//if (!ccProjectsList.contains(project)) {
					JSONObject data = new JSONObject();
					data.put("id", project.getProjectId());
					data.put("parent", "#");
					String projectLink = null;
					if(ccProjectsList.contains(project)){
						projectLink = project.getProjectName();
					}
					else {
					projectLink = "<a href='#' id='"
							+ project.getProjectId()
							+ "' name='"
							+ project.getProjectName()
							+ "' onclick='javascript:addCCtoProjects(this.id,this.name);' > "
							+ project.getProjectName() + "</a>";
					} 
					data.put("text", projectLink);
					dataArray.put(data);
					String wsParent = project.getProjectId();

					List<IWorkSpace> wsList = wsManager.listActiveWorkspace(
							project.getProjectId(), userName);
					for (IWorkSpace ws : wsList) {
						// workspace json
					//	if(!ccWorkspaceList.contains(ws)) {
							JSONObject data1 = new JSONObject();
							data1.put("id", ws.getWorkspaceId());
							data1.put("parent", wsParent);
							String wsLink = null;
							if(ccWorkspaceList.contains(ws)){
								wsLink = ws.getWorkspaceName();
							}
							else {
							 wsLink = "<a href='#' id='"
									+ ws.getWorkspaceId()
									+ "' name='"
									+ ws.getWorkspaceName()
									+ "' onclick='javascript:addCCtoWorkspace(this.id,this.name);' >"
									+ ws.getWorkspaceName() + "</a>";
							}
							data1.put("text", wsLink);
							dataArray.put(data1);
					//}
					}

				//}
			}
		}
			JSONObject dataList = new JSONObject();
			dataList.put("data", dataArray);

			core.put("core", dataList);
			//logger.info(core.toString(1));

		} catch (QuadrigaStorageException e) {
			logger.error("DB Error while fetching project, Workspace  details",
					e);
		}
		// return core.toString(SUCCESS);
		return core.toString(1);
	}
	
	@Override
	public boolean hasConceptID(List<IConceptCollectionConcepts> conceptCollectionConceptList , String id){
		for(IConceptCollectionConcepts conceptCollectionConcept : conceptCollectionConceptList){
			
			if(conceptCollectionConcept.getConcept().getConceptId().equals(id)){
				return true;
			}
		}
		return false;
	}
	
	
	
}
