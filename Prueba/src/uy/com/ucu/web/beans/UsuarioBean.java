package uy.com.ucu.web.beans;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import util.mail;
import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Geolocalizacion;
import uy.com.ucu.web.utilities.GeolocationUtilities;
import uy.com.ucu.web.utilities.SecurityUtilities;
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean(name="usuario") 
@SessionScoped
public class UsuarioBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private String email;
	private String nombreCompleto;
	private String direccion;
	private String celular;
	
	private String successfulLoginURL = "home.xhtml?faces-redirect=true";
	private String failedLoginURL = "";
	private String successfulRegistrationURL = "Hello.xhtml?faces-redirect=true";
	private String failedRegistrationURL = "";
	
	private EntityManager entityManager;
	
	private SecurityUtilities securityUtilities;
	private SessionUtilities sessionUtilities;
	
	public UsuarioBean(){		
		setSecurityUtilities(new SecurityUtilities());
		setSessionUtilities(new SessionUtilities());
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());		
	}
	
	public void connectToDatabase(){
		getEntityManager().getTransaction().begin();
	}
	
	public void disconnectFromDatabase(){
		getEntityManager().getTransaction().commit();
	}
	
	public String login(){		
		
		String result = failedLoginURL;
		
		connectToDatabase();
		
		try{
			getEntityManager().createNamedQuery("Usuario.control", Usuario.class)
				.setParameter("username", username)
				.setParameter("password", getSecurityUtilities().hash(password)).getSingleResult();//getResultList().size();
			
			// get Http Session and store username
            HttpSession session = SessionUtilities.getSession();
            session.setAttribute("username", username);
            
            result = successfulLoginURL;
		}catch(Exception e){
			RequestContext.getCurrentInstance().update("msg");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario o contrasena invalidos"));
		}
		
		disconnectFromDatabase();
		return result;
	}
	
	public String logout() {
	      HttpSession session = getSessionUtilities().getSession();
	      session.invalidate();
	      return "failedLoginURL";
	}
	
	public String userRegistration(){
		
		String result = failedRegistrationURL;
		
		connectToDatabase();

		try{
			getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class)
				.setParameter("username", username)
				.getSingleResult();

			//Ya existe un usuario con el username
			RequestContext.getCurrentInstance().update("msgGeneral");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario en uso"));
		}catch(Exception e){
			Usuario toInsert = new Usuario();
			toInsert.setCelular(getCelular());
			toInsert.setDireccion(getDireccion());
			toInsert.setEmail(getEmail());
			toInsert.setNombreCompleto(getNombreCompleto());
			toInsert.setPassword(getSecurityUtilities().hash(getPassword()));
			toInsert.setUsername(getUsername());
			getEntityManager().persist(toInsert);
			mail.send(
	                "login", "farmaciayaing3@gmail.com",
	                "password", "putoelquelee",
	                "to", getEmail(),
	                "subject", "Confirmacion de e-mail",
	                "body", "<h1>Hola " + getNombreCompleto() + ":</h1><p>Gracias por registrarte en FarmaciaYa! \n Para confirmar tu dirección de correo electrónico ingresa al siguiente link:</p>"
	        );
			
			result = successfulRegistrationURL;
		}
		
		disconnectFromDatabase();
		
		return result;
	}

	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public SecurityUtilities getSecurityUtilities() {
		return securityUtilities;
	}
	
	public void setSecurityUtilities(SecurityUtilities securityUtilities) {
		this.securityUtilities = securityUtilities;
	}
	
	public SessionUtilities getSessionUtilities() {
		return sessionUtilities;
	}
	
	public void setSessionUtilities(SessionUtilities sessionUtilities) {
		this.sessionUtilities = sessionUtilities;
	}
	
	public Geolocalizacion obtenerLatitudLongitudUsuario(){
		GeolocationUtilities gu = new GeolocationUtilities();
		String direccionUsuario = direccion;
		String ubicacionUsuario = gu.pedidoGeolocacion(direccionUsuario);
		Double latitudUsuario = gu.coordenadaDeGeolocacion(ubicacionUsuario, "latitud");
		Double longitudUsuario = gu.coordenadaDeGeolocacion(ubicacionUsuario, "longitud");
		Geolocalizacion g = new Geolocalizacion(latitudUsuario, longitudUsuario);
		return g;
	}
}

