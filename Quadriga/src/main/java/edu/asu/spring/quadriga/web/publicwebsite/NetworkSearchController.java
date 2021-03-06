package edu.asu.spring.quadriga.web.publicwebsite;

import edu.asu.spring.quadriga.domain.workbench.IProject;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.profile.ISearchResult;
import edu.asu.spring.quadriga.profile.IService;
import edu.asu.spring.quadriga.service.workbench.IRetrieveJsonProjectManager;
import edu.asu.spring.quadriga.service.workbench.IRetrieveProjectManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller searches for concept terms
 * and returns json as response and also a combined network of searched terms
 *
 * @author Vikranth Doosa
 */
@Controller
public class NetworkSearchController {

    @Autowired
    IRetrieveProjectManager projectManager;

    @Qualifier("conceptPowerService")
    @Autowired
    IService service;

    private static String defaultJsonErrorMsg = "{\"status\" : 500," +
            " \"message\": \"Unable to get the search terms\"}";

    private static final Logger logger = LoggerFactory.getLogger(NetworkSearchController.class);

    /**
     * This method will return a search page
     *
     * @return view
     * @throws QuadrigaStorageException
     */
    @RequestMapping(value = "sites/{projectUnixName}/search", method = RequestMethod.GET)
    public String getSearch(@PathVariable("projectUnixName") String projectUnixName, Model model)
            throws QuadrigaStorageException {

        IProject project = projectManager.getProjectDetailsByUnixName(projectUnixName);
        if (project == null) {
            return "forbidden";
        }

        model.addAttribute("project", project);
        return "sites/search";
    }

    /**
     * This method returns json data for search term
     *
     * @return json
     */
    @RequestMapping(value = "sites/{projectUnixName}/search", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getSearchTerms(@RequestParam("searchTerm") String searchTerm) {
        List<ISearchResult> searchResults = service.search(searchTerm);
        List<JSONObject> jsonResults = new ArrayList<JSONObject>();

        try {
            if (searchResults != null) {
                for (ISearchResult result : searchResults) {
                    JSONObject jsonResult = new JSONObject();
                    jsonResult.put("id", result.getId());
                    jsonResult.put("name", result.getName());
                    jsonResult.put("description", result.getDescription());
                    jsonResults.add(jsonResult);
                }
            }

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("terms", jsonResults);

            return new ResponseEntity<String>(jsonResponse.toString(), HttpStatus.OK);
        } catch (JSONException e) {
            // Exception
            logger.error("Json exception while adding the results", e);
        }
        return new ResponseEntity<String>(defaultJsonErrorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
