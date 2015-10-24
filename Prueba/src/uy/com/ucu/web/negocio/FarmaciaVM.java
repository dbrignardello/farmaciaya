package uy.com.ucu.web.negocio;

public class FarmaciaVM {
	private Farmacia farmacia;
	private Double distancia;
	
	public FarmaciaVM (Farmacia f, Double d){
		this.farmacia = f;
		this.distancia = d;
	}
	public Farmacia getFarmacia() {
		return farmacia;
	}
	public void setFarmacia(Farmacia farmacia) {
		this.farmacia = farmacia;
	}
	public Double getDistancia() {
		return distancia;
	}
	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}
}
