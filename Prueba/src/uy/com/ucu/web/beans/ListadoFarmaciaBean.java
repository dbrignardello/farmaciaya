package uy.com.ucu.web.beans;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Farmacia;
import uy.com.ucu.web.negocio.FarmaciaVM;
import uy.com.ucu.web.negocio.Geolocalizacion;
import uy.com.ucu.web.utilities.GeolocationUtilities;
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean(name="listadoFarmacia") 
@SessionScoped
public class ListadoFarmaciaBean implements Serializable {
	private static final long serialVersionUID = 1L;


	private List<FarmaciaVM> farmaciasUsuario;
	private List<Farmacia> farmacias;
	private EntityManager entityManager;
	
	private Usuario usuario;
	private List<Double> distanciasToDisplay;
	private String valorBusqueda;
	
	public ListadoFarmaciaBean() {
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
		
		/*
		 * Calcular distancias usuario-farmacias, ordenar la lista de farmacias según cercanía
		 */
		//Obtener Geolocalizacion del Usuario
		
		Map<Double, Farmacia> distancias = new TreeMap<Double, Farmacia>();
		GeolocationUtilities gu = new GeolocationUtilities();

		String ubicacionUser =gu.pedidoGeolocacion(this.getUsuario().getDireccion());
		Double latUser = gu.coordenadaDeGeolocacion(ubicacionUser, "latitud");
		Double longUser = gu.coordenadaDeGeolocacion(ubicacionUser, "longitud");
		Geolocalizacion geolocacionUser = new Geolocalizacion(latUser, longUser);
		for (Farmacia farmacia : this.getFarmacias()) {
			String direccionFarmacia = farmacia.getDireccion();
			String ubicacionFarmacia = gu.pedidoGeolocacion(direccionFarmacia);
			Double latitudFarmacia = gu.coordenadaDeGeolocacion(ubicacionFarmacia, "latitud");
			Double longitudFarmacia = gu.coordenadaDeGeolocacion(ubicacionFarmacia, "longitud");
			Geolocalizacion geolocacion = new Geolocalizacion(latitudFarmacia, longitudFarmacia);
			farmacia.setGeolocalizacion(geolocacion);
			
			Double distancia = gu.distanciaEntreUbicacionYFarmacia(geolocacionUser, farmacia);
			distancias.put(distancia, farmacia);
			
			
		}
		List<Farmacia> aux = new ArrayList<Farmacia>();
		for (Farmacia farmacia : distancias.values()) {
			aux.add(farmacia);
		}
		this.setFarmacias(aux);
		
		List<Double> aux1 = new ArrayList<Double>();
		for(Double d : distancias.keySet()){
			aux1.add(d);
		}
		this.setDistanciasToDisplay(aux1);
		
		for (Farmacia f : this.getFarmacias()) {
			Double distanciaFarmacia = this.distanciasToDisplay.get(this.getFarmacias().indexOf(f));
			
		
			Double distanciaFormateada = (Double)((int)(distanciaFarmacia*100)/100.0);
			
			FarmaciaVM faux = new FarmaciaVM(f, distanciaFormateada);
			farmaciasUsuario.add(faux);
		}

	}

	
	public String buscar(){
		//Reinicializar variables
		setFarmaciasUsuario(new ArrayList<>());
		setFarmacias(new ArrayList<>());
		setDistanciasToDisplay(new ArrayList<>());
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());	
		String valor = valorBusqueda;
		beginTransaction();
		try{
			//Obtener usuario logueado y todas las farmacias
			this.setFarmacias(getEntityManager().createQuery("SELECT f FROM Farmacia f WHERE UPPER(f.nombreFarmacia) LIKE :nombreFarmacia").setParameter("nombreFarmacia", "%" + valor.toUpperCase() + "%").getResultList());
			//this.setFarmacias(getEntityManager().createNamedQuery("Farmacia.findByNameLikeQuery").setParameter("nombreFarmacia",valor).getResultList());	
			
		}catch(Exception e){
			
		}

		endTransaction();
		/*
		 * Calcular distancias usuario-farmacias, ordenar la lista de farmacias según cercanía
		 */
		//Obtener Geolocalizacion del Usuario
		
		Map<Double, Farmacia> distancias = new TreeMap<Double, Farmacia>();
		GeolocationUtilities gu = new GeolocationUtilities();

