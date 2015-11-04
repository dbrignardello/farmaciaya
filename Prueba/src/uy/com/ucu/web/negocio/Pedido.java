package uy.com.ucu.web.negocio;

import java.io.Serializable;
import javax.persistence.*;

import uy.com.ucu.web.backoffice.Usuario;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the pedido database table.
 * 
 */
@Entity
@Table(name="pedido")
@NamedQuery(name="Pedido.findAll", query="SELECT p FROM Pedido p")
public class Pedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idpedido;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private int rating;
	
	private String nombreFarmacia;

	private double total;

	//bi-directional many-to-one association to ProductoPedido
	@OneToMany(mappedBy="pedido")
	private List<ProductoPedido> productoPedidos;
	
	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="fk_usuario")
	private Usuario usuario;

	public Pedido() {
	}

	public int getIdpedido() {
		return this.idpedido;
	}

	public void setIdpedido(int idpedido) {
		this.idpedido = idpedido;
	}

	public Date getFecha() {
		if (this.fecha == null){
			return null;
		}
		return (Date) this.fecha.clone();
	}

	public void setFecha(Date fecha) {
		this.fecha = new Date(fecha.getTime());
	}


	public String getNombreFarmacia() {
		return this.nombreFarmacia;
	}

	public void setNombreFarmacia(String nombreFarmacia) {
		this.nombreFarmacia = nombreFarmacia;
	}

	public double getTotal() {
		return this.total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<ProductoPedido> getProductoPedidos() {
		return this.productoPedidos;
	}

	public void setProductoPedidos(List<ProductoPedido> productoPedidos) {
		this.productoPedidos = productoPedidos;
	}

	public ProductoPedido addProductoPedido(ProductoPedido productoPedido) {
		getProductoPedidos().add(productoPedido);
		productoPedido.setPedido(this);

		return productoPedido;
	}

	public ProductoPedido removeProductoPedido(ProductoPedido productoPedido) {
		getProductoPedidos().remove(productoPedido);
		productoPedido.setPedido(null);

		return productoPedido;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}