package uy.com.ucu.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;

import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Pedido;
import uy.com.ucu.web.utilities.SecurityUtilities;
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean(name="historialPedidos")
@SessionScoped
public class HistorialPedidosBean {
	
	private EntityManager entityManager;
	private SecurityUtilities securityUtilities;
	private SessionUtilities sessionUtilities;

	private List<Pedido> pedidos;
	
	public HistorialPedidosBean(){
		setSecurityUtilities(new SecurityUtilities());
		setSessionUtilities(new SessionUtilities());
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());	
		//Obtener el usuario logueado
		HttpSession session = SessionUtilities.getSession();
		String username=(String) session.getAttribute("username");
	    Usuario user = getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username",username).getSingleResult();
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
	
}
