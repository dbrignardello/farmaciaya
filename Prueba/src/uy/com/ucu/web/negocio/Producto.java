package uy.com.ucu.web.negocio;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Producto database table.
 * 
 */
@Entity
@Table(name="Producto")
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idProducto;

	private String nombre;

	//bi-directional many-to-one association to EntradaInventario
	@OneToMany(mappedBy="producto")
	private List<ItemInventario> entradaInventarios;

	public Producto() {
	}

	public int getIdProducto() {
		return this.idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<ItemInventario> getEntradaInventarios() {
		return this.entradaInventarios;
	}

	public void setEntradaInventarios(List<ItemInventario> entradaInventarios) {
		this.entradaInventarios = entradaInventarios;
	}

	public ItemInventario addEntradaInventario(ItemInventario entradaInventario) {
		getEntradaInventarios().add(entradaInventario);
		entradaInventario.setProducto(this);

		return entradaInventario;
	}

	public ItemInventario removeEntradaInventario(ItemInventario entradaInventario) {
		getEntradaInventarios().remove(entradaInventario);
		entradaInventario.setProducto(null);

		return entradaInventario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Producto other = (Producto) obj;
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		return true;
	}

}