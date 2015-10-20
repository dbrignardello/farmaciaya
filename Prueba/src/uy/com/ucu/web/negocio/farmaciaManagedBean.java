package uy.com.ucu.web.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import util.FarmaciaVM;
import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.backoffice.UsuarioManagerBean;
import uy.com.ucu.web.utilities.GeolocationUtilities;
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean 
@SessionScoped
public class farmaciaManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Farmacia farmacia;
	private List<FarmaciaVM> farmaciasUsuario;
	private List<Farmacia> farmacias;	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Usuario usuario;

	public farmaciaManagedBean() {
		farmaciasUsuario = new ArrayList<>();
		 HttpSession session = SessionUtilities.getSession();
         String username=(String) session.getAttribute("username");
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());	
		connectToDatabase();
		try{
			//Obtener usuario logueado y todas las farmacias
			this.setUsuario(getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username",username).getSingleResult());
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<FarmaciaVM> getFarmaciasUsuario() {
		return farmaciasUsuario;
	}

	public void setFarmaciasUsuario(List<FarmaciaVM> farmaciasUsuario) {
		this.farmaciasUsuario = farmaciasUsuario;
	}
	
	public String seleccionarFarmacia(Farmacia farmacia){
		setFarmacia(farmacia);
		return "Farmacia.xhtml?faces-redirect=true";
	}

	public Farmacia getFarmacia() {
		return farmacia;
	}

	public void setFarmacia(Farmacia farmacia) {
		this.farmacia = farmacia;
	}

}