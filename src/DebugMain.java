import client.Client;
import master.JobManager;
import slave.TaskManager;

import java.io.IOException;

public class DebugMain {

    public static void main(String[] args) throws IOException {

        JobManager.main(new String[0]);
        TaskManager.main(new String[0]);
        Client.main(new String[0]);
    }
}
