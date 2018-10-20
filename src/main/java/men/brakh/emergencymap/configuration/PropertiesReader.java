package men.brakh.emergencymap.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для чтения файлов с конфигурацией
 */
public class PropertiesReader {
    private String filename;


    public PropertiesReader(String filename) {
        this.filename = filename;
    }

    /**
     * Чтение ресурсов с конфигурацией
     * @param propName Название свойства
     * @return Значение свойства
     * @throws IOException
     */

    public String read(String propName) throws IOException {
        String result;
        InputStream inputStream = null;

        try {
            Properties prop = new Properties();

            inputStream = getClass().getClassLoader().getResourceAsStream(filename);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + filename + "' not found in the classpath");
            }


            String property = prop.getProperty(propName);

            System.out.println( "PROP " + propName + " : " + property);
            result = property;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            inputStream.close();
        }
        return result;
    }

}
