package edu.asu.spring.quadriga.web.workspace;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.spring.quadriga.domain.IBitStream;
import edu.asu.spring.quadriga.domain.ICollaborator;
import edu.asu.spring.quadriga.domain.ICollection;
import edu.asu.spring.quadriga.domain.ICommunity;
import edu.asu.spring.quadriga.domain.IItem;
import edu.asu.spring.quadriga.domain.INetwork;
import edu.asu.spring.quadriga.domain.IWorkSpace;
import edu.asu.spring.quadriga.domain.factories.IDspaceKeysFactory;
import edu.asu.spring.quadriga.dspace.service.IDspaceKeys;
import edu.asu.spring.quadriga.dspace.service.IDspaceManager;
import edu.asu.spring.quadriga.dspace.service.impl.DspaceKeys;
import edu.asu.spring.quadriga.exceptions.QuadrigaAccessException;
import edu.asu.spring.quadriga.exceptions.QuadrigaException;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.service.workspace.ICheckWSSecurity;
import edu.asu.spring.quadriga.service.workspace.IListWSManager;
import edu.asu.spring.quadriga.service.workspace.IRetrieveWSCollabManager;

/**
 * Controller to handle all the workspace requests for Quadriga.
 * 
 *  The (Dspace) design flow is to load the list of all communities accessible to the user
 *  and then load all collections within the community selected by the user. This 
 *  second call for collection loads all the collections and the items within them.
 *  The last call is to load the set of bitstreams within a selected item. Any deviation 
 *  from the above flow is handled by the concerned classes.
 *  
 *  
 * @author Kiran Kumar Batna
 * @author Ram Kumar Kumaresan
 */

@Controller
@Scope(value="session")
public class ListWSController 
{
	@Autowired
	private	IListWSManager wsManager;

	private static final Logger logger = LoggerFactory.getLogger(ListWSController.class);

	@Autowired
	private	IRetrieveWSCollabManager wsCollabManager;

	@Autowired
	ICheckWSSecurity workspaceSecurity;

	@Autowired
	private IDspaceManager dspaceManager;

	private String dspaceUsername;
	private String dspacePassword;
	private IDspaceKeys dspaceKeys;

	@Autowired
	private IDspaceKeysFactory dspaceKeysFactory;

	public IDspaceKeysFactory getDspaceKeysFactory() {
		return dspaceKeysFactory;
	}


	public void setDspaceKeysFactory(IDspaceKeysFactory dspaceKeysFactory) {
		this.dspaceKeysFactory = dspaceKeysFactory;
	}


	public IDspaceManager getDspaceManager() {
		return dspaceManager;
	}


	public void setDspaceManager(IDspaceManager dspaceManager) {
		this.dspaceManager = dspaceManager;
	}


	public IRetrieveWSCollabManager getWsCollabManager() {
		return wsCollabManager;
	}


	public void setWsCollabManager(IRetrieveWSCollabManager wsCollabManager) {
		this.wsCollabManager = wsCollabManager;
	}


	public IListWSManager getWsManager() {
		return wsManager;
	}


	public void setWsManager(IListWSManager wsManager) {
		this.wsManager = wsManager;
	}

	/**
	 * Handle the request to view masked dspace keys and also allow an user to update the keys.
	 * 
	 * @return The model object containing the dspacekeys class for generating the form in the UI.
	 * @throws QuadrigaStorageException	Thrown when database encountered any problem during the operation.
	 */
	@RequestMapping(value="/auth/workbench/keys", method=RequestMethod.GET)
	public ModelAndView addDspaceKeysRequestForm(Principal principal) throws QuadrigaStorageException
	{
		this.dspaceKeys = dspaceManager.getMaskedDspaceKeys(principal.getName());
		ModelAndView model = new ModelAndView("/auth/workbench/keys","command",getDspaceKeysFactory().createDspaceKeysObject());
		model.addObject("dspaceKeys", dspaceKeys);

		return model;
	}

