package uy.com.ucu.web.utilities;
import org.json.*;

public class ParserUtilities {
	
	public JSONObject parseJSONGeolocation (String geolocationString){
		String processedString = geolocationString.substring(1, geolocationString.length()-1);
		return new JSONObject(processedString);
	}

}
