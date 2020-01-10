import client.Client;
import master.JobManager;
import slave.TaskManager;

public class DebugMain {

    public static void main(String[] args) {

        JobManager.main(new String[0]);
        TaskManager.main(new String[0]);
        TaskManager.main(new String[0]);
        TaskManager.main(new String[0]);
        TaskManager.main(new String[0]);
        Client.main(new String[0]);
    }
}