	/**
	 * Handle request to insert or update the dspace keys used by the user.
	 * @param dspaceKeys				The public and private key provided by the user.
	 * 
	 * @return							Redirect to the dspace manage pages.
	 * @throws QuadrigaStorageException	Thrown when database encountered any problem during the operation.
	 * @throws QuadrigaAccessException
	 */
	@RequestMapping(value = "/auth/workbench/updatekeys", method = RequestMethod.POST)
	public String addDspaceKeys(@ModelAttribute("SpringWeb")DspaceKeys dspaceKeys, Principal principal, ModelMap model) throws QuadrigaStorageException, QuadrigaAccessException 
	{
		if(dspaceKeys!=null)
		{
			if(dspaceKeys.getPrivateKey() != null && dspaceKeys.getPublicKey() != null)
			{
				if(!dspaceKeys.getPrivateKey().equals("") && !dspaceKeys.getPublicKey().equals(""))
				{
					dspaceManager.addDspaceKeys(dspaceKeys,principal.getName());
					this.dspaceKeys = dspaceKeys;
				}
			}
		}

		return "redirect:/auth/workbench/keys";
	}	

	/**
	 * This will list the details of workspaces 
	 * @param  workspaceid
	 * @param  model
	 * @return String - url of the page listing all the workspaces of the project.
	 * @throws QuadrigaStorageException
	 * @author Kiran Kumar Batna
	 * @throws QuadrigaAccessException 
	 * @throws QuadrigaException 
	 */
	@RequestMapping(value="auth/workbench/workspace/workspacedetails/{workspaceid}", method = RequestMethod.GET)
	public String getWorkspaceDetails(@PathVariable("workspaceid") String workspaceid, Principal principal, ModelMap model) throws QuadrigaStorageException, QuadrigaAccessException, QuadrigaException
	{
		String userName;
		IWorkSpace workspace;
		List<ICollaborator> collaboratorList;

		userName = principal.getName();
		workspace = getWsManager().getWorkspaceDetails(workspaceid,userName);

		//Check bitstream access in dspace. 
		this.dspaceKeys = dspaceManager.getDspaceKeys(principal.getName());
		List<IBitStream> workspaceBitStreams = dspaceManager.checkDspaceBitstreamAccess(workspace.getBitstreams(), this.dspaceKeys, this.dspaceUsername, this.dspacePassword);
		workspace.setBitstreams(workspaceBitStreams);

		//retrieve the collaborators associated with the workspace
		collaboratorList = getWsCollabManager().getWorkspaceCollaborators(workspaceid);


		workspace.setCollaborators(collaboratorList);
		List<INetwork> networkList = wsManager.getWorkspaceNetworkList(workspaceid);
		model.addAttribute("networkList", networkList);
		model.addAttribute("workspacedetails", workspace);

		//Load the Dspace Keys used by the user
		this.dspaceKeys = dspaceManager.getDspaceKeys(principal.getName());
		if(this.dspaceKeys != null)
		{
			model.addAttribute("dspaceKeys", "true");
		}
		else if((this.dspaceUsername != null && this.dspacePassword != null))
		{
			model.addAttribute("dspaceLogin", "true");
		}

		if(workspaceSecurity.checkWorkspaceOwner(userName, workspaceid)){
			model.addAttribute("owner", 1);
		}else{
			model.addAttribute("owner", 0);
		}
		if(workspaceSecurity.checkWorkspaceOwnerEditorAccess(userName, workspaceid)){
			model.addAttribute("editoraccess", 1);
		}else{
			model.addAttribute("editoraccess", 0);
		}

		return "auth/workbench/workspace/workspacedetails";
	}


	/**
	 * Following methods are responsible for the Dspace part of the workspace
	 */


	/**
	 * This method is responsible for the handle of dspace Username and password.
	 * After the assignment of dspace variables, this redirects to the dspace communities controller.
	 * 
	 * @param workspaceId		The workspace id from which the add request is raised.
	 * @param dspaceUsername	The dspace username provided by the user.
	 * @param dspacePassword	The dspace password provided by the user.
	 * @return					Redirect to the dspace communities page.
	 * @author 					Ram Kumar Kumaresan
	 */
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/adddspacelogin", method = RequestMethod.POST)
	public String addFilesDspaceAuthentication(@PathVariable("workspaceId") String workspaceId, HttpServletRequest req, ModelMap model, Principal principal) {		
		String dspaceUsername = req.getParameter("username");
		String dspacePassword = req.getParameter("password");
		String dspacePublicAccess = req.getParameter("dspacePublicAccess");
		if(dspacePublicAccess == null)
		{
			if(dspaceUsername == null || dspacePassword == null)
			{
				return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
			}
			else if(dspaceUsername.equals("") || dspacePassword.equals(""))
			{
				return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
			}
			this.dspaceUsername = dspaceUsername;
			this.dspacePassword = dspacePassword;
		}
		else
		{
			this.dspaceUsername = "";
			this.dspacePassword = "";
		}


		return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
	}


