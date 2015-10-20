package uy.com.ucu.web.negocio;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CarritoBean {
	
	private String flag = "no add";

    private List<ItemCarrito> itemsCarrito = new ArrayList<ItemCarrito>();

    public void agregarAlCarrito(ItemCarrito item) {
    	if (item == null){
    		ItemCarrito ic = new ItemCarrito();
    		Producto p = new Producto();
    		p.setNombre("Aspirina");
    		Farmacia f = new Farmacia();
    		f.setNombreFarmacia("Farmashop");
    		ic.setCantidad(1);
    		ic.setFarmacia(f);
    		ic.setPrecio(100);
    		ic.setProducto(p);
    		item = ic;
    	}
    	this.flag = "add";
    	itemsCarrito.add(item);
    }

    public void quitarDelCarrito(ItemCarrito item) {
    	itemsCarrito.remove(item);
    }
    
    public int obtenerCantidadItemsCarrito(){
    	return getItemsCarrito().size();
    }

	public List<ItemCarrito> getItemsCarrito() {
		return itemsCarrito;
	}

	public void setItemsCarrito(List<ItemCarrito> itemsCarrito) {
		this.itemsCarrito = itemsCarrito;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}