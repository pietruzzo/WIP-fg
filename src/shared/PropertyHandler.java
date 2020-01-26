package shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHandler {

    static private PropertyHandler propertyHandler;

    private final String DEFAULT_PROP_LOCATION1 = "./config.properties";
    private final String DEFAULT_PROP_LOCATION2 = "src/shared/resources/config.properties";

    private Properties prop;


    public static String getProperty(String name) throws IOException {

        if (propertyHandler == null) {
            propertyHandler = new PropertyHandler();
        }
        return propertyHandler.prop.getProperty(name);

    }

    private PropertyHandler() throws IOException {

        prop = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP_LOCATION1);

        if (inputStream == null) {

            System.out.println("config.properties not found on: " + DEFAULT_PROP_LOCATION1);

            inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP_LOCATION2);

            if (inputStream == null) {
                throw new IOException("config.properties not found on " + DEFAULT_PROP_LOCATION1 + " an not found on " + DEFAULT_PROP_LOCATION2);
            }
        }

        prop.load(inputStream);
        inputStream.close();

    }

}
