package uy.com.ucu.web.primefaces;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.context.RequestContext;

@ManagedBean(name="usuario") 
@SessionScoped

public class UsuarioController implements Serializable{
	private String username;
	private String password;
	
	EntityManagerFactory emf;
	EntityManager em;
	
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
		//emf = Persistence.createEntityManagerFactory("Prueba");
		
		
		
		em = Persistence.createEntityManagerFactory("prueba").createEntityManager();//emf.createEntityManager();
		em.getTransaction().begin();
		
		try{
			Usuario u = em.createNamedQuery("Usuario.control", Usuario.class).setParameter("username", username).setParameter("password", password).getSingleResult();
			if(u!=null){
				RequestContext.getCurrentInstance().update("growl");
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Nombre de usuario o contrasena invalidos"));
				return "home.xhtml?faces-redirect=true";
			}
			return "";
		}catch(Exception e){
			return "";
		}
		
	}
}
