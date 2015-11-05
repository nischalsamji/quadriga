package edu.asu.spring.quadriga.service.impl.workspace;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.spring.quadriga.dao.workbench.IProjectWorkspaceDAO;
import edu.asu.spring.quadriga.dao.workspace.IWorkspaceDAO;
import edu.asu.spring.quadriga.dspace.service.impl.DspaceItemSubmitter;
import edu.asu.spring.quadriga.dspace.service.impl.DspaceMetadataItemEntity;
import edu.asu.spring.quadriga.dto.WorkspaceDTO;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.dspace.service.IDspaceManager;
import edu.asu.spring.quadriga.dspace.service.IDspaceMetadataItemEntity;

public class ListWSManagerTest {

    @InjectMocks
    private ListWSManager listmanager;

    @Mock
    private IWorkspaceDAO workspaceDao; 
    
    @Mock
    private IProjectWorkspaceDAO projectWorkspaceDao; 
    
    @Mock
    private IDspaceManager dspaceManager;
   

    @Before
    public void setup() {
        workspaceDao = Mockito.mock(IWorkspaceDAO.class);
        projectWorkspaceDao = Mockito.mock(IProjectWorkspaceDAO.class);
        dspaceManager = Mockito.mock(IDspaceManager.class);
        MockitoAnnotations.initMocks(this);
    }  

    

    @Test
    public void getArchiveStatusReturnsTrue() throws QuadrigaStorageException {
        String workSpaceId = "10663fb3-8a81-498a-8e2f-423dcc17ac8a";
        WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setIsarchived(true);
        Mockito.when(workspaceDao.getWorkspaceDTO(workSpaceId)).thenReturn(
                workspaceDTO);
        Assert.assertTrue(listmanager.getArchiveStatus(workSpaceId));
    }
    
    @Test
    public void getArchiveStatusReturnsFalse() throws QuadrigaStorageException {
        String workSpaceId = "10663fb3-8a81-498a-8e2f-423dcc17ac8a";
        WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setIsarchived(false);
        Mockito.when(workspaceDao.getWorkspaceDTO(workSpaceId)).thenReturn(
                workspaceDTO);
        Assert.assertFalse(listmanager.getArchiveStatus(workSpaceId));
    }

    @Test
    public void getDeactiveStatusReturnsTrue() throws QuadrigaStorageException {
        String workSpaceId = "10663fb3-8a81-498a-8e2f-423dcc17ac8a";
        WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setIsdeactivated(true);
        Mockito.when(workspaceDao.getWorkspaceDTO(workSpaceId)).thenReturn(
                workspaceDTO);
        Assert.assertTrue(listmanager.getDeactiveStatus(workSpaceId));
    }
    
    @Test
    public void getDeactiveStatusReturnsFalse() throws QuadrigaStorageException {
        String workSpaceId = "10663fb3-8a81-498a-8e2f-423dcc17ac8a";
        WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setIsdeactivated(false);
        Mockito.when(workspaceDao.getWorkspaceDTO(workSpaceId)).thenReturn(
                workspaceDTO);
        Assert.assertFalse(listmanager.getDeactiveStatus(workSpaceId));
    }
    
    @Test
    public void getProjectIdFromWorkspaceIdReturnSameString()
            throws QuadrigaStorageException {
        String workSpaceId = "10663fb3-8a81-498a-8e2f-423dcc17ac8a";
        String value = "abc";
        Mockito.when(projectWorkspaceDao.getCorrespondingProjectID(workSpaceId))
                .thenReturn(value);
        Assert.assertEquals(value,listmanager.getProjectIdFromWorkspaceId(workSpaceId));
    } 
   
    @Test
    public void getItemMetadataAsJsonReturnSameString()
            throws QuadrigaStorageException, NoSuchAlgorithmException, JSONException {
        IDspaceMetadataItemEntity dspaceMetaDetaIdentity = new DspaceMetadataItemEntity();
        Date date = new Date();
        String dateString = date.toString();
        dspaceMetaDetaIdentity.setLastModifiedDate(date);
        dspaceMetaDetaIdentity.setName("name");
        DspaceItemSubmitter dspaceItemSubmitter = new DspaceItemSubmitter();
        dspaceItemSubmitter.setFullname("fullname");
        dspaceMetaDetaIdentity.setSubmitter(dspaceItemSubmitter);
        Mockito.when(
                dspaceManager.getItemMetadata("fileid", "username", "password",
                        null)).thenReturn(dspaceMetaDetaIdentity);  
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj2 = new JSONObject();
        JSONArray jsonArray = new JSONArray();        
        jsonObj.put("filename", "name");
        jsonObj.put("submitter", "fullname");
        jsonObj.put("modifieddate", dateString);
        jsonArray.put(jsonObj);
        jsonObj2.putOpt("text", jsonArray);
        Assert.assertEquals(jsonObj2.toString(),listmanager.getItemMetadataAsJson("fileid", "username", "password", null));        
    }
    

}
