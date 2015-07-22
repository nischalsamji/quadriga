package edu.asu.spring.quadriga.aspects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;

import edu.asu.spring.quadriga.domain.ICollaborator;
import edu.asu.spring.quadriga.domain.ICollaboratorRole;
import edu.asu.spring.quadriga.domain.IUser;
import edu.asu.spring.quadriga.domain.dictionary.IDictionary;
import edu.asu.spring.quadriga.domain.dictionary.IDictionaryCollaborator;
import edu.asu.spring.quadriga.domain.factory.dictionary.IDictionaryFactory;
import edu.asu.spring.quadriga.domain.factory.impl.dictionary.DictionaryFactory;
import edu.asu.spring.quadriga.domain.impl.Collaborator;
import edu.asu.spring.quadriga.domain.impl.CollaboratorRole;
import edu.asu.spring.quadriga.domain.impl.User;
import edu.asu.spring.quadriga.domain.impl.dictionary.Dictionary;
import edu.asu.spring.quadriga.domain.impl.dictionary.DictionaryCollaborator;
import edu.asu.spring.quadriga.exceptions.QuadrigaAccessException;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
import edu.asu.spring.quadriga.service.dictionary.IDictionaryManager;
import edu.asu.spring.quadriga.service.dictionary.IRetrieveDictionaryManager;

public class DictionaryAuthorizationTest {

    @Mock
    private IRetrieveDictionaryManager mockedManager;
    @Mock
    private IDictionaryManager dictionaryManager;
    
    @InjectMocks
    private DictionaryAuthorization authorization;
    
    private IDictionaryFactory dictionaryFactory;
    private List<IDictionaryCollaborator> collaborators;
    
    @Before
    public void init() throws QuadrigaStorageException {
        mockedManager = Mockito.mock(IRetrieveDictionaryManager.class);
        dictionaryManager = Mockito.mock(IDictionaryManager.class);
        dictionaryFactory = new DictionaryFactory();
        
        MockitoAnnotations.initMocks(this);
        
        Field fieldToSet = ReflectionUtils.findField(DictionaryAuthorization.class, "dictionaryFactory");
        ReflectionUtils.makeAccessible(fieldToSet);
        ReflectionUtils.setField(fieldToSet, authorization, dictionaryFactory);
        
        IDictionary dictionary = new Dictionary();
        final IUser owner = new User();
        owner.setUserName("testuser");
        dictionary.setOwner(owner);
        dictionary.setDictionaryId("dictionaryId");
        
        // create colaborator
        IDictionaryCollaborator ccCollaborator = new DictionaryCollaborator();
        ICollaborator collaborator = new Collaborator();
        IUser collabUser = new User();
        collabUser.setUserName("collabuser");
        collaborator.setUserObj(collabUser);
        
        // create collaborator roles 
        ICollaboratorRole role = new CollaboratorRole();
        role.setRoleid("role1");
        List<ICollaboratorRole> roles = new ArrayList<ICollaboratorRole>();
        roles.add(role);
        collaborator.setCollaboratorRoles(roles);
        
        ccCollaborator.setCollaborator(collaborator);
        collaborators = new ArrayList<IDictionaryCollaborator>();
        
        collaborators.add(ccCollaborator);
        
        Mockito.when(mockedManager.getDictionaryDetails("dictionaryId")).thenReturn(dictionary);
        Mockito.when(dictionaryManager.showCollaboratingUsers("dictionaryId")).thenReturn(collaborators);
    }
    
    @Test
    public void testChkAuthorizationIsOwner() throws QuadrigaStorageException, QuadrigaAccessException {
        // this should succeed because testuser is the owner of the collection
        boolean authorized = authorization.chkAuthorization("testuser", "dictionaryId", new String[] { "" });
        assertTrue(authorized);
    }
    
    @Test
    public void testChkAuthorizationHasValidRole() throws QuadrigaStorageException, QuadrigaAccessException {
        // this should succeed because collabuser is a collaborator and
        // has a role that has access to the collection
        boolean authorized = authorization.chkAuthorization("collabuser", "dictionaryId", new String[] { "role1" });
        assertTrue(authorized);
    }
    
    @Test
    public void testChkAuthorizationHasNoValidRole() throws QuadrigaStorageException, QuadrigaAccessException {
        // this should succeed because collabuser is a collaborator but
        // does not have a role that has access to the collection
        boolean authorized = authorization.chkAuthorization("collabuser", "dictionaryId", new String[] { "role2" });
        assertFalse(authorized);
    }
    
    @Test
    public void testChkAuthorizationIsNotCollaborator() throws QuadrigaStorageException, QuadrigaAccessException {
        // this should fail because testuser2 is not a collaborator
        boolean authorized = authorization.chkAuthorization("testuser2", "dictionaryId", new String[] { "role1" });
        assertFalse(authorized);
    }
    
}
