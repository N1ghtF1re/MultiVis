package men.brakh.emergencymap.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Класс для "Общения" с апи Nominatim
 */
public class NominatimCommunication {
    String host;
    String key;
    HttpInteraction httpInteraction;

    public NominatimCommunication() {
        try {
            host = getProperties("host");
            key = getProperties("key");
        } catch (IOException e) {
            new RuntimeException(e);
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

        System.out.println(url);
        return url.toString();


    }


    /**
     * Получение полигона региона из Nominatim API
     * @param region название региона
     * @return полигон региона
     */
    public String getCoordsFromNominatim(String region) {
        region = region.replaceAll(" ", "%20");

        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("polygon_geojson", "1");
        additionalParams.put("q", region);
        String strurl = formationRequest(additionalParams);

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
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Получение количество жителей региона из Nominatim API
     * @param region Название региона
     * @return количество жителей региона
     */
    public long getPopulationFromNominatim(String region) {
        region = region.replaceAll(" ", "%20");

        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("polygon", "0");
        additionalParams.put("q", region);
        additionalParams.put("extratags", "1");

        String strurl = formationRequest(additionalParams);

        try {
            String result = httpInteraction.sendGet(strurl);

            JsonParser parser = new JsonParser();

            JsonArray arr = parser.parse(result).getAsJsonArray();
            JsonObject mainObject = (JsonObject) arr.get(0);

            JsonObject extratags = mainObject.getAsJsonObject("extratags");
            return extratags.getAsJsonPrimitive("population").getAsLong();

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new InvalidParameterException("Bad region :(");
    }

    /**
     * Чтение ресурсов с конфигурацией АПИ Nominatim (key, host, ...)
     * @param propName Название свойства
     * @return Значение свойства
     * @throws IOException
     */
    public String getProperties(String propName) throws IOException {
        String result = "";
        InputStream inputStream = null;

        try {
            Properties prop = new Properties();
            String propFileName = "nominatim.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }


            String property = prop.getProperty(propName);

            System.out.println( "PROP " + propName + " : " + property);
            result = property;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }

}
