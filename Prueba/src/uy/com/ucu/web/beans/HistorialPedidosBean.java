package uy.com.ucu.web.beans;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;

import org.primefaces.event.RateEvent;

import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Pedido;
import uy.com.ucu.web.utilities.SecurityUtilities;
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean(name="historialPedidos")
@ViewScoped
public class HistorialPedidosBean {
	
	private EntityManager entityManager;
	private SecurityUtilities securityUtilities;
	private SessionUtilities sessionUtilities;

	private List<Pedido> pedidos;
	
	
	public HistorialPedidosBean(){
		setSecurityUtilities(new SecurityUtilities());
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());	
		//Obtener el usuario logueado
		HttpSession session = SessionUtilities.getSession();
		String username=(String) session.getAttribute("username");
		
		beginTransaction();
	    Usuario user = getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username",username).getSingleResult();
	    endTransaction();
	    
	    //Obtener pedidos de usuario
	    setPedidos(user.getPedidos());
	    
	}
	
	/*
	 * Getters and setters
	 */
	public List<Pedido> getPedidos() {
		return pedidos;
	}
	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	public EntityManager getEntityManager() {
		return entityManager;
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
	
	
	/*
	 * Auxiliar methods
	 */
	public void beginTransaction(){
		getEntityManager().getTransaction().begin();
	}
	
	public void endTransaction(){
		getEntityManager().getTransaction().commit();
	}
	
	
	
	
	/*
	 * Bean methods
	 */
	
	//Actualiza el rating del pedido con la calificacion ingresada por el usuario
	public void calificar(RateEvent rateEvent){
		//To implement
		/*
		 * Buscar el pedido por id (ver como recibirlo desde el frontend)
		 * setearle al pedido el parametro calificacion
		 * 
		 */
		String selectedObjID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedObj");
		int idPedido = Integer.parseInt(selectedObjID);
		int calificacion = ((Integer) rateEvent.getRating()).intValue();
		
		beginTransaction();
		Pedido pedidoToRate = getEntityManager().find(Pedido.class, idPedido);
		pedidoToRate.setRating(calificacion);
		getEntityManager().flush();
		endTransaction();
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación", "Used calificó: " + ((Integer) rateEvent.getRating()).intValue());
	    FacesContext.getCurrentInstance().addMessage(null, message);	
	}
	
	public void cancelar(){
		//Setea 0 a la calificacion
		String selectedObjID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedObj");
		int idPedido = Integer.parseInt(selectedObjID);
		int calificacion = 0;
		
		beginTransaction();
		Pedido pedidoToRate = getEntityManager().find(Pedido.class, idPedido);
		pedidoToRate.setRating(calificacion);
		getEntityManager().flush();
		endTransaction();
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación", "Calificación anulada");
        FacesContext.getCurrentInstance().addMessage(null, message);
	}

	
	
}
