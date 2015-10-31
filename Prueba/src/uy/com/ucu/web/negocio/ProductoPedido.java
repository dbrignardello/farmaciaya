package uy.com.ucu.web.negocio;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the productoPedido database table.
 * 
 */
@Entity
@Table(name="productoPedido")
@NamedQuery(name="ProductoPedido.findAll", query="SELECT p FROM ProductoPedido p")
public class ProductoPedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idproductoPedido;

	private int cantidad;

	private String nombreProducto;
	
	

	//bi-directional many-to-one association to Pedido
	@ManyToOne
	@JoinColumn(name="fk_pedido")
	private Pedido pedido;

	public ProductoPedido() {
	}

	public int getIdproductoPedido() {
		return this.idproductoPedido;
	}

	public void setIdproductoPedido(int idproductoPedido) {
		this.idproductoPedido = idproductoPedido;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getNombreProducto() {
		return this.nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public Pedido getPedido() {
		return this.pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

}