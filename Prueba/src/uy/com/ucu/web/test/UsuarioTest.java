/**
 * 
 */
package uy.com.ucu.web.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uy.com.ucu.web.primefaces.*;

/**
 * @author mortelli
 *
 */
public class UsuarioTest {

	private static UsuarioController usuarioBeanTest;
	
	private static Usuario testUsuario;
	private static Usuario testUsuarioInvalido;
	
	private static String testUsername = "test";
	private static String testPassword = "password";
	private static String testEmail = "example@test.com";
	private static String testNombreCompleto = "Juan Pérez";
	private static String testDireccion = "Avenida Siempreverde 742";
	private static String testCelular = "09001234";	
	
	private static String testInvalidUsername = "invalid";
	private static String testInvalidPassword = "invalid";
	
	private String loginSuccessMessage = "home.xhtml?faces-redirect=true";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		usuarioBeanTest = new UsuarioController();
		
		testUsuario = new Usuario();
		testUsuario.setCelular(testCelular);
		testUsuario.setDireccion(testDireccion);
		testUsuario.setEmail(testEmail);
		testUsuario.setNombreCompleto(testNombreCompleto);
		testUsuario.setPassword(testPassword);
		testUsuario.setUsername(testUsername);
		
		testUsuarioInvalido = new Usuario();
		testUsuarioInvalido.setCelular(testCelular);
		testUsuarioInvalido.setDireccion(testDireccion);
		testUsuarioInvalido.setEmail(testEmail);
		testUsuarioInvalido.setNombreCompleto(testNombreCompleto);
		testUsuarioInvalido.setPassword(testPassword);
		testUsuarioInvalido.setUsername(testUsername);
		testUsuarioInvalido.setPassword(testInvalidPassword);
		testUsuarioInvalido.setUsername(testInvalidUsername);
		
		loadValidUser(usuarioBeanTest);
		usuarioBeanTest.userRegistration();									
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		usuarioBeanTest.getEm().getTransaction().begin();
		usuarioBeanTest.getEm().createNamedQuery("Usuario.deleteByUsername")
		.setParameter("username", testUsername)
        .executeUpdate();
		usuarioBeanTest.getEm().getTransaction().commit();
	}
	
	private static void loadValidUser(UsuarioController usuarioBean){
		loadBean(usuarioBean, testUsuario);
	}
	
	private static void loadInvalidUser(UsuarioController usuarioBean){
		loadBean(usuarioBean, testUsuarioInvalido);
	}
	
	private static void loadBean(UsuarioController usuarioBean, Usuario u){
		usuarioBean.setCelular(u.getCelular());
		usuarioBean.setDireccion(u.getDireccion());
		usuarioBean.setEmail(u.getEmail());
		usuarioBean.setNombreCompleto(u.getNombreCompleto());
		usuarioBean.setPassword(u.getPassword());
		usuarioBean.setUsername(u.getUsername());
	}

	@Test
	public void testSuccessfulRegistration() {		
		TypedQuery<Usuario> retrievedUsuarios = usuarioBeanTest.getEm().createNamedQuery("Usuario.findByUsername", Usuario.class)
		.setParameter("username",testUsername);
		List<Usuario> resultList = retrievedUsuarios.getResultList();
		
		if (resultList.isEmpty()){
			fail();
		}else{
			Usuario retrievedUsuario = resultList.get(0);
			assertTrue(testUsuario.equals(retrievedUsuario));
		}		
	}
	
	@Test
	public void testFailedRegistration() {		
		TypedQuery<Usuario> retrievedUsuarios = usuarioBeanTest.getEm().createNamedQuery("Usuario.findByUsername", Usuario.class)
		.setParameter("username",testInvalidUsername);
		List<Usuario> resultList = retrievedUsuarios.getResultList();
		assertTrue(resultList.isEmpty());			
	}
	
	@Test
	public void testSuccessfulLogin() {
		loadValidUser(usuarioBeanTest);
		String returnMessage = usuarioBeanTest.loginControl();		
		assertEquals(returnMessage, loginSuccessMessage);		
	}
	
	@Test
	public void testFailedLogin() {
		loadInvalidUser(usuarioBeanTest);
		String returnMessage = usuarioBeanTest.loginControl();
		assertNotEquals(returnMessage, loginSuccessMessage);		
	}

}
