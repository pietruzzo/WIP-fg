package client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.flink.api.java.utils.ParameterTool;
import shared.AkkaMessages.LaunchMsg;
import shared.AkkaMessages.modifyGraph.ModifyGraphMsg;
import shared.Lexer;
import shared.antlr4.InputParser;

import java.io.File;
import java.io.Serializable;
import java.util.*;


public class Client {

	public static void main(String[] args) {
		final ParameterTool param = ParameterTool.fromArgs(args);

		final String configFile = param.get("config", "conf/client.conf");
		final String jobManagerAddr = param.get("jobManagerAddr", "127.0.0.1");
		final int jobManagerPort = param.getInt("jobManagerPort", 6123);

		final String jobManager = "akka.tcp://JobManager@" + jobManagerAddr + ":" + jobManagerPort + "/user/JobManager";

		final Config conf = ConfigFactory.parseFile(new File(configFile));
		final ActorSystem sys = ActorSystem.create("Client", conf);
		final ActorRef clientActor = sys.actorOf(ClientActor.props(jobManager), "Client");

		final Scanner scanner = new Scanner(System.in);
		while (true) {
			final String line = scanner.nextLine();

			// Quit
			if (line.equals("q")) {
				break;
			}
			// Start
			else if (line.equals("start")) {
				clientActor.tell(new LaunchMsg(), ActorRef.noSender());
			}

			// Graph inputs
			else {
				Serializable parsedMessage = InputParser.parse(line);
				clientActor.tell(parsedMessage, ActorRef.noSender());
			}
		}
		scanner.close();
		sys.terminate();
	}

}
