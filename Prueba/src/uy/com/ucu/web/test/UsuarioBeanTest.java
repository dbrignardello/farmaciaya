package uy.com.ucu.web.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uy.com.ucu.web.primefaces.Usuario;
import uy.com.ucu.web.primefaces.UsuarioController;

public class UsuarioBeanTest {

	//Variables internas
	//En su mayoría son estáticas al ser utilizadas en el beforeClass y afterClass, los cuales son métodos estáticos por definición
	
	//Bean para manejar usuarios
	private static UsuarioController usuarioBean;
	
	//Usuario válido
	private static Usuario usuarioValido;
	private static Usuario usuarioInvalido;
	
	//Datos de usuario válido
	private static String usernameValido = "test";
	private static String passwordValido = "password";
	private static String emailValido = "example@test.com";
	private static String nombreCompletoValido = "Juan Pérez";
	private static String direccionValido = "Avenida Siempreverde 742";
	private static String celularValido = "09001234";	
	
	//Datos de usuario inválido
	private static String usernameInvalido = "inválido";
	private static String passwordInvalido = "inválido";
	private static String emailInvalido = "inválido";
	private static String nombreCompletoInvalido = "inválido";
	private static String direccionInvalido = "inválido";
	private static String celularInvalido = "inválido";
	
	//Mensaje de éxito de log
	private String loginSuccessMessage = "home.xhtml?faces-redirect=true";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		//Inicialización de variables
		usuarioBean = new UsuarioController();
		
		//Cargado de datos de usuario válido
		usuarioValido = new Usuario();
		usuarioValido.setCelular(celularValido);
		usuarioValido.setDireccion(direccionValido);
		usuarioValido.setEmail(emailValido);
		usuarioValido.setNombreCompleto(nombreCompletoValido);
		usuarioValido.setPassword(passwordValido);
		usuarioValido.setUsername(usernameValido);
		
		//Cargado de datos de usuario inválido
		usuarioInvalido = new Usuario();
		usuarioInvalido.setCelular(celularInvalido);
		usuarioInvalido.setDireccion(direccionInvalido);
		usuarioInvalido.setEmail(emailInvalido);
		usuarioInvalido.setNombreCompleto(nombreCompletoInvalido);
		usuarioInvalido.setPassword(passwordInvalido);
		usuarioInvalido.setUsername(usernameInvalido);
		usuarioInvalido.setPassword(passwordInvalido);
		usuarioInvalido.setUsername(usernameInvalido);
		
		//Carga el usuario válido en el bean y lo registra en la base de datos
		loadValidUser(usuarioBean);
		usuarioBean.userRegistration();		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Borra el usuario válido de prueba de la base de datos
		
		usuarioBean.getEntityManager().getTransaction().begin();
		
		usuarioBean.getEntityManager().createNamedQuery("Usuario.deleteByUsername")
			.setParameter("username", usernameValido)
			.executeUpdate();
		
		usuarioBean.getEntityManager().getTransaction().commit();
	}
	
	//Carga un usuario válido en el bean
	private static void loadValidUser(UsuarioController usuarioBean){
		loadBeanFromUser(usuarioBean, usuarioValido);
	}
	
	//Carga un usuario inválido en el bean
	private static void loadInvalidUser(UsuarioController usuarioBean){
		loadBeanFromUser(usuarioBean, usuarioInvalido);
	}
	
	//Carga un usuario en el bean
	private static void loadBeanFromUser(UsuarioController usuarioBean, Usuario usuario){
		usuarioBean.setCelular(usuario.getCelular());
		usuarioBean.setDireccion(usuario.getDireccion());
		usuarioBean.setEmail(usuario.getEmail());
		usuarioBean.setNombreCompleto(usuario.getNombreCompleto());
		usuarioBean.setPassword(usuario.getPassword());
		usuarioBean.setUsername(usuario.getUsername());
	}
	
	//Crea un objeto Usuario en base a los datos cargados en el bean
	private static Usuario extractUserFromBean(UsuarioController usuarioBean){		
		Usuario usuario = new Usuario();
		usuario.setCelular(usuarioBean.getCelular());
		usuario.setDireccion(usuarioBean.getDireccion());
		usuario.setEmail(usuarioBean.getEmail());
		usuario.setNombreCompleto(usuarioBean.getNombreCompleto());
		usuario.setPassword(usuarioBean.getPassword());
		usuario.setUsername(usuarioBean.getUsername());
		usuario.setPassword(usuarioBean.getPassword());
		
		return usuario;
	}

	@Test
	public void testSuccessfulRegistration_existingUser() {		
		loadValidUser(usuarioBean);
		TypedQuery<Usuario> retrievedUsuarios = usuarioBean.getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class)
		.setParameter("username",usernameValido);
		List<Usuario> resultList = retrievedUsuarios.getResultList();
		
		Usuario retrievedUsuario = resultList.isEmpty() ? null : resultList.get(0);
		Usuario usuarioInBean = extractUserFromBean(usuarioBean); 
		usuarioInBean.setPassword(usuarioBean.getSecurityUtilities().hash(usuarioInBean.getPassword()));
		assertTrue(usuarioInBean.equals(retrievedUsuario));
	}
	
	@Test
	//Reescribir este código, conceptualmente erróneo
	public void testFailedRegistration_nonExistingUser() {		
		loadInvalidUser(usuarioBean);
		TypedQuery<Usuario> retrievedUsuarios = usuarioBean.getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class)
		.setParameter("username",usernameInvalido);
		List<Usuario> resultList = retrievedUsuarios.getResultList();
		assertTrue(resultList.isEmpty());			
	}
	
	@Test
	public void testSuccessfulLogin() {
		loadValidUser(usuarioBean);
		String returnMessage = usuarioBean.loginControl();		
		assertEquals(returnMessage, loginSuccessMessage);		
	}
	
	@Test
	public void testFailedLogin() {
		loadInvalidUser(usuarioBean);
		String returnMessage = usuarioBean.loginControl();
		assertNotEquals(returnMessage, loginSuccessMessage);		
	}

}
