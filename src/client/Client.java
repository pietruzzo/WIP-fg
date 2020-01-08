package client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.flink.api.java.utils.ParameterTool;
import shared.AkkaMessages.LaunchMsg;
import shared.antlr4.InputParser;

import java.io.*;
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

		System.out.println("Client console: \n" +
				"\t q : quit \n" +
				"\t start : launch system \n" +
				"\t (vertex | edge) (insert | update | delete) : SRC, (DST ,), [LABELS ,] timestamp");
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
				try {
					Serializable parsedMessage = InputParser.parse(line);
					if (parsedMessage == null) throw new NullPointerException();
					clientActor.tell(parsedMessage, ActorRef.noSender());
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Parsing error, no message has been sent to Job Manager. Try again");
				}

			}
		}
		scanner.close();
		sys.terminate();
	}

}