	/**
	 * This method is responsible for the handle of dspace Username and password.
	 * After the assignment of dspace variables, this redirects to the {@link #updateBitStreamsFromWorkspace(String, ModelMap, Principal)} controller
	 * for updating the bitstreams in this workspace
	 * 
	 * @param workspaceId		The workspace id from which the update request is raised.
	 * @param dspaceUsername	The dspace username provided by the user.
	 * @param dspacePassword	The dspace password provided by the user.
	 * @return					Redirect to the dspace update page.
	 * @author 					Ram Kumar Kumaresan
	 */
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/syncdspacelogin", method = RequestMethod.POST)
	public String syncFilesDspaceAuthentication(@PathVariable("workspaceId") String workspaceId, HttpServletRequest req, ModelMap model, Principal principal) {
		String dspaceUsername = req.getParameter("username");
		String dspacePassword = req.getParameter("password");
		String dspacePublicAccess = req.getParameter("dspacePublicAccess");

		//Check for username and password only when there are no keys for the user.
		if(this.dspaceKeys == null)
		{
			if(dspacePublicAccess == null)
			{
				if(dspaceUsername == null || dspacePassword == null)
				{
					return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
				}
				else if(dspaceUsername.equals("") || dspacePassword.equals(""))
				{
					return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
				}
				this.dspaceUsername = dspaceUsername;
				this.dspacePassword = dspacePassword;
			}
			else
			{
				this.dspaceUsername = "";
				this.dspacePassword = "";
			}
		}

		return "redirect:/auth/workbench/workspace/"+workspaceId+"/updatebitstreams";
	}

	/**
	 * This method is responsible for the assignment/change of dspace Username and password.
	 * When the user changes the dspace authentication. All the cached dspace data is cleared.
	 * After the assignment of dspace variables, this redirects to the workspace page.
	 * 
	 * @param workspaceId		The workspace id from which the add request is raised.
	 * @param dspaceUsername	The dspace username provided by the user.
	 * @param dspacePassword	The dspace password provided by the user.
	 * @return					Redirect to the workspace page.
	 * @author 					Ram Kumar Kumaresan
	 */
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/changedspacelogin", method = RequestMethod.POST)
	public String changeDspaceAuthentication(@PathVariable("workspaceId") String workspaceId, HttpServletRequest req, ModelMap model, Principal principal) {
		String dspaceUsername = req.getParameter("username");
		String dspacePassword = req.getParameter("password");
		String dspacePublicAccess = req.getParameter("dspacePublicAccess");
		if(dspacePublicAccess == null)
		{
			if(dspaceUsername == null || dspacePassword == null)
			{
				return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
			}
			else if(dspaceUsername.equals("") || dspacePassword.equals(""))
			{
				return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
			}
			this.dspaceUsername = dspaceUsername;
			this.dspacePassword = dspacePassword;
		}
		else
		{
			this.dspaceUsername = "";
			this.dspacePassword = "";
		}

		dspaceManager.clearCompleteCache();

		return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
	}


	/**
	 * Handle the request for the list of communities to be fetched from Dspace.
	 * 
	 * @return Return to the dspace communities page of Quadriga
	 * @author Ram Kumar Kumaresan
	 * @throws QuadrigaException 
	 */
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/communities", method = RequestMethod.GET)
	public String workspaceCommunityListRequest(@PathVariable("workspaceId") String workspaceId, ModelMap model, Principal principal) throws QuadrigaException {

		if(this.dspaceKeys == null && (this.dspaceUsername == null || this.dspacePassword == null))
		{
			return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
		}

		List<ICommunity> communities = dspaceManager.getAllCommunities(this.dspaceKeys, this.dspaceUsername,this.dspacePassword);

		model.addAttribute("communityList", communities);
		model.addAttribute("workspaceId",workspaceId);

		return "auth/workbench/workspace/communities";
	}


