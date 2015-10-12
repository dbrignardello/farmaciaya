package uy.com.ucu.web.primefaces;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.primefaces.context.RequestContext;

import uy.com.ucu.web.utils.SecurityUtilities;


@ManagedBean(name="usuario") 
@ApplicationScoped
public class UsuarioController implements Serializable{
	private String username;
	private String password;
	private String email;
	private String nombreCompleto;
	private String direccion;
	private String celular;
	
	private EntityManager entityManager;
	
	private SecurityUtilities securityUtilities = new SecurityUtilities();
	
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
	
	public String loginControl(){		
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());
		getEntityManager().getTransaction().begin();
		try{
			Usuario u = getEntityManager().createNamedQuery("Usuario.control", Usuario.class).setParameter("username", username).setParameter("password", getSecurityUtilities().hash(password)).getSingleResult();
			getEntityManager().getTransaction().commit();
			return "home.xhtml?faces-redirect=true";
			
		}catch(Exception e){
			RequestContext.getCurrentInstance().update("msg");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario o contrasena invalidos"));
		
			getEntityManager().getTransaction().commit();
			return "";
		}
	}
	
	public String userRegistration(){
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());
		getEntityManager().getTransaction().begin();
		try{
			Usuario aux = entityManager.createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username", username).getSingleResult();

				//Ya existe un usuario con el username
				entityManager.getTransaction().commit();
				RequestContext.getCurrentInstance().update("msgGeneral");
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario en uso"));
			return "";
		}catch(Exception e){
			Usuario toInsert = new Usuario();
			toInsert.setCelular(getCelular());
			toInsert.setDireccion(getDireccion());
			toInsert.setEmail(getEmail());
			toInsert.setNombreCompleto(getNombreCompleto());
			toInsert.setPassword(getSecurityUtilities().hash(getPassword()));
			toInsert.setUsername(getUsername());
			getEntityManager().persist(toInsert);
			getEntityManager().getTransaction().commit();
			return "Hello.xhtml?faces-redirect=true";
		}
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
}

