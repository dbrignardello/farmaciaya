package uy.com.ucu.web.negocio;

public class EntradaInventario {
	
	//JPA?
	private int id;
	
	private int cantidad;
	private Double precio;
	
	public EntradaInventario(){
		
	}
	
	public EntradaInventario (int cantidad, Double precio){
		this.cantidad = cantidad;
		this.precio = precio;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}

}
