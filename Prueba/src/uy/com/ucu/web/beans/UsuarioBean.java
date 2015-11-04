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

import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Geolocalizacion;
import uy.com.ucu.web.utilities.GeolocationUtilities;
import uy.com.ucu.web.utilities.MailUtilities;
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
	private String codigo;
	private Boolean validado;
	
	private String successfulLoginURL = "home.xhtml?faces-redirect=true";
	private String failedLoginURL = "";
	private String successfulRegistrationURL = "Hello.xhtml?faces-redirect=true";
	private String failedRegistrationURL = "";
	private String validationURL = "http://localhost:8080/Prueba/Validacion.xhtml?codigo="; //Cambiar dependiendo del servidor a utilizar
	
	private EntityManager entityManager;
	
	private SecurityUtilities securityUtilities;
	private SessionUtilities sessionUtilities;
	
	public UsuarioBean(){		
		setSecurityUtilities(new SecurityUtilities());
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());		
	}
	
	public void beginTransaction(){
		getEntityManager().getTransaction().begin();
	}
	
	public void endTransaction(){
		getEntityManager().getTransaction().commit();
	}
	
	public String login(){		
		
		String result = failedLoginURL;
		FacesContext context = FacesContext.getCurrentInstance();
		
		beginTransaction();
		
		try{
			getEntityManager().createNamedQuery("Usuario.control", Usuario.class)
				.setParameter("username", username)
				.setParameter("password", getSecurityUtilities().hash(password)).getSingleResult();//getResultList().size();
			
			// get Http Session and store username
            HttpSession session = SessionUtilities.getSession();
            session.setAttribute("username", username);
            
            result = successfulLoginURL;
		}catch(Exception e){
			//RequestContext.getCurrentInstance().update("msg");
			RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario o contrasena invalidos"));
		}		
		
		endTransaction();
		return result;
	}
	
	public String logout() {
	      HttpSession session = SessionUtilities.getSession();
	      session.invalidate();
	      return "Hello.xhtml?faces-redirect=true";
	}
	
	public String userRegistration(){
		
		String result = failedRegistrationURL;
		
		beginTransaction();

		//Verificar email
		try{
			getEntityManager().createNamedQuery("Usuario.findByEmail", Usuario.class)
				.setParameter("email", email)
				.getSingleResult();

			//Ya existe un usuario con el email
			RequestContext.getCurrentInstance().update("msgGeneral");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email ya registrado"));
			
			//Abortar registro
			endTransaction();
			return result;
			
		}catch(Exception e){		
			// OK
		}
		
		//Si el email no está en uso, pasa a verificar username		
		try{
			getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class)
				.setParameter("username", username)
				.getSingleResult();

			//Ya existe un usuario con el username
			RequestContext.getCurrentInstance().update("msgGeneral");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario ya registrado"));
		}catch(Exception e){
			Usuario toInsert = new Usuario();
			toInsert.setCelular(getCelular());
			toInsert.setDireccion(getDireccion());
			toInsert.setEmail(getEmail());
			toInsert.setNombreCompleto(getNombreCompleto());
			toInsert.setPassword(getSecurityUtilities().hash(getPassword()));
			toInsert.setUsername(getUsername());
			toInsert.setValidado(false);
			toInsert.setCodigo(getSecurityUtilities().hash(getUsername()));
			getEntityManager().persist(toInsert);
			
			MailUtilities.send(
	                "login", "farmaciayaing3@gmail.com",
	                "password", "putoelquelee",
	                "to", getEmail(),
	                "subject", "Confirmacion de e-Mail",
	                "body", "<h1>Hola " + getNombreCompleto() + ":</h1><p>Gracias por registrarte en FarmaciaYa! <br /><br /> Para confirmar tu dirección de correo electrónico haz click <a href="+ validationURL + toInsert.getCodigo() + ">aqui</a>.</p>"
	        );
			
			result = successfulRegistrationURL;
		}
		
		endTransaction();		
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Boolean getValidado() {
		return validado;
	}

	public void setValidado(Boolean validado) {
		this.validado = validado;
	}
}

