import client.Client;
import master.JobManager;
import shared.PropertyHandler;
import shared.Utils;
import slave.TaskManager;

import java.io.IOException;

public class DebugMain {

    public static void main(String[] args) throws IOException {

        if (Utils.getLocalIP().equals(PropertyHandler.getProperty("masterIp")) || PropertyHandler.getProperty("masterIp").equals("127.0.0.1")) {
            JobManager.main(new String[0]);
            Client.main(new String[0]);
            PropertyHandler.processes.set(3);
        }

        TaskManager.main(new String[0]);

    }
}
