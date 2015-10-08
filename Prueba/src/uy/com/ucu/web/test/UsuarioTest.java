/**
 * 
 */
package uy.com.ucu.web.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.faces.context.FacesContext;
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

	//Variables internas
	//En su mayor�a son est�ticas al ser utilizadas en el beforeClass y afterClass, los cuales son m�todos est�ticos por definici�n
	
	//Bean para manejar usuarios
	private static UsuarioController usuarioBean;
	
	//Usuario v�lido
	private static Usuario usuarioValido;
	
	//Datos de usuario v�lido
	private static String usernameValido = "test";
	private static String passwordValido = "password";
	private static String emailValido = "example@test.com";
	private static String nombreCompletoValido = "Juan P�rez";
	private static String direccionValido = "Avenida Siempreverde 742";
	private static String celularValido = "09001234";	
	
	//Datos de usuario inv�lido
	private static String usernameInvalido = "inv�lido";
	private static String passwordInvalido = "inv�lido";
	private static String emailInvalido = "inv�lido";
	private static String nombreCompletoInvalido = "inv�lido";
	private static String direccionInvalido = "inv�lido";
	private static String celularInvalido = "inv�lido";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {		
		//Cargado de datos de usuario v�lido
		usuarioValido = new Usuario();
		usuarioValido.setCelular(celularValido);
		usuarioValido.setDireccion(direccionValido);
		usuarioValido.setEmail(emailValido);
		usuarioValido.setNombreCompleto(nombreCompletoValido);
		usuarioValido.setPassword(passwordValido);
		usuarioValido.setUsername(usernameValido);
	}
	
	@Test
	public void testUsuarioHashCode(){
		Usuario otroUsuarioValido = new Usuario();
		
		assertNotEquals(usuarioValido.hashCode(),otroUsuarioValido.hashCode());
		otroUsuarioValido.setCelular(usuarioValido.getCelular());
		
		assertNotEquals(usuarioValido.hashCode(),otroUsuarioValido.hashCode());
		otroUsuarioValido.setDireccion(usuarioValido.getDireccion());
		
		assertNotEquals(usuarioValido.hashCode(),otroUsuarioValido.hashCode());
		otroUsuarioValido.setEmail(usuarioValido.getEmail());
		
		assertNotEquals(usuarioValido.hashCode(),otroUsuarioValido.hashCode());
		otroUsuarioValido.setNombreCompleto(usuarioValido.getNombreCompleto());
		
		assertNotEquals(usuarioValido.hashCode(),otroUsuarioValido.hashCode());
		otroUsuarioValido.setPassword(usuarioValido.getPassword());
		
		assertNotEquals(usuarioValido.hashCode(),otroUsuarioValido.hashCode());
		otroUsuarioValido.setUsername(usuarioValido.getUsername());

		assertEquals(usuarioValido.hashCode(),otroUsuarioValido.hashCode());
	}
	
	@Test
	public void testUsuarioEquals(){
		
		assertTrue(usuarioValido.equals(usuarioValido));
		
		Usuario usuarioComparable = null;
		assertNotEquals(usuarioValido,usuarioComparable);
		
		assertNotEquals(usuarioValido,usuarioBean);
		
		usuarioComparable = new Usuario();
		
		assertNotEquals(usuarioComparable, usuarioValido);
		
		usuarioComparable.setCelular(celularInvalido);
		assertNotEquals(usuarioComparable,usuarioValido);
		usuarioComparable.setCelular(celularValido);
		assertNotEquals(usuarioComparable,usuarioValido);
		
		usuarioComparable.setDireccion(direccionInvalido);
		assertNotEquals(usuarioComparable,usuarioValido);
		usuarioComparable.setDireccion(direccionValido);
		assertNotEquals(usuarioComparable,usuarioValido);
		
		usuarioComparable.setEmail(emailInvalido);
		assertNotEquals(usuarioComparable,usuarioValido);
		usuarioComparable.setEmail(emailValido);
		assertNotEquals(usuarioComparable,usuarioValido);
		
		usuarioComparable.setNombreCompleto(nombreCompletoInvalido);
		assertNotEquals(usuarioComparable,usuarioValido);
		usuarioComparable.setNombreCompleto(nombreCompletoValido);
		assertNotEquals(usuarioComparable,usuarioValido);
		
		usuarioComparable.setPassword(passwordInvalido);
		assertNotEquals(usuarioComparable,usuarioValido);
		usuarioComparable.setPassword(passwordValido);
		assertNotEquals(usuarioComparable,usuarioValido);
		
		usuarioComparable.setUsername(usernameInvalido);
		assertNotEquals(usuarioComparable,usuarioValido);
		usuarioComparable.setUsername(usernameValido);
		
		assertTrue(usuarioComparable.equals(usuarioValido));
	}

}
