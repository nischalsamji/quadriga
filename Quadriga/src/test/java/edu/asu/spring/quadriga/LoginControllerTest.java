package edu.asu.spring.quadriga;

import static org.junit.Assert.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.support.BindingAwareModelMap;

public class LoginControllerTest {

	LoginController loginController;
	Principal principal;	
	UsernamePasswordAuthenticationToken authentication;
	CredentialsContainer credentials;
	BindingAwareModelMap model;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		loginController = new LoginController();

		model =  new BindingAwareModelMap();		
		principal = new Principal() {			
			@Override
			public String getName() {
				return "test";
			}
		};		
		authentication = new UsernamePasswordAuthenticationToken(principal, credentials);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidUserHandle() {

		//Valid and Active user
		assertEquals(loginController.validUserHandle(model, principal, authentication),"hello");
		
		//Valid but inactive user		
		principal = new Principal() {			
			@Override
			public String getName() {
				return "jack";
			}
		};		
		assertEquals(loginController.validUserHandle(model, principal, authentication),"inactiveuser");
		
		//Ivalid user
		principal = new Principal() {			
			@Override
			public String getName() {
				return "abc";
			}
		};		
		assertEquals(loginController.validUserHandle(model, principal, authentication),"nouser");

	}

	@Test
	public void testLogin() {
		assertEquals(loginController.login(model),"login");
	}

	@Test
	public void testLoginerror() {
		assertEquals(loginController.loginerror(model),"login");
	}

	@Test
	public void testLogout() {
		assertEquals(loginController.logout(model),"login");
	}

}
