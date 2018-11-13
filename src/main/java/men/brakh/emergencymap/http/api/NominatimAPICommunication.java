package men.brakh.emergencymap.http.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import men.brakh.emergencymap.configuration.PropertiesReader;
import men.brakh.emergencymap.http.APICommunication;
import men.brakh.emergencymap.http.HttpInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для "Общения" с апи Nominatim
 */
@Component
public class NominatimAPICommunication implements APICommunication {
    private String host;
    private String key;
    private HttpInteraction httpInteraction;

    private final static String propertiesFile = "nominatim.properties";

    private final static Logger logger = LoggerFactory.getLogger(NominatimAPICommunication.class);

    public NominatimAPICommunication() {
        PropertiesReader propertiesReader = new PropertiesReader(propertiesFile);
        try {
            host = propertiesReader.read("host");
            key = propertiesReader.read("key");
        } catch (IOException e) {
            logger.error("Error reading file with properties for working with Nominatim api");
        }
        httpInteraction = new HttpInteraction();
    }

    /**
     * Формирование URL запроса
     * @param params параметры url запроса
     * @return сформированный url запрос
     */
    public String formationRequest(Map<String, String> params) {
        String initialFormat = "%s?key=%s&format=json&limit=1";
        String paramFormat = "&%s=%s";

        String initialUrl = String.format(initialFormat, host, key);
        StringBuilder url = new StringBuilder(initialUrl);

        for(Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            String urlParam = String.format(paramFormat, key, value);
            url.append(urlParam);
        }

        return url.toString();


    }


    /**
     * Получение полигона региона из Nominatim API
     * @param region название региона
     * @return полигон региона
     */
    public String getCoords(String region) {
        region = region.replaceAll(" ", "%20");

        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("polygon_geojson", "1");
        additionalParams.put("q", region);
        String strurl = formationRequest(additionalParams);

        logger.info("[GETTING COORDS] Request sent to third party server: " + strurl);

        try {
            String result = httpInteraction.sendGet(strurl);

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
            logger.error("Error while processing the request for receiving coordinates" , e);
        }

        logger.error("Entered bad region : " + region);
        throw new InvalidParameterException("Bad region :(");
    }

    /**
     * Получение количество жителей региона из Nominatim API
     * @param region Название региона
     * @return количество жителей региона
     */
    public long getPopulation(String region) {
        region = region + ", Беларусь";
        region = region.replaceAll(" ", "%20");

        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("polygon", "0");
        additionalParams.put("q", region);
        additionalParams.put("extratags", "1");

        String strurl = formationRequest(additionalParams);
        logger.info("[GETTING POPULATION] Request sent to third party server: " + strurl);

        try {
            String result = httpInteraction.sendGet(strurl);

            JsonParser parser = new JsonParser();

            JsonArray arr = parser.parse(result).getAsJsonArray();
            JsonObject mainObject = (JsonObject) arr.get(0);

            JsonObject extratags = mainObject.getAsJsonObject("extratags");
            if(extratags.getAsJsonPrimitive("population") != null) {
                return extratags.getAsJsonPrimitive("population").getAsLong();
            } else {
                logger.error("Nomination has no data on the population of " + region +". This region is set to -1");
                System.out.println(region);
                return -1;
            }


        } catch (Exception e) {
            logger.error("Error while processing the request for receiving population" , e);
        }

        logger.error("Entered bad region : " + region);
        throw new InvalidParameterException("Bad region :(");
    }

}
