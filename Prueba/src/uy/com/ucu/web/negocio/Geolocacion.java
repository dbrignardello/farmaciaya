package uy.com.ucu.web.negocio;

public class Geolocacion {
	
	//JPA ?
	private int id;
	private double latitud;
	private double longitud;
	
	public Geolocacion(){
		
	}
	
	public Geolocacion(Double latitud, Double longitud){
		this.latitud = latitud;
		this.longitud = longitud;
	}
	
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	

}
