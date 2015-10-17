package uy.com.ucu.web.negocio;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the geolocalizacion database table.
 * 
 */
@Entity
@NamedQuery(name="Geolocalizacion.findAll", query="SELECT g FROM Geolocalizacion g")
@Table(name="geolocalizacion")
public class Geolocalizacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idFarmacia;

	private double latitud;

	private double longitud;

	//bi-directional one-to-one association to Farmacia
	@OneToOne
	@JoinColumn(name="idFarmacia")
	private Farmacia farmacia;

	public Geolocalizacion() {
	}
	public Geolocalizacion(Double latitud, Double longitud){
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public int getIdFarmacia() {
		return this.idFarmacia;
	}

	public void setIdFarmacia(int idFarmacia) {
		this.idFarmacia = idFarmacia;
	}

	public double getLatitud() {
		return this.latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return this.longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public Farmacia getFarmacia() {
		return this.farmacia;
	}

	public void setFarmacia(Farmacia farmacia) {
		this.farmacia = farmacia;
	}

}