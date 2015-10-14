package uy.com.ucu.web.negocio;

import java.util.HashMap;

public class Inventario {
	
	//JPA?
	private int id;
	
	//Relaciona nombre de producto con cantidad disponible
	private HashMap<String, EntradaInventario> productos;
	
	public Inventario(){
		this.productos = new HashMap<String, EntradaInventario>();
	}
	
	//Empieza a trackear un producto en el stock, sin precio y con 0 de cantidad
	//Verdadero si es exitoso, falso sino
	public boolean registrarProducto(Producto producto){
		
		String nombreProducto = producto.getNombre();
		
		if (this.productos.containsKey(nombreProducto)){
			return false;
		}
		
		EntradaInventario entrada = new EntradaInventario (0, null);
		this.productos.put(nombreProducto, entrada);
		return true;
	}
	
	//Deja de trackear un producto en el stock
	//Verdadero si es exitoso, falso sino
	public boolean eliminarProducto(Producto producto){
		
		String nombreProducto = producto.getNombre();
		
		if (!this.productos.containsKey(nombreProducto)){
			return false;
		}
		this.productos.remove(nombreProducto);
		return true;
	}
	
	//Verdadero si es exitoso, falso sino
	public boolean modificarStock(String nombreProducto, int movimiento){
		
		//El movimiento tiene que ser positivo o negativo
		if (movimiento != 0){			
			//El producto debe existir en el stock
			if (productos.containsKey(nombreProducto)){				
				EntradaInventario entrada = productos.get(nombreProducto);
				
				int cantidadDisponible = entrada.getCantidad();
				int stockFinal = cantidadDisponible + movimiento;
				
				//No se pueden remover más de los que hay
				if (stockFinal > 0){
					entrada.setCantidad(stockFinal);
					return true;
				}							
			}
		}		
		return false;
	}
	
	//Verdadero si es exitoso, falso sino
	public boolean modificarPrecio(String nombreProducto, Double nuevoPrecio){
		
		//El nuevo precio debe ser mayor a $0
		if (nuevoPrecio > 0){			
			//El producto debe existir en el stock
			if (productos.containsKey(nombreProducto)){				
				EntradaInventario entrada = productos.get(nombreProducto);
				entrada.setPrecio(nuevoPrecio);
				return true;			
			}
		}		
		return false;
	}
	
	public Integer obtenerStock(String nombreProducto){
		if (productos.containsKey(nombreProducto)){
			return(productos.get(nombreProducto).getCantidad());
		}
		return null;
	}
	
	public Double obtenerPrecio(String nombreProducto){
		if (productos.containsKey(nombreProducto)){
			return(productos.get(nombreProducto).getPrecio());
		}
		return null;
	}


}
