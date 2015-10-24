package uy.com.ucu.web.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import uy.com.ucu.web.negocio.Farmacia;
import uy.com.ucu.web.negocio.ItemCarrito;
import uy.com.ucu.web.negocio.ItemInventario;
import uy.com.ucu.web.negocio.Producto;

@ManagedBean(name="carrito")
@SessionScoped
public class CarritoBean {

    private List<ItemCarrito> itemsCarrito = new ArrayList<ItemCarrito>();

    public void agregarAlCarrito(Producto p, Farmacia f, int c) {
    	
    	int cantidadFinal = c;
    	
    	ItemInventario itemInventario = f.buscarItemInventario(p);
    	
    	ItemCarrito itemCarrito = productoExisteEnElCarrito(p, f);
    	
    	if (itemCarrito == null){
    		itemCarrito = new ItemCarrito();
    		itemCarrito.setFarmacia(f);
        	itemCarrito.setProducto(p);
        	itemCarrito.setPrecio(itemInventario.getPrecio());
        	itemsCarrito.add(itemCarrito);	
    	}else{
    		cantidadFinal = itemCarrito.getCantidad() + c;
    	}
    	
    	itemCarrito.setCantidad(cantidadFinal);
    	    	
    }
    
    public ItemCarrito productoExisteEnElCarrito(Producto p, Farmacia f){
    	for (ItemCarrito item : itemsCarrito){
    		if (item.getProducto().equals(p)){
    			if (item.getFarmacia().equals(f)){
    				return item;
    			}
    		}    			
    	}
    	return null;
    }

    public void quitarDelCarrito(ItemCarrito item) {
    	itemsCarrito.remove(item);
    }
    
    public int obtenerCantidadItemsCarrito(){
    	int tamanio = 0;
    	for (ItemCarrito item : itemsCarrito){
    		tamanio += item.getCantidad(); 
    	}
    	return tamanio;
    }

	public double calcularMontoTotal(){
		double resultado = 0;
		for (ItemCarrito item : itemsCarrito){
			resultado += item.calcularMonto();
		}
		return resultado;
	}
	
	public String realizarCompra(){
		//Deberiamos utilizar esta bandera para saber si actualizar la base de datos o no
		boolean compraExitosa = true;
		boolean hayStock;
		
		//Verificar stock de farmacia
		Iterator<ItemCarrito> iterador = getItemsCarrito().iterator();
		ItemCarrito item;
		
		while (iterador.hasNext()){
			item = iterador.next();
			hayStock = item.getFarmacia().verificarStock(item.getProducto(), item.getCantidad());
			if (!hayStock){
				//Remover items que ya no se pueden comprar
				iterador.remove();
			}
			compraExitosa = compraExitosa && hayStock;
		}
		
		//Modificar stock y vaciar carrito
		if (compraExitosa){
			iterador = getItemsCarrito().iterator();
			while (iterador.hasNext()){
				item = iterador.next();
				item.getFarmacia().modificarStock(item.getProducto(), item.getCantidad()*-1);		
				iterador.remove();
			}
			//Desplegar mensaje de exito y volver a home
			return "home.xhtml?faces-redirect=true";
		}else{
			//Ocurrio un error, volver a cargar el carrito con mensaje de error y sin los items problematicos
			return "Carrito.xhtml?faces-redirect=true";
		}
	}
	
	//Getters & Setters

	public List<ItemCarrito> getItemsCarrito() {
		return itemsCarrito;
	}

	public void setItemsCarrito(List<ItemCarrito> itemsCarrito) {
		this.itemsCarrito = itemsCarrito;
	}
	
}