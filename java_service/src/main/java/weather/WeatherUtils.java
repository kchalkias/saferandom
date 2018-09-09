package weather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;


public class WeatherUtils {
	private static ObjectMapper objectMapper = new ObjectMapper();
	private static String URL_PREFIX = "http://api.apixu.com/v1/current.json?key=bef045405149488eb49105901162404&q=";
	public static void parseApixu() throws IOException {
		

		URL url = new URL(URL_PREFIX + "Paris");

		WeatherJson weather = objectMapper.readValue(url, WeatherJson.class);
		System.out.println(weather);
	}

}
