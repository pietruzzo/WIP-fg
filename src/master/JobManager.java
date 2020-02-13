package master;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.apache.flink.api.java.utils.ParameterTool;
import shared.Utils;

import java.io.File;

public class JobManager {

	public static void main(String[] args) {
		final ParameterTool param = ParameterTool.fromArgs(args);
		final String configFile = param.get("config", Utils.getAkkaConfPath("jobmanager.conf"));

		Config conf = ConfigFactory.parseFile(new File(configFile));
		conf = conf.withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(Utils.getLocalIP()));
		final ActorSystem sys = ActorSystem.create("JobManager", conf);
		sys.actorOf(JobManagerActor.props(), "JobManager");
	}
}
