package shared;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class PropertyHandler {

    static private PropertyHandler propertyHandler;
    static public AtomicInteger processes = new AtomicInteger(0);

    private final String DEFAULT_PROP_LOCATION1 = "config.properties";
    private final String DEFAULT_PROP_LOCATION2 = "src/shared/resources/config.properties";

    private Properties prop;
    private StringBuilder performanceLog;


    public static String getProperty(String name) throws IOException {

        synchronized (processes) {
            if (propertyHandler == null) {
                propertyHandler = new PropertyHandler();
            }
        }
        return propertyHandler.prop.getProperty(name);

    }

    public static void writeOnPerformanceLog(String msg) {
        if ( Boolean.parseBoolean(propertyHandler.prop.getProperty("timingLog")) ) {
            writeOnLog(msg);
        }
    }

    public static void writeSpacePerformanceLog(String msg) {

        try {
            if  (!Boolean.parseBoolean(getProperty("spaceLog"))){
                return;
            }
            long pid = ProcessHandle.current().pid();
            Runtime runtime = Runtime.getRuntime();


            System.out.println("launching GC: " + msg + " " + Utils.getUsedMemoryMB());
            Process pr1 = runtime.exec("jcmd " + pid + " GC.run");
            pr1.waitFor();
            System.out.println("finished GC: " + msg + " " + Utils.getUsedMemoryMB());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized static void writeOnLog(String msg) {
        propertyHandler.performanceLog.append(msg).append("\n");
    }

    private PropertyHandler() throws IOException {

        prop = new Properties();
        InputStream inputStream = null;

        try{
            inputStream = new FileInputStream(Utils.getJarFolder()+DEFAULT_PROP_LOCATION1);
        } catch (FileNotFoundException ignored) {
        }


        if (inputStream == null) {

            System.out.println("config.properties not found on: " + DEFAULT_PROP_LOCATION1);

            inputStream = new FileInputStream(DEFAULT_PROP_LOCATION2);

            if (inputStream == null) {
                throw new IOException("config.properties not found on " + Utils.getJarFolder()+DEFAULT_PROP_LOCATION1 + " an not found on " + DEFAULT_PROP_LOCATION2);
            }
        }

        prop.load(inputStream);
        inputStream.close();
        Utils.disableWarning();
        performanceLog = new StringBuilder(50000);
        performanceLog.append("___OPENING_PEFORMANCE_LOG___\n");

    }

    private static void finalizeLog(){

        propertyHandler.performanceLog.append("___CLOSING_PERFORMANCE_LOG___\n");

        try {
            File file = new File(PropertyHandler.getProperty("logPath") + PropertyHandler.getProperty("performanceLogName") + Utils.getLocalIP().replaceAll("\\.", "_") + ".log");
            FileWriter writer = new FileWriter(file, Boolean.parseBoolean(PropertyHandler.getProperty("appendLog")));
            writer.write(propertyHandler.performanceLog.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exit() {
        if (processes.decrementAndGet() == 0) {
            PropertyHandler.finalizeLog();
            System.exit(0);
        }
    }
}
