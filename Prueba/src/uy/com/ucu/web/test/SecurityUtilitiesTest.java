package uy.com.ucu.web.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uy.com.ucu.web.utils.SecurityUtilities;

public class SecurityUtilitiesTest {
	
	private String hashableString = "hashable";
	private String unhashableString = null;
	private String expectedHashResult = "dd16fe9f76604a7400d5e1fcf88afaca";
	
	private SecurityUtilities securityUtilities = new SecurityUtilities();

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testSuccessfulHash() {
		String hashResult = securityUtilities.hash(hashableString);
		assertEquals(expectedHashResult, hashResult);
	}
	
	@Test
	public void testFailedHash(){
		assertEquals(null,securityUtilities.hash(unhashableString));
	}

}
