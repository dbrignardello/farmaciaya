package uy.com.ucu.web.test;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import uy.com.ucu.web.negocio.Farmacia;

import uy.com.ucu.web.negocio.Geolocalizacion;

import uy.com.ucu.web.negocio.Producto;
import uy.com.ucu.web.utilities.GeolocationUtilities;
import uy.com.ucu.web.utilities.ParserUtilities;

public class GeolocationUtilitiesTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testObtenerCoordenadas() {
		GeolocationUtilities gu = new GeolocationUtilities();
		
		String address = "Av. 8 de Octubre 2738, Montevideo, Uruguay";
		
		System.out.println("~~~~~~~~~~");
		String response = gu.pedidoGeolocacion(address);
		System.out.println("raw response:\n" + response);

		Double latitud = gu.coordenadaDeGeolocacion(response, "latitud");
		Double longitud = gu.coordenadaDeGeolocacion(response, "longitud");
		
		System.out.println("latitud:\n" + latitud.toString());
		System.out.println("longitud:\n" + longitud.toString());
		System.out.println("~~~~~~~~~~");
		System.out.println();
	}
	
	@Test
	public void testFarmaciasCercanas() {
		
		GeolocationUtilities gu = new GeolocationUtilities();
		ParserUtilities pu = new ParserUtilities();
		
		//UCU
		String direccionUsuario = "Av. 8 de Octubre 2738, Montevideo, Uruguay";
		String ubicacionUsuario = gu.pedidoGeolocacion(direccionUsuario);
		Double latitudUsuario = gu.coordenadaDeGeolocacion(ubicacionUsuario, "latitud");
		Double longitudUsuario = gu.coordenadaDeGeolocacion(ubicacionUsuario, "longitud");
		Geolocalizacion geolocacionUsuario = new Geolocalizacion(latitudUsuario, longitudUsuario);
		System.out.println("Lat usuario (UCU)");
		System.out.println(latitudUsuario);
		System.out.println("Lon usuario (UCU)");
		System.out.println(longitudUsuario);
		
		//Mi casa
		String direccionFarmacia1 = "General Urquiza 3039, Montevideo, Uruguay";
		String ubicacionFarmacia1 = gu.pedidoGeolocacion(direccionFarmacia1);
		Double latitudFarmacia1 = gu.coordenadaDeGeolocacion(ubicacionFarmacia1, "latitud");
		Double longitudFarmacia1 = gu.coordenadaDeGeolocacion(ubicacionFarmacia1, "longitud");
		Geolocalizacion geolocacion1 = new Geolocalizacion(latitudFarmacia1, longitudFarmacia1);
		System.out.println("Lat Farmacia1 (Casa de Marce)");
		System.out.println(latitudFarmacia1);
		System.out.println("Lon Farmacia1 (Casa de Marce)");
		System.out.println(longitudFarmacia1);
		
		//Nuevocentro
		String direccionFarmacia2 = "Avenida Luis Alberto de Herrera 3469, Montevideo, Uruguay";
		String ubicacionFarmacia2 = gu.pedidoGeolocacion(direccionFarmacia2);
		Double latitudFarmacia2 = gu.coordenadaDeGeolocacion(ubicacionFarmacia2, "latitud");
		Double longitudFarmacia2 = gu.coordenadaDeGeolocacion(ubicacionFarmacia2, "longitud");
		Geolocalizacion geolocacion2 = new Geolocalizacion(latitudFarmacia2, longitudFarmacia2);
		System.out.println("Lat Farmacia2 (Nuevocentro)");
		System.out.println(latitudFarmacia2);
		System.out.println("Lon Farmacia2 (Nuevocentro)");
		System.out.println(longitudFarmacia2);
		
		Farmacia f1 = new Farmacia();
		f1.setGeolocalizacion(geolocacion1);
		
		Farmacia f2 = new Farmacia();
		f2.setGeolocalizacion(geolocacion2);
		
		System.out.println("Distancias");
		System.out.println("Usuario y Farmacia 1 (UCU y casa de Marce)");
		System.out.println(gu.distanciaEntreUbicacionYFarmacia(geolocacionUsuario, f1) + "km");
		System.out.println("Usuario y Farmacia 2 (UCU y Nuevocentro)");
		System.out.println(gu.distanciaEntreUbicacionYFarmacia(geolocacionUsuario, f2) + "km");
		
		
		
	}

}