	/**
	 * Handle the request for the list of collections associated with a community.
	 * 
	 * @param communityId	The community id of the community whose collections are to be fetched.
	 * @return				Return to the collections page of Quadriga
	 * @author 				Ram Kumar Kumaresan
	 */
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/community/{communityId}", method = RequestMethod.GET)
	public String workspaceCommunityRequest(@PathVariable("workspaceId") String workspaceId, @PathVariable("communityId") String communityId, ModelMap model, Principal principal) {

		if(this.dspaceKeys == null && (this.dspaceUsername == null || this.dspacePassword == null))
		{
			return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
		}

		String communityName = dspaceManager.getCommunityName(communityId);

		//No community has been fetched. The user is trying to access the collection page directly
		//Redirect him to community list page
		if(communityName == null)
		{
			return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
		}
		List<ICollection> collections = dspaceManager.getAllCollections(dspaceKeys,this.dspaceUsername,this.dspacePassword, communityId);

		model.addAttribute("communityName", communityName);
		model.addAttribute("collectionList", collections);
		model.addAttribute("workspaceId",workspaceId);

		return "auth/workbench/workspace/community";
	}


	/**
	 * Handle the request for the list of items associated with a collection.
	 * 
	 * @param collectionId		The collection id of the collection whose items are to be fetched.
	 * @return					Return to the items page of Quadriga.
	 * @author 					Ram Kumar Kumaresan
	 */
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/community/collection/{collectionId}", method = RequestMethod.GET)
	public String workspaceItemListRequest(@PathVariable("workspaceId") String workspaceId, @PathVariable("collectionId") String collectionId, ModelMap model, Principal principal) {

		if(this.dspaceKeys == null && (this.dspaceUsername == null || this.dspacePassword == null))
		{
			return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
		}

		String communityId = dspaceManager.getCommunityId(collectionId);
		//No such collection has been fetched. The user is trying to access the item page directly
		//Redirect him to community list page
		if(communityId == null)
		{
			return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
		}

		String communityName = dspaceManager.getCommunityName(communityId);
		//No such community has been fetched. The user is trying to access the item page directly
		//Redirect him to community list page
		if(communityName == null)
		{
			return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
		}

		String collectionName = dspaceManager.getCollectionName(collectionId);
		//No such collection has been fetched. The user is trying to access the item page directly
		//Redirect him to community list page
		if(collectionName == null)
		{
			return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
		}

		List<IItem> items = dspaceManager.getAllItems(collectionId);
		model.addAttribute("communityId",communityId);
		model.addAttribute("communityName",communityName);
		model.addAttribute("collectionId",collectionId);
		model.addAttribute("collectionName", collectionName);		
		model.addAttribute("itemList", items);
		model.addAttribute("workspaceId",workspaceId);

		return "auth/workbench/workspace/community/collection";
	}

	/**
	 * Handle the request for the list of bitstreams associated with an item.
	 * 
	 * @param itemId		The id of the item whose bitstreams are to be listed.
	 * @param collectionId	The id of the collection to which the item and bitstream belongs to.
	 * 
	 * @return			Return to the bitstream page of quadriga.
	 * @author 			Ram Kumar Kumaresan
	 */
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/community/collection/item", method = RequestMethod.GET)
	public String workspaceBitStreamListRequest(@PathVariable("workspaceId") String workspaceId,@RequestParam("itemId") String itemId,@RequestParam("collectionId") String collectionId, ModelMap model, Principal principal){

		if(this.dspaceKeys == null && (this.dspaceUsername == null || this.dspacePassword == null))
		{
			return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
		}


		String communityId = dspaceManager.getCommunityId(collectionId);
		//No such collection has been fetched. The user is trying to access the item page directly
		//Redirect him to community list page
		if(communityId == null)
		{
			return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
		}

		String communityName = dspaceManager.getCommunityName(communityId);
		//No such community has been fetched. The user is trying to access the item page directly
		//Redirect him to community list page
		if(communityName == null)
		{
			return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
		}

		String collectionName = dspaceManager.getCollectionName(collectionId);
		//No such collection has been fetched. The user is trying to access the item page directly
		//Redirect him to community list page
		if(collectionName == null)
		{
			return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
		}

		String itemName = dspaceManager.getItemName(collectionId, itemId);
		//No such item has been fetched. The user is trying to access the bitstream page directly
		//Redirect him to community list page
		if(itemName == null)
		{
			return "redirect:/auth/workbench/workspace/"+workspaceId+"/communities";
		}


		List<IBitStream> bitstreams = dspaceManager.getAllBitStreams(dspaceKeys,this.dspaceUsername,this.dspacePassword,collectionId, itemId);
		model.addAttribute("communityId",communityId);
		model.addAttribute("communityName",communityName);
		model.addAttribute("collectionId",collectionId);
		model.addAttribute("collectionName", collectionName);
		model.addAttribute("itemId",itemId);
		model.addAttribute("itemName",itemName);
		model.addAttribute("bitList",bitstreams);	
		model.addAttribute("workspaceId",workspaceId);

		return "auth/workbench/workspace/community/collection/item";
	}


