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
	private List<EntradaInventario> entradaInventarios;

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

	public List<EntradaInventario> getEntradaInventarios() {
		return this.entradaInventarios;
	}

	public void setEntradaInventarios(List<EntradaInventario> entradaInventarios) {
		this.entradaInventarios = entradaInventarios;
	}

	public EntradaInventario addEntradaInventario(EntradaInventario entradaInventario) {
		getEntradaInventarios().add(entradaInventario);
		entradaInventario.setProducto(this);

		return entradaInventario;
	}

	public EntradaInventario removeEntradaInventario(EntradaInventario entradaInventario) {
		getEntradaInventarios().remove(entradaInventario);
		entradaInventario.setProducto(null);

		return entradaInventario;
	}

}