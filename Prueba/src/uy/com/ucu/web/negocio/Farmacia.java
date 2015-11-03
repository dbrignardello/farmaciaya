package uy.com.ucu.web.negocio;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the farmacia database table.
 * 
 */
@Entity
@Table(name="farmacia")
@NamedQueries({
	@NamedQuery(name="Farmacia.findByName", query="SELECT f FROM Farmacia f WHERE f.nombreFarmacia = :nombreFarmacia"),
	@NamedQuery(name="Farmacia.findByNameLikeQuery", query="SELECT f FROM Farmacia f WHERE lower(f.nombreFarmacia) LIKE :nombreFarmacia"),
	@NamedQuery(name="Farmacia.findAll", query="SELECT f FROM Farmacia f")
})

public class Farmacia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idFarmacia;

	private String nombreFarmacia;
	
	private String direccion;
	
	private Integer rating;
	//bi-directional many-to-one association to EntradaInventario
	@OneToMany(mappedBy="farmacia")
	private List<ItemInventario> inventario;

	//bi-directional one-to-one association to Geolocalizacion
	@OneToOne(mappedBy="farmacia")
	private Geolocalizacion geolocalizacion;

	public Farmacia() {
	}
	
	public ItemInventario agregarItemInventario(ItemInventario itemInventario) {
		getInventario().add(itemInventario);
		itemInventario.setFarmacia(this);

		return itemInventario;
	}
	
	public ItemInventario buscarItemInventario(Producto p){
		for (ItemInventario item : getInventario()){
			if (item.getProducto().equals(p)){
				return item;
			}
		}
		return null;			
	}

	public ItemInventario quitarItemInventario(ItemInventario itemInventario) {
		getInventario().remove(itemInventario);
		itemInventario.setFarmacia(null);

		return itemInventario;
	}
	
	public boolean verificarStock(Producto p, int cantidad){
		ItemInventario item = buscarItemInventario(p); 
		return item.verificarStock(cantidad);
	}
	
	public boolean modificarStock(Producto p, int cantidad){
		ItemInventario item = buscarItemInventario(p); 
		return item.modificarStock(cantidad);
	}
	
	public String resultadoStock(Producto p){
		String resultado = "Fuera de stock";
		ItemInventario item = buscarItemInventario(p);
		if (item.hayStock()){
			resultado = "En stock";
		}
		return resultado;
	}
	
	public Integer obtenerStock(Producto p){
		int resultado = 0;
		ItemInventario item = buscarItemInventario(p);
		if (item != null){
			resultado = item.getCantidad();
		}
		return resultado;
	}
	
	//Getters & Setters

	public Geolocalizacion getGeolocalizacion() {
		return this.geolocalizacion;
	}

	public void setGeolocalizacion(Geolocalizacion geolocalizacion) {
		this.geolocalizacion = geolocalizacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getIdFarmacia() {
		return this.idFarmacia;
	}

	public void setIdFarmacia(int idFarmacia) {
		this.idFarmacia = idFarmacia;
	}

	public String getNombreFarmacia() {
		return this.nombreFarmacia;
	}

	public void setNombreFarmacia(String nombreFarmacia) {
		this.nombreFarmacia = nombreFarmacia;
	}

	public List<ItemInventario> getInventario() {
		return this.inventario;
	}

	public void setInventario(List<ItemInventario> inventario) {
		this.inventario = inventario;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}