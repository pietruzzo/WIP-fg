package master;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.flink.api.java.utils.ParameterTool;
import shared.PropertyHandler;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class JobManager {

	public static void main(String[] args) {
		final ParameterTool param = ParameterTool.fromArgs(args);
		final String configFile = param.get("config", "conf/jobmanager.conf");

		final Config conf = ConfigFactory.parseFile(new File(configFile));
		final ActorSystem sys = ActorSystem.create("JobManager", conf);
		sys.actorOf(JobManagerActor.props(), "JobManager");
	}
}
