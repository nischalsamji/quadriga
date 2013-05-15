package edu.asu.spring.quadriga.domain.implementation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.asu.spring.quadriga.domain.IUser;
import edu.asu.spring.quadriga.domain.ICollaborator;

public class ConceptCollectionTest {
	
	private ConceptCollection conceptcollection;
	private IUser owner;
	private List<ICollaborator> collaborators;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		this.conceptcollection = new ConceptCollection();
		this.owner = new User();
		
		this.collaborators = new ArrayList<ICollaborator>();
		collaborators.add(new Collaborator());
		collaborators.add(new Collaborator());
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetName() {
		
		conceptcollection.setName(null);
		assertEquals(conceptcollection.getName(), null);
		
		conceptcollection.setName("jane");
		assertEquals(conceptcollection.getName(),"jane");
				
	}

	@Test
	public void testGetDescription() {
		
		conceptcollection.setDescription(null);
		assertEquals(conceptcollection.getDescription(), null);
		
		conceptcollection.setDescription("xyz");
		assertEquals(conceptcollection.getDescription(), "xyz");
		
	}

	@Test
	public void testGetId() {
		
		conceptcollection.setId(null);
		assertEquals(conceptcollection.getId(), null);
		
		conceptcollection.setId("3333");
		assertEquals(conceptcollection.getId(), "3333");
		
	}

	
	@Test
	public void testGetOwner() {
		
		conceptcollection.setOwner(null);
		assertEquals(conceptcollection.getOwner(), null);
		
		conceptcollection.setOwner(owner);
		assertEquals(conceptcollection.getOwner(), owner);
				
	}

	@Test
	public void testGetCollaborators() {
		
		conceptcollection.setCollaborators(null);
		assertEquals(conceptcollection.getCollaborators(), null);
		
		conceptcollection.setCollaborators(collaborators);
		assertEquals(conceptcollection.getCollaborators(), collaborators);
		
	}

}