package edu.asu.spring.quadriga.web.publicwebsite;



import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.spring.quadriga.domain.INetwork;
import edu.asu.spring.quadriga.domain.INetworkNodeInfo;
import edu.asu.spring.quadriga.domain.IProject;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.service.INetworkManager;
import edu.asu.spring.quadriga.service.workbench.IRetrieveProjectManager;
import edu.asu.spring.quadriga.web.network.NetworkListManager;


@Controller
public class WebsiteProjectController {
	
	//public static String projectid = "";
	
	@Autowired 
	IRetrieveProjectManager projectManager;
	
	@Autowired
	INetworkManager networkmanager;
	
	@Autowired
	@Qualifier("qStoreURL")
	private String qStoreURL;

	@Autowired
	@Qualifier("qStoreURL_Add")
	private String qStoreURL_Add;

	@Autowired
	@Qualifier("qStoreURL_Get")
	private String qStoreURL_Get;

	private static final Logger logger = LoggerFactory
			.getLogger(NetworkListManager.class);

	/*
	 * Prepare the QStore GET URL
	 */
	public String getQStoreGetURL() {
		return qStoreURL+""+qStoreURL_Get;
	}

	
	public IRetrieveProjectManager getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(IRetrieveProjectManager projectManager) {
		this.projectManager = projectManager;
	}
	
	public IProject getProjectDetails(String name) throws QuadrigaStorageException{
		IProject project = projectManager.getProjectDetailsByUnixName(name);
		return project;
	}

	
	/**
	 * This method displays the public or external Website for the particular project 
	 * 
	 * If the project has been set to 'accessible', then the public website page is displayed. If the project does not exist
	 * then an error page is shown.
	 * @param unixName unix name that is given to the project at the time of its creation
	 * @param model
	 * @return
	 * @throws QuadrigaStorageException
	 */
	@RequestMapping(value="sites/{ProjectUnixName}", method=RequestMethod.GET)
	public String showProject(@PathVariable("ProjectUnixName") String unixName,Model model) throws QuadrigaStorageException {
		
		
		IProject project = getProjectDetails(unixName);
		//IProject project = projectManager.getProjectDetailsByUnixName(unixName);
		//String projectid = project.getInternalid();
		if(project!=null){
			model.addAttribute("project", project);
			return "sites/website";
		} 
		else
			return "forbidden";
		
	}
	
	/**
	 * This method retrieves all the networks associated with the project based on the project unix name
	 * 
	 * If the project contains networks, it displays all of the networks along with the names of the workspaces
	 * that contain the networks. If no networks have been created for that
	 * particular project, then an appropriate error page is displayed.
	 * @param unixName unix name that is given to the project at the time of its creation
	 * @param model
	 * @return 
	 * @throws QuadrigaStorageException
	 */
	@RequestMapping(value="sites/{ProjectUnixName}/browsenetworks", method=RequestMethod.GET)
	public String browseNetworks(@PathVariable("ProjectUnixName") String unixName,Model model) throws QuadrigaStorageException{
		System.out.println("browse");
		IProject project = getProjectDetails(unixName);
		String projectid = project.getInternalid();
		List<INetwork> Networks = networkmanager.getNetworksInProject(projectid);
		
		//List<String> networkNames = null;

		if(Networks != null)
		{
			//networkNames = new ArrayList<String>();
			for(INetwork network : Networks){
				System.out.println(network.getWorkspace().getName());
			}
				//networkNames.add(network.getName());
				 
		}
		
		if(!Networks.isEmpty()){
			model.addAttribute("networks", Networks);
			model.addAttribute("project", project);
			return "sites/browseNetworks";
		}
		
		return "NoNetworks";
	}
	
	/**
	 * This method gives the visualization of the network with the given network id 
	 * @param networkId network id of the network that has to be visualized
	 * @param model
	 * @param principal
	 * @return
	 * @throws QuadrigaStorageException
	 * @throws JAXBException
	 */
	@RequestMapping(value = "sites/networks/visualize/{networkId}", method = RequestMethod.GET)
	public String visualizeNetworks(@PathVariable("networkId") String networkId, ModelMap model, Principal principal) throws QuadrigaStorageException, JAXBException {
		StringBuffer jsonstring=new StringBuffer();
		logger.debug("Network id "+networkId);
		String qstoreGetURL = getQStoreGetURL();
		logger.debug("Qstore Get URL : "+qstoreGetURL);
		List<INetworkNodeInfo> networkTopNodesList = networkmanager.getNetworkTopNodes(networkId);
		Iterator <INetworkNodeInfo> I = networkTopNodesList.iterator();
		jsonstring.append("[");
		networkmanager.setIntialValueForD3JSon();
		List<List<Object>>relationEventPredicateMapping = new ArrayList<List<Object>>();
		networkmanager.setRelationEventPredicateMapping(relationEventPredicateMapping);
		while(I.hasNext()){
			INetworkNodeInfo networkNodeInfo = I.next();
			logger.debug("Node id "+networkNodeInfo.getId());
			logger.debug("Node statement type "+networkNodeInfo.getStatementType());
			jsonstring.append(networkmanager.generateJsontoJQuery(networkNodeInfo.getId(), networkNodeInfo.getStatementType()));
		}
		logger.info(networkmanager.getD3JSon());
		String jsonstring1 = jsonstring.toString();
		if(jsonstring1.charAt(jsonstring1.length()-1) == ','){
			jsonstring1 = jsonstring1.substring(0, jsonstring1.length()-1);
		}
		jsonstring1 = jsonstring1+"]";
	//	String newJson = jsonstring1+"#"+networkId;
		logger.info(jsonstring1);
	
		logger.info("Json object formed and sent to the JSP");
		
		String nwId = "\""+networkId+"\"";
		model.addAttribute("jsonstring",jsonstring1);
		model.addAttribute("networkid",nwId);
		logger.debug("json string:"+ jsonstring1);
		logger.info("network id:"+nwId);
		
		//model.addAttribute("networkId",networkId);
		return "sites/networks/visualize";
	}
}