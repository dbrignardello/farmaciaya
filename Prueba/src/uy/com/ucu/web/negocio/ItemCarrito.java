package uy.com.ucu.web.negocio;

public class ItemCarrito {
	
	Farmacia farmacia;
	Producto producto;
	double precio;
	int cantidad;
	
	public double calcularMonto(){
		return cantidad*precio;
	}
	
	//Getters & Setters
	
	public Farmacia getFarmacia() {
		return farmacia;
	}
	
	public void setFarmacia(Farmacia farmacia) {
		this.farmacia = farmacia;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}	
	
}
