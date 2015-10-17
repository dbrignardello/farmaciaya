package uy.com.ucu.web.negocio;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the entradaInventario database table.
 * 
 */
@Entity
@Table(name="entradaInventario")
@NamedQuery(name="EntradaInventario.findAll", query="SELECT e FROM EntradaInventario e")
public class EntradaInventario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idEntradaInventario;

	private int cantidad;

	private double precio;

	//bi-directional many-to-one association to Farmacia
	@ManyToOne
	@JoinColumn(name="fk_farmacia")
	private Farmacia farmacia;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="fk_producto")
	private Producto producto;

	public EntradaInventario() {
	}

	public int getIdEntradaInventario() {
		return this.idEntradaInventario;
	}

	public void setIdEntradaInventario(int idEntradaInventario) {
		this.idEntradaInventario = idEntradaInventario;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio() {
		return this.precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Farmacia getFarmacia() {
		return this.farmacia;
	}

	public void setFarmacia(Farmacia farmacia) {
		this.farmacia = farmacia;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}