package client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.flink.api.java.utils.ParameterTool;
import shared.AkkaMessages.LaunchMsg;
import shared.AkkaMessages.modifyGraph.ModifyGraphMsg;
import shared.Lexer;
import shared.Vertex;

import java.io.File;
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
			if (line.equals("start")) {
				clientActor.tell(new LaunchMsg(), ActorRef.noSender());
			}

			// Modify Graph

			if (line.trim().startsWith("vertex") || line.trim().startsWith("edge")){
				List<ModifyGraphMsg> messages = Lexer.parse(Lexer.lex(line));
				for (ModifyGraphMsg msg: messages) {
					clientActor.tell(msg, ActorRef.noSender());
				}
			}
			/*
			// Install computation
			else if (line.startsWith("install max")) {
				final InstallComputationMsg<Integer, Integer> max = new InstallComputationMsg<>("Max",
				    () -> new MaxIncomingEdges());
				clientActor.tell(max, ActorRef.noSender());
			}

			// Install computation
			else if (line.startsWith("install fastestGrowing")) {
				final InstallComputationMsg<Integer, Integer> max = new InstallComputationMsg<>("FastestGrowing",
				    () -> new MaxDelta());
				clientActor.tell(max, ActorRef.noSender());
			}

			// Install Traingle count
			else if (line.startsWith("install triangleCount")) {
				final InstallComputationMsg<NamesSet, HashSet<HashSet<String>>> tCount = new InstallComputationMsg<>("TraingleCounting",
						() -> new TraingleCounting());
				clientActor.tell(tCount, ActorRef.noSender());
			}

*/

		}
		scanner.close();
		sys.terminate();
	}

}
