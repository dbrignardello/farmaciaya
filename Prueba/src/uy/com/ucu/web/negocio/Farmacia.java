package uy.com.ucu.web.negocio;

public class Farmacia {

	//JPA?
	private int id;
	
	private String nombre;
	private Geolocacion direccion;
	
	private Inventario stock;
	
	public Farmacia(){
		this.stock = new Inventario();
	}
	
	//Ver comentarios de Inventario.java
	//Farmacia no es mas que un wrapper de esos métodos + geolocación

	public boolean registrarProducto(Producto producto){
		return(stock.registrarProducto(producto));
	}
	
	public boolean eliminarProducto(Producto producto){
		return(stock.eliminarProducto(producto));
	}
	
	public boolean modificarStock(String producto, int movimiento){
		return(stock.modificarStock(producto, movimiento));
	}
	
	public boolean modificarPrecio(String producto, Double nuevoPrecio){
		return(stock.modificarPrecio(producto, nuevoPrecio));
	}
	
	public int obtenerStock(String producto){
		return(stock.obtenerStock(producto));
	}
	
	public Double obtenerPrecio(String producto){
		return(stock.obtenerPrecio(producto));
	}	

	public Inventario getStock() {
		return stock;
	}

	public void setStock(Inventario stock) {
		this.stock = stock;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Geolocacion getDireccion() {
		return direccion;
	}

	public void setDireccion(Geolocacion direccion) {
		this.direccion = direccion;
	}
	
}
