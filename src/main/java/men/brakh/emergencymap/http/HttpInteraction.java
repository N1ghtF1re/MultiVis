package men.brakh.emergencymap;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HttpInteraction {

    private static final String USER_AGENT = "Mozilla/5.0";

    // HTTP GET request
    public String sendGet(String url) throws Exception {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }


        return result.toString();
    }

    public String getCoordsFromNominatim(String region) {


        String strurl = "http://open.mapquestapi.com/nominatim/v1/search.php?key=O0H4lLbv8vYAQQM4leZjroUVK5okAtb3&q=" + region + "&format=json&polygon_geojson=1&limit=1";

        try {
            String result = sendGet(strurl);

            JsonParser parser = new JsonParser();

            JsonArray arr = parser.parse(result).getAsJsonArray();
            JsonObject mainObject = (JsonObject) arr.get(0);

            JsonPrimitive osmType = mainObject.getAsJsonPrimitive("osm_type");

            if(osmType.getAsString().equals("relation")) {

                JsonObject geojson = mainObject.getAsJsonObject("geojson");
                JsonArray coordinates = geojson.getAsJsonArray("coordinates");
                if((coordinates.size() == 1) || (((JsonArray) coordinates.get(1)).size() > 1)) {
                    return coordinates.toString();
                } else {
                    return coordinates.get(0).toString();
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