	/**
	 * Handle ajax requests to retrieve the collection name based on the collection id.
	 * 
	 * @param collectionid		The id of the collection whose name is to fetched.
	 * @return					Returns the collection name if it is loaded from Dspace. Else returns 'Loading...'
	 * @author 					Ram Kumar Kumaresan
	 * @throws QuadrigaException 
	 */
	@RequestMapping(value = "/auth/workbench/workspace/collectionstatus/{collectionid}", method = RequestMethod.GET)
	public @ResponseBody String getCollectionStatus(@PathVariable("collectionid") String collectionid) throws QuadrigaException {

		//Can't find collection in any of the communities
		if(dspaceManager.getCommunityId(collectionid) == null)
			return "Restricted Collection";

		ICollection collection = dspaceManager.getCollection(collectionid);
		if(collection != null)
		{
			if(collection.getLoadStatus() == true)
			{
				if(collection.getName() != null)
					return collection.getName();
				else
					return "Restricted Collection";
			}
		}
		return "Loading...";		
	}

	@RequestMapping(value = "/auth/workbench/workspace/itemstatus/{collectionid}/{itemid}", method = RequestMethod.GET)
	public @ResponseBody String getItemStatus(@PathVariable("collectionid") String collectionid, @PathVariable("itemid") String itemid) throws QuadrigaException {

		//Can't find collection in any of the communities
		if(dspaceManager.getCommunityId(collectionid) == null)
			return "Restricted Item";

		ICollection collection = dspaceManager.getCollection(collectionid);
		if(collection != null)
		{
			if(collection.getLoadStatus() == true)
			{
				if(collection.getName() != null)
				{
					//Collection has been loaded.
					IItem item = collection.getItem(itemid);
					if(item != null)
					{
						return item.getName();
					}
					else
					{
						//No item found in the collection
						return "Restricted Item";
					}
				}
				else
					return "Restricted Item";
			}
		}
		return "Loading...";
	}

	@RequestMapping(value = "/auth/workbench/workspace/bitstreamaccessstatus", method = RequestMethod.GET)
	public @ResponseBody String getBitStreamAccessStatus(@RequestParam("bitstreamid") String bitstreamid, @RequestParam("itemid") String itemid, @RequestParam("collectionid") String collectionid) throws QuadrigaException {

		//Can't find collection in any of the communities
		if(dspaceManager.getCommunityId(collectionid) == null)
			return "No Access to File";

		ICollection collection = dspaceManager.getCollection(collectionid);
		if(collection != null)
		{
			if(collection.getLoadStatus() == true)
			{
				if(collection.getName() != null)
				{
					//Collection has been loaded.
					IItem item = collection.getItem(itemid);
					if(item != null)
					{
						if(!item.getBitids().contains(bitstreamid))
						{
							//The item does not contain the bitstream
							return "No Access to File";
						}
						else
						{
							IBitStream bitstream = item.getBitStream(bitstreamid);
							if(bitstream!=null)
							{
								if(bitstream.getName() != null)
									return bitstream.getName();
							}
						}						
					}
					else
					{
						//No item found in the collection
						return "No Access to File";
					}
				}
				else
					return "No Access to File";
			}
		}
		return "Loading...";
	}



