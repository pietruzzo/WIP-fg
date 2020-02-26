package slave;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.apache.flink.api.java.utils.ParameterTool;
import shared.PropertyHandler;
import shared.Utils;

import java.io.File;
import java.io.IOException;

public class TaskManager {

	public static void main(String[] args) throws IOException {
		final ParameterTool param = ParameterTool.fromArgs(args);
		final String name = param.get("name", "slave");
		//final int numWorkers = param.getInt("numWorkers", Integer.parseInt(PropertyHandler.getProperty("numThreads")));
		final String jobManagerAddr = PropertyHandler.getProperty("masterIp");
		final int jobManagerPort = Integer.parseInt(PropertyHandler.getProperty("masterPort"));
		final String configFile = param.get("config", Utils.getAkkaConfPath("taskmanager.conf"));

		final String jobManager = "akka.tcp://JobManager@" + jobManagerAddr + ":" + jobManagerPort + "/user/JobManager";

		Config conf = ConfigFactory.parseFile(new File(configFile));
		conf = conf.withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(Utils.getLocalIP()));
		final ActorSystem sys = ActorSystem.create(name, conf);
		if (Utils.waitConnection(jobManagerAddr, jobManagerPort, null)) {
			sys.actorOf(TaskManagerActor.props(name, jobManager), name);
		} else {
			throw new RuntimeException("Unable to reach Job manager @ " + jobManagerAddr +", " + jobManagerPort);
		}
	}
}
