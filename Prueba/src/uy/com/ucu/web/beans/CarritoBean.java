package uy.com.ucu.web.beans;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;

import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Farmacia;
import uy.com.ucu.web.negocio.ItemCarrito;
import uy.com.ucu.web.negocio.ItemInventario;
import uy.com.ucu.web.negocio.Pedido;
import uy.com.ucu.web.negocio.Producto;
import uy.com.ucu.web.negocio.ProductoPedido;
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
	
	private Producto productoAComprar;
	private Farmacia farmaciaAComprar;
	private int stockMaximo;
	private int cantidadAComprar;
	

	
	public CarritoBean(){		
		setSecurityUtilities(new SecurityUtilities());		
		setSessionUtilities(new SessionUtilities());

		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());		
	}
	
	public boolean carritoVacio(){
		return itemsCarrito.isEmpty();
	}
    
	public boolean carritoNoVacio(){
		return (!itemsCarrito.isEmpty());
	}
	
	public Integer obtenerStock(){
		Integer retorno;
		if (this.getFarmaciaAComprar() == null || this.getProductoAComprar() == null){
			retorno = 0;
		}else{
			retorno = this.getFarmaciaAComprar().obtenerStock(this.getProductoAComprar());
		}
		return retorno;
	}
	
    public void agregarAlCarrito() {
    	
    	if (cantidadAComprar > 0){
    		int cantidadFinal = cantidadAComprar;
        	
        	ItemInventario itemInventario = farmaciaAComprar.buscarItemInventario(productoAComprar);
        	
        	ItemCarrito itemCarrito = productoExisteEnElCarrito(productoAComprar, farmaciaAComprar);
        	
        	if (itemCarrito == null){
        		itemCarrito = new ItemCarrito();
        		itemCarrito.setFarmacia(farmaciaAComprar);
            	itemCarrito.setProducto(productoAComprar);
            	itemCarrito.setPrecio(itemInventario.getPrecio());
            	itemsCarrito.add(itemCarrito);	
        	}else{
        		cantidadFinal = itemCarrito.getCantidad() + cantidadAComprar;
        	}
        	    	
        	itemCarrito.setCantidad(cantidadFinal);
        	this.cantidadAComprar = 0;
    	}
    	
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
    
    public Integer obtenerStockMaximo(){
    	return getFarmaciaAComprar().obtenerStock(productoAComprar);
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
		String listadoCarrito;
		String montoTotal = this.calcularMontoTotal().toString();
		
		//Obtener el usuario: para agregar sus pedidos realizados y enviar el mail correspondiente.
		HttpSession session = SessionUtilities.getSession();
        String username=(String) session.getAttribute("username");
        Usuario user = getEntityManager().createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username",username).getSingleResult();
        Usuario usuario = getEntityManager().find(Usuario.class, user.getIdusuario());
		
        StringBuffer buf = new StringBuffer();
		while (iterador.hasNext()){
			item = iterador.next();
			hayStock = item.getFarmacia().verificarStock(item.getProducto(), item.getCantidad());
			if (!hayStock){
				//Remover items que ya no se pueden comprar
				iterador.remove();
			}
			compraExitosa = compraExitosa && hayStock;
			
			//Email
			buf.append("<li>" + item.getProducto().getNombre() + " (" + item.getCantidad().toString() + ") </li>");
		}
		
		listadoCarrito = buf.toString();
		
		//Modificar stock, vaciar carrito y registrar pedido
		if (compraExitosa){
			
			Farmacia f;
			
			beginTransaction();
			
			iterador = getItemsCarrito().iterator();
			List <ProductoPedido> productosPedidos = new ArrayList<ProductoPedido>();
			Pedido p = new Pedido();
			p.setFecha(new Date());
			Double total = 0.0;
			while (iterador.hasNext()){
				item = iterador.next();
				f = item.getFarmacia();
				f = getEntityManager().find(Farmacia.class, f.getIdFarmacia());
				f.modificarStock(item.getProducto(), item.getCantidad()*-1);
				
				//Registrar el pedido
				
				//Crear producto pedido a partir del producto y el itemcarrito
				ProductoPedido pp = new ProductoPedido();
				pp.setCantidad(item.getCantidad());
				pp.setNombreProducto(item.getProducto().getNombre());
				pp.setPedido(p);
				
				//Agrego al pedido el productoPedido creado, incremento el total de compra
				p.setNombreFarmacia(f.getNombreFarmacia());
				total += item.calcularMonto();
				productosPedidos.add(pp);
				
				iterador.remove();
			}
			//Seteo los productosPedido del pedido nuevo y su total
			p.setProductoPedidos(productosPedidos);
			p.setTotal(total);
			
			//Aca ya tengo en el Pedido p todos los ProductoPedido agregados a su lista
			for (ProductoPedido productoPedido : p.getProductoPedidos()) {
				productoPedido.setPedido(p);
				getEntityManager().persist(productoPedido);
			}
			
			getEntityManager().persist(p);
			
			p.setUsuario(usuario);
			List<Pedido> pedidosUsuario = usuario.getPedidos();
			pedidosUsuario.add(p);
			usuario.setPedidos(pedidosUsuario);
			
			endTransaction();					
						
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Compra realizada.", "Se ha enviado un resumen de compras a su dirección de e-mail."));
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			//Desplegar mensaje de exito y volver a home
			return "home.xhtml?faces-redirect=true";
		}else{
			//Ocurrio un error, volver a cargar el carrito con mensaje de error y sin los items problematicos
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "Algunos de los productos seleccionados ya no están disponibles y se han removido del carrito."));
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			return "Carrito.xhtml?faces-redirect=true";
		}
	}
	
	public void beginTransaction(){
		getEntityManager().getTransaction().begin();
	}
	
	public void endTransaction(){
		getEntityManager().getTransaction().commit();
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

	public Producto getProductoAComprar() {
		return productoAComprar;
	}

	public void setProductoAComprar(Producto productoAComprar) {
		this.productoAComprar = productoAComprar;
	}

	public Farmacia getFarmaciaAComprar() {
		return farmaciaAComprar;
	}

	public void setFarmaciaAComprar(Farmacia farmaciaAComprar) {
		this.farmaciaAComprar = farmaciaAComprar;
	}

	public int getCantidadAComprar() {
		return cantidadAComprar;
	}

	public void setCantidadAComprar(int cantidadAComprar) {
		this.cantidadAComprar = cantidadAComprar;
	}
	
	public int getStockMaximo() {
		return stockMaximo;
	}

	public void setStockMaximo(int stockMaximo) {
		this.stockMaximo = stockMaximo;
	}

	
}