		String ubicacionUser =gu.pedidoGeolocacion(this.getUsuario().getDireccion());
		Double latUser = gu.coordenadaDeGeolocacion(ubicacionUser, "latitud");
		Double longUser = gu.coordenadaDeGeolocacion(ubicacionUser, "longitud");
		Geolocalizacion geolocacionUser = new Geolocalizacion(latUser, longUser);
		for (Farmacia farmacia : this.getFarmacias()) {
			String direccionFarmacia = farmacia.getDireccion();
			String ubicacionFarmacia = gu.pedidoGeolocacion(direccionFarmacia);
			Double latitudFarmacia = gu.coordenadaDeGeolocacion(ubicacionFarmacia, "latitud");
			Double longitudFarmacia = gu.coordenadaDeGeolocacion(ubicacionFarmacia, "longitud");
			Geolocalizacion geolocacion = new Geolocalizacion(latitudFarmacia, longitudFarmacia);
			farmacia.setGeolocalizacion(geolocacion);
			
			Double distancia = gu.distanciaEntreUbicacionYFarmacia(geolocacionUser, farmacia);
			distancias.put(distancia, farmacia);
			
			
		}
		List<Farmacia> aux = new ArrayList<Farmacia>();
		for (Farmacia farmacia : distancias.values()) {
			aux.add(farmacia);
		}
		this.setFarmacias(aux);
		
		List<Double> aux1 = new ArrayList<Double>();
		for(Double d : distancias.keySet()){
			aux1.add(d);
		}
		this.setDistanciasToDisplay(aux1);
		
		for (Farmacia f : this.getFarmacias()) {
			Double distanciaFarmacia = this.distanciasToDisplay.get(this.getFarmacias().indexOf(f));
			
		
			Double distanciaFormateada = (Double)((int)(distanciaFarmacia*100)/100.0);
			
			FarmaciaVM faux = new FarmaciaVM(f, distanciaFormateada);
			farmaciasUsuario.add(faux);
		}
		return null;
	}
	
	public String borrarBusqueda(){
		//Reinicializar variables
		setFarmaciasUsuario(new ArrayList<>());
		setFarmacias(new ArrayList<>());
		setDistanciasToDisplay(new ArrayList<>());
		
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());	
		beginTransaction();
		try{
			this.setFarmacias(getEntityManager().createNamedQuery("Farmacia.findAll", Farmacia.class).getResultList());			
			
		}catch(Exception e){
			
		}
		
		endTransaction();
		
		/*
		 * Calcular distancias usuario-farmacias, ordenar la lista de farmacias según cercanía
		 */
		//Obtener Geolocalizacion del Usuario
		
		Map<Double, Farmacia> distancias = new TreeMap<Double, Farmacia>();
		GeolocationUtilities gu = new GeolocationUtilities();

		String ubicacionUser =gu.pedidoGeolocacion(this.getUsuario().getDireccion());
		Double latUser = gu.coordenadaDeGeolocacion(ubicacionUser, "latitud");
		Double longUser = gu.coordenadaDeGeolocacion(ubicacionUser, "longitud");
		Geolocalizacion geolocacionUser = new Geolocalizacion(latUser, longUser);
		for (Farmacia farmacia : this.getFarmacias()) {
			String direccionFarmacia = farmacia.getDireccion();
			String ubicacionFarmacia = gu.pedidoGeolocacion(direccionFarmacia);
			Double latitudFarmacia = gu.coordenadaDeGeolocacion(ubicacionFarmacia, "latitud");
			Double longitudFarmacia = gu.coordenadaDeGeolocacion(ubicacionFarmacia, "longitud");
			Geolocalizacion geolocacion = new Geolocalizacion(latitudFarmacia, longitudFarmacia);
			farmacia.setGeolocalizacion(geolocacion);
			
			Double distancia = gu.distanciaEntreUbicacionYFarmacia(geolocacionUser, farmacia);
			distancias.put(distancia, farmacia);
			
			
		}
		List<Farmacia> aux = new ArrayList<Farmacia>();
		for (Farmacia farmacia : distancias.values()) {
			aux.add(farmacia);
		}
		this.setFarmacias(aux);
		
		List<Double> aux1 = new ArrayList<Double>();
		for(Double d : distancias.keySet()){
			aux1.add(d);
		}
		this.setDistanciasToDisplay(aux1);
		
		for (Farmacia f : this.getFarmacias()) {
			Double distanciaFarmacia = this.distanciasToDisplay.get(this.getFarmacias().indexOf(f));
			
		
			Double distanciaFormateada = (Double)((int)(distanciaFarmacia*100)/100.0);
			
			FarmaciaVM faux = new FarmaciaVM(f, distanciaFormateada);
			farmaciasUsuario.add(faux);
		}
		return null;
	}
	
	public void beginTransaction(){
		getEntityManager().getTransaction().begin();
	}
	
	public void endTransaction(){
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

	public void setDistanciasToDisplay(List<Double> distancias) {
		this.distanciasToDisplay = distancias;
	}

	public List<FarmaciaVM> getFarmaciasUsuario() {
		return farmaciasUsuario;
	}

	public void setFarmaciasUsuario(List<FarmaciaVM> farmaciasUsuario) {
		this.farmaciasUsuario = farmaciasUsuario;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

}