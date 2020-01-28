package shared;

import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class PropertyHandler {

    static private PropertyHandler propertyHandler;

    private final String DEFAULT_PROP_LOCATION1 = "./config.properties";
    private final String DEFAULT_PROP_LOCATION2 = "src/shared/resources/config.properties";

    private Properties prop;
    private ExecutorService writeExecutor;


    public static String getProperty(String name) throws IOException {

        if (propertyHandler == null) {
            propertyHandler = new PropertyHandler();
        }
        return propertyHandler.prop.getProperty(name);

    }

    public static void writeOnPerformanceLog(String msg) {
        if ( Boolean.parseBoolean(propertyHandler.prop.getProperty("timingLog")) ) {
            writeAsyncOnLog(msg);
        }
    }

    public static void writeSpacePerformanceLog(String msg) {
        if ( Boolean.parseBoolean(propertyHandler.prop.getProperty("spaceLog")) ) {
            writeAsyncOnLog(msg);
        }
    }

    private static void writeAsyncOnLog(String msg) {
        //Initialize logs
        propertyHandler.initializeLog();

        propertyHandler.writeExecutor.submit( () -> {
            Logger.getLogger(propertyHandler.prop.getProperty("performanceLogName")).info(msg);
        });
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

    private void initializeLog() {
        if (this.writeExecutor == null) {
            try {
                Logger performance = Logger.getLogger(PropertyHandler.getProperty("performanceLogName"));
                performance.addHandler(new FileHandler(PropertyHandler.getProperty("logPath") + PropertyHandler.getProperty("performanceLogName") + ".log", PropertyHandler.getProperty("appendLog").equals("true")));
                performance.info("___OPENING_PEFORMANCE_LOG___");
                writeExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void finalizeLog(){
        propertyHandler.writeExecutor.shutdown();
    }
}
