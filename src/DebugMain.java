import client.Client;
import master.JobManager;
import shared.PropertyHandler;
import shared.Utils;
import slave.TaskManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class DebugMain {

    public static void main(String[] args) throws IOException {

        //Write standard output on file
        if (Boolean.parseBoolean(PropertyHandler.getProperty("outOnFile"))) {
            // Creating a File object that represents the disk file.
            PrintStream o = new PrintStream(new File(PropertyHandler.getProperty("logPath") + "output"+ Utils.getLocalIP() +".txt"));

            System.setOut(o);

        }

        if (Utils.getLocalIP().equals(PropertyHandler.getProperty("masterIp")) || PropertyHandler.getProperty("masterIp").equals("127.0.0.1")) {
            JobManager.main(new String[0]);
            Client.main(new String[0]);
            PropertyHandler.processes.set(2);
        }

        int numOfWorkers = Integer.parseInt(PropertyHandler.getProperty("numOfWorkers"));
        PropertyHandler.processes.set(PropertyHandler.processes.intValue() + numOfWorkers);
        for (int i = 0; i < numOfWorkers; i++) {
            //Process pr1 = Runtime.getRuntime().exec("java -jar /home/pietro/Desktop/slave.jar ");
            TaskManager.main(new String[0]);
        }


    }
}
