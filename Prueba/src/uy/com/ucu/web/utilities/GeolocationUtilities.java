package uy.com.ucu.web.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import uy.com.ucu.web.negocio.Farmacia;
import uy.com.ucu.web.negocio.Geolocalizacion;

public class GeolocationUtilities {
	
	private String geolocationService = "http://nominatim.openstreetmap.org/";
	private String format = "json";
	
	private ParserUtilities pu = new ParserUtilities();
	
	// HTTP GET request
	public String pedidoGeolocacion(String address) {

		try{			
			String url = geolocationService + "search?" + "q=" + address.replace(" ", "%20") + "&format=" + format;
			System.out.println(url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			

			//add request header
			//con.setRequestProperty("User-Agent", USER_AGENT);

			//int responseCode = con.getResponseCode();
			//System.out.println("\nSending 'GET' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return(response.toString());
			
		}catch(Exception e){
			
		}
		
		return "";
	}
	
	public Double coordenadaDeGeolocacion(String geolocationResponse, String type){
		
		JSONObject processedGeolocationResponse = pu.parseJSONGeolocation(geolocationResponse);
		
		if (type.equals("latitud")){
			return Double.parseDouble(processedGeolocationResponse.getString("lat"));
		}else if (type.equals("longitud")){
			return Double.parseDouble(processedGeolocationResponse.getString("lon"));
		}
		return null;
	}
	
	public Double distanciaEntreUbicacionYFarmacia(Geolocalizacion g, Farmacia f){
		return calculateDistance(g.getLatitud(), g.getLongitud(), f.getGeolocalizacion().getLatitud(), f.getGeolocalizacion().getLongitud(), "K");		
	}
	
	//De aca para abajo obtenido de: http://www.geodatasource.com/developers/java
	
	public double calculateDistance(double lat1, double lon1, double lat2, double lon2, String unit) {
			double theta = lon1 - lon2;
			double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
			dist = Math.acos(dist);
			dist = rad2deg(dist);
			dist = dist * 60 * 1.1515;
			if (unit == "K") {
				dist = dist * 1.609344;
			} else if (unit == "N") {
				dist = dist * 0.8684;
			}

			return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
}
