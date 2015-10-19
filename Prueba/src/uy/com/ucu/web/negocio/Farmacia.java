package uy.com.ucu.web.negocio;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the farmacia database table.
 * 
 */
@Entity
@Table(name="farmacia")
@NamedQueries({
	@NamedQuery(name="Farmacia.findByName", query="SELECT f FROM Farmacia f WHERE f.nombreFarmacia = :nombreFarmacia"),
	@NamedQuery(name="Farmacia.findByNameLikeQuery", query="SELECT f FROM Farmacia f WHERE lower(f.nombreFarmacia) LIKE :nombreFarmacia"),
	@NamedQuery(name="Farmacia.findAll", query="SELECT f FROM Farmacia f")
})

public class Farmacia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idFarmacia;

	private String nombreFarmacia;
	
	private String direccion;
	//bi-directional many-to-one association to EntradaInventario
	@OneToMany(mappedBy="farmacia")
	private List<EntradaInventario> entradaInventarios;

	//bi-directional one-to-one association to Geolocalizacion
	@OneToOne(mappedBy="farmacia")
	private Geolocalizacion geolocalizacion;

	public Farmacia() {
	}

	public int getIdFarmacia() {
		return this.idFarmacia;
	}

	public void setIdFarmacia(int idFarmacia) {
		this.idFarmacia = idFarmacia;
	}

	public String getNombreFarmacia() {
		return this.nombreFarmacia;
	}

	public void setNombreFarmacia(String nombreFarmacia) {
		this.nombreFarmacia = nombreFarmacia;
	}

	public List<EntradaInventario> getEntradaInventarios() {
		return this.entradaInventarios;
	}

	public void setEntradaInventarios(List<EntradaInventario> entradaInventarios) {
		this.entradaInventarios = entradaInventarios;
	}

	public EntradaInventario addEntradaInventario(EntradaInventario entradaInventario) {
		getEntradaInventarios().add(entradaInventario);
		entradaInventario.setFarmacia(this);

		return entradaInventario;
	}

	public EntradaInventario removeEntradaInventario(EntradaInventario entradaInventario) {
		getEntradaInventarios().remove(entradaInventario);
		entradaInventario.setFarmacia(null);

		return entradaInventario;
	}

	public Geolocalizacion getGeolocalizacion() {
		return this.geolocalizacion;
	}

	public void setGeolocalizacion(Geolocalizacion geolocalizacion) {
		this.geolocalizacion = geolocalizacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

}