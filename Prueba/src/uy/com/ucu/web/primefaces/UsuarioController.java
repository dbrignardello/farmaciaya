package uy.com.ucu.web.primefaces;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.context.RequestContext;

@ManagedBean(name="usuario") 
@ApplicationScoped
public class UsuarioController implements Serializable{
	private String username;
	private String password;
	private String email;
	private String nombreCompleto;
	private String direccion;
	private String celular;
	
	EntityManagerFactory emf;
	private EntityManager em;
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
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
		setEm(Persistence.createEntityManagerFactory("prueba").createEntityManager());
		getEm().getTransaction().begin();
		try{
			Usuario u = getEm().createNamedQuery("Usuario.control", Usuario.class).setParameter("username", username).setParameter("password", password).getSingleResult();
			if(u!=null){
				getEm().getTransaction().commit();
				return "home.xhtml?faces-redirect=true";
			}
			RequestContext.getCurrentInstance().update("msg");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario o contrasena invalidos"));
		
			getEm().getTransaction().commit();
			return "";
		}catch(Exception e){
			RequestContext.getCurrentInstance().update("msg");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario o contrasena invalidos"));
		
			getEm().getTransaction().commit();
			return "";
		}
	}
	
	public String userRegistration(){
		setEm(Persistence.createEntityManagerFactory("prueba").createEntityManager());
		getEm().getTransaction().begin();
		try{
			Usuario toInsert = new Usuario();
			toInsert.setCelular(getCelular());
			toInsert.setDireccion(getDireccion());
			toInsert.setEmail(getEmail());
			toInsert.setNombreCompleto(getNombreCompleto());
			toInsert.setPassword(getPassword());
			toInsert.setUsername(getUsername());
			getEm().persist(toInsert);
			getEm().getTransaction().commit();
			return "Hello.xhtml?faces-redirect=true";
		}catch(Exception e){
			getEm().getTransaction().commit();
			return "";
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
}