	/**
	 * Handle ajax requests to retrieve the bitstream name based on the bitstream id, item id and collection id
	 * 
	 * @param bitstreamId		The id of the bitstream whose name is to be retrieved.
	 * @param itemId			The id of the item to which the bitstream belongs to.
	 * @param collectionId		The id of the collection to which the item and the bitstream belongs to.
	 * @return					Returns the bitstream name if it is loaded. Else returns 'Loading...'
	 * @author 					Ram Kumar Kumaresan 
	 */
	@RequestMapping(value = "/auth/workbench/workspace/bitstreamstatus", method = RequestMethod.GET)
	public @ResponseBody String getBitStreamStatus(@RequestParam("bitstreamId") String bitstreamId, @RequestParam("itemId") String itemId,@RequestParam("collectionId") String collectionId) {
		IBitStream bitstream = dspaceManager.getBitStream(collectionId, itemId, bitstreamId);
		if(bitstream != null)
		{
			if(bitstream.getName() != null)
				return bitstream.getName();
		}
		return "Loading...";		
	}

	/**
	 * Handle the request to add one or more bitstreams to a workspace. The user must have access to the project that the workspace belongs to.
	 * If not, this method will throw an QuadrigaACcessException.
	 * 
	 * @param workspaceId					The id of the workspace to which the bitstream(s) are to be added.
	 * @param communityId					The id of the community to which the bitstream(s) belong to.
	 * @param collectionId					The id of the colletion to which the bitstream(s) belong to.
	 * @param itemId						The id of the item to which the bitstream(s) belong to.
	 * @param bitstreamids					The id(s) of the bitstream(s) which are to be added to the workspace.
	 * @return								Return to the workspace page
	 * @throws QuadrigaStorageException		Thrown when any unexpected error occurs in the database.
	 * @throws QuadrigaAccessException		Thrown when a user tries to modify a workspace to which he/she does not have access. Also thrown when a user tries to access this method with made-up request paramaters.
	 * @author 								Ram Kumar Kumaresan
	 * @throws QuadrigaException 
	 */
	//TODO: @AccessPolicies({ @ElementAccessPolicy(type = CheckedElementType.WORKSPACE,paramIndex = 1, userRole = { "SINGLE_WORKSPACE_ADMIN" } )})
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/addbitstreams", method = RequestMethod.POST)
	public String addBitStreamsToWorkspace(@PathVariable("workspaceId") String workspaceId, @RequestParam(value="communityid") String communityId,@RequestParam(value="collectionid") String collectionId,@RequestParam(value="itemid") String itemId,@RequestParam(value="bitstreamids") String[] bitstreamids, ModelMap model, Principal principal) throws QuadrigaStorageException, QuadrigaAccessException, QuadrigaException{
		if(this.dspaceKeys == null && (this.dspaceUsername == null || this.dspacePassword == null))
		{
			return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
		}

		dspaceManager.addBitStreamsToWorkspace(workspaceId, communityId, collectionId, itemId, bitstreamids, principal.getName());
		return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
	}

	/**
	 * Handle the request to delete bitstream(s) from a workspace.
	 * 
	 * @param workspaceId					The id of the workspace from which the bitstream(s) are to deleted. 
	 * @param bitstreamids					The id(s) of the bitstream(s) which are to deleted from the workspace.
	 * @return								Return to the workspace page.
	 * @throws QuadrigaStorageException		Thrown when any unexpected error occurs in the database.
	 * @throws QuadrigaAccessException		Thrown when a user tries to modify a workspace to which he/she does not have access. Also thrown when a user tries to access this method with made-up request paramaters.
	 * @author 								Ram Kumar Kumaresan
	 * @throws QuadrigaException 
	 */
	@RequestMapping(value = "/auth/workbench/workspace/{workspaceId}/deletebitstreams", method = RequestMethod.POST)
	public String deleteBitStreamsFromWorkspace(@PathVariable("workspaceId") String workspaceId, @RequestParam(value="bitstreamids") String[] bitstreamids, ModelMap model, Principal principal) throws QuadrigaStorageException, QuadrigaAccessException, QuadrigaException{

		IWorkSpace workspace = getWsManager().getWorkspaceDetails(workspaceId,principal.getName());
		//Check bitstream access in dspace. 
		this.dspaceKeys = dspaceManager.getDspaceKeys(principal.getName());
		List<IBitStream> workspaceBitStreams = dspaceManager.checkDspaceBitstreamAccess(workspace.getBitstreams(), this.dspaceKeys, this.dspaceUsername, this.dspacePassword);
		
		dspaceManager.deleteBitstreamFromWorkspace(workspaceId, bitstreamids, workspaceBitStreams, principal.getName());
		return "redirect:/auth/workbench/workspace/workspacedetails/"+workspaceId;
	}
}
