package slave;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.flink.api.java.utils.ParameterTool;

import java.io.File;

public class TaskManager {

	public static void main(String[] args) {
		final ParameterTool param = ParameterTool.fromArgs(args);
		final String name = param.get("name");
		final int numWorkers = param.getInt("numWorkers", 1);
		final String jobManagerAddr = param.get("jobManagerAddr", "127.0.0.1");
		final int jobManagerPort = param.getInt("jobManagerPort", 6123);
		final String configFile = param.get("config", "conf/taskmanager.conf");

		final String jobManager = "akka.tcp://JobManager@" + jobManagerAddr + ":" + jobManagerPort + "/user/JobManager";

		final Config conf = ConfigFactory.parseFile(new File(configFile));
		final ActorSystem sys = ActorSystem.create(name, conf);
		sys.actorOf(TaskManagerActor.props(name, numWorkers, jobManager), name);
	}
}
