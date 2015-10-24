package uy.com.ucu.web.beans;

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

import org.hibernate.Criteria;
import org.primefaces.context.RequestContext;

import sun.java2d.pipe.AATextRenderer;
import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Farmacia;
import uy.com.ucu.web.negocio.FarmaciaVM;
import uy.com.ucu.web.negocio.ItemInventario;
import uy.com.ucu.web.utilities.GeolocationUtilities;
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean(name="farmacia")
@SessionScoped
public class FarmaciaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Farmacia farmacia;
	private List<FarmaciaVM> farmaciasUsuario;
	private List<Farmacia> farmacias;	
	
	private List<ItemInventario> busquedaReciente; 
	
	private String valorBusqueda;
	private EntityManager entityManager;
	
	private Usuario usuario;

	public FarmaciaBean() {
		farmaciasUsuario = new ArrayList<>();
		HttpSession session = SessionUtilities.getSession();
        String username=(String) session.getAttribute("username");
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());	
		beginTransaction();
		try{
			//Obtener usuario logueado y todas las farmacias
			this.setUsuario(getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username",username).getSingleResult());
			this.setFarmacias(getEntityManager().createNamedQuery("Farmacia.findAll", Farmacia.class).getResultList());			
			
		}catch(Exception e){
			
		}
		
		endTransaction();
		
	}
	
	public void recargarInventario(){
		//Recargo datos de farmacia, entre ellos el inventario.
				setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());	
				beginTransaction();
				try{
					
					this.setFarmacia(getEntityManager().createNamedQuery("Farmacia.findByName", Farmacia.class).setParameter("nombreFarmacia", this.farmacia.getNombreFarmacia()).getSingleResult());
				}catch(Exception e){
					
				}
				endTransaction();
	}
	public String buscar(){
		recargarInventario();
			//Obtener productos similares al buscado
			List<ItemInventario> resultado = new ArrayList<>();
			for (ItemInventario itemInventario : farmacia.getInventario()) {
				String nombreProducto = itemInventario.getProducto().getNombre();
				String nombreLower = nombreProducto.toLowerCase();
				if(nombreLower.contains(getValorBusqueda().toLowerCase())){
					resultado.add(itemInventario);
				}
			}
			//Seteo al bean un nuevo inventario segun la busqueda, que luego, al llamarse nuevamente 
			//a buscar será reinicializado segun la BDD.
			this.farmacia.setInventario(resultado);
		return null;
	}
	
	public String borrarBusqueda(){
		//Recargo nuevamente los datos del inventario original de la farmacia, sobreescribiendo el inventario
		//de busqueda 
		recargarInventario();
		return null;
	}

	public void beginTransaction(){
		getEntityManager().getTransaction().begin();
	}
	
	public void endTransaction(){
		getEntityManager().getTransaction().commit();
	}
	
	public String seleccionarFarmacia(Farmacia farmacia){
		setFarmacia(farmacia);
		return "Farmacia.xhtml?faces-redirect=true";
	}
	
	//Getters & setters
	
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
	
	public Farmacia getFarmacia() {
		return farmacia;
	}

	public void setFarmacia(Farmacia farmacia) {
		this.farmacia = farmacia;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public List<ItemInventario> getBusquedaReciente() {
		return busquedaReciente;
	}

	public void setBusquedaReciente(List<ItemInventario> busquedaReciente) {
		this.busquedaReciente = busquedaReciente;
	}

}