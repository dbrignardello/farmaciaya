package uy.com.ucu.web.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;

import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Farmacia;
import uy.com.ucu.web.negocio.ItemCarrito;
import uy.com.ucu.web.negocio.ItemInventario;
import uy.com.ucu.web.negocio.Producto;
import uy.com.ucu.web.utilities.MailUtilities;
import uy.com.ucu.web.utilities.SecurityUtilities;
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean(name="carrito")
@SessionScoped
public class CarritoBean {

    private List<ItemCarrito> itemsCarrito = new ArrayList<ItemCarrito>();

    private EntityManager entityManager;
	private SecurityUtilities securityUtilities;
	private SessionUtilities sessionUtilities;
	
	public CarritoBean(){		
		setSecurityUtilities(new SecurityUtilities());
		setSessionUtilities(new SessionUtilities());
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());		
	}
    
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

	public Double calcularMontoTotal(){
		Double resultado = (double) 0;
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
		
		//String para email
		String listadoCarrito = "";
		String montoTotal = this.calcularMontoTotal().toString();
		
		while (iterador.hasNext()){
			item = iterador.next();
			hayStock = item.getFarmacia().verificarStock(item.getProducto(), item.getCantidad());
			if (!hayStock){
				//Remover items que ya no se pueden comprar
				iterador.remove();
			}
			compraExitosa = compraExitosa && hayStock;
			
			//Email
			listadoCarrito += "<li>" + item.getProducto().getNombre() + " (" + item.getCantidad().toString() + ") </li>";
		}
		
		//Modificar stock y vaciar carrito
		if (compraExitosa){
			iterador = getItemsCarrito().iterator();
			while (iterador.hasNext()){
				item = iterador.next();
				item.getFarmacia().modificarStock(item.getProducto(), item.getCantidad()*-1);		
				iterador.remove();
			}
			
			HttpSession session = SessionUtilities.getSession();
	        String username=(String) session.getAttribute("username");
	        Usuario user = getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username",username).getSingleResult();
						
			MailUtilities.send(
	                "login", "farmaciayaing3@gmail.com",
	                "password", "putoelquelee",
	                "to", user.getEmail(),
	                "subject", "Tu compra en FarmaciaYa!",
	                "body", "<h1>Hola " + user.getNombreCompleto() + ":</h1><p>Gracias por comprar en FarmaciaYa! <br /><br />"
	                		+ "Se enviará a la dirección " + user.getDireccion() + ": <br />"
	                		+ "<ul>" + listadoCarrito + "</ul>"
	                		+ "por el monto total de $" + montoTotal + ". <br />"	                		
	                		+ "</p>"
	        );
			
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
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
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
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
}