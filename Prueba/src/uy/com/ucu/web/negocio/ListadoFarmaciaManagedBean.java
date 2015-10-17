package uy.com.ucu.web.negocio;

import java.io.Serializable;
import java.util.List;

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
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean 
@SessionScoped
public class ListadoFarmaciaManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;


	
	private List<Farmacia> farmacias;	
	@PersistenceContext
	private EntityManager entityManager;

	public ListadoFarmaciaManagedBean() {
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());	
		connectToDatabase();
		try{
			this.setFarmacias(getEntityManager().createNamedQuery("Farmacia.findAll", Farmacia.class).getResultList());			
			
		}catch(Exception e){
			
		}
		
		disconnectFromDatabase();

	}

	public void connectToDatabase(){
		getEntityManager().getTransaction().begin();
	}
	
	public void disconnectFromDatabase(){
		getEntityManager().getTransaction().commit();
	}
	private EntityManager getEntityManager() {
		return entityManager;
	}

	private void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}



	public List<Farmacia> getFarmacias() {
		return farmacias;
	}



	public void setFarmacias(List<Farmacia> farmacias) {
		this.farmacias = farmacias;
	}

}