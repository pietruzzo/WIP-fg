package client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.commons.math3.geometry.spherical.twod.Edge;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import shared.Vertex;
import shared.VertexState;

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
			/* TO BE IMPLEMENTED */
			// Quit
			if (line.equals("start")) {
				clientActor.tell(new StartMsg(), ActorRef.noSender());
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

			// Add vertex
			else if (line.startsWith("addVertex")) {
				final AddVertexMsg msg = new AddVertexMsg(getVertexFromString(line), System.currentTimeMillis());
				clientActor.tell(msg, ActorRef.noSender());
			}

			// Add edge
			else if (line.startsWith("addEdge")) {
				final AddEdgeMsg msg = new AddEdgeMsg(getEdgeFromString(line), System.currentTimeMillis());
				clientActor.tell(msg, ActorRef.noSender());
			}

			// Update vertex
			else if (line.startsWith("updateVertex")) {
				final UpdateVertexMsg msg = new UpdateVertexMsg(getVertexFromString(line), System.currentTimeMillis());
				clientActor.tell(msg, ActorRef.noSender());
			}

			// Delete vertex
			else if (line.startsWith("delVertex")) {
				final DelVertexMsg msg = new DelVertexMsg(getVertexFromString(line), System.currentTimeMillis());
				clientActor.tell(msg, ActorRef.noSender());
			}

			// Delete edge
			else if (line.startsWith("delEdge")) {
				final DelEdgeMsg msg = new DelEdgeMsg(getEdgeFromString(line), System.currentTimeMillis());
				clientActor.tell(msg, ActorRef.noSender());
			}

		}
		scanner.close();
		sys.terminate();
	}


	private static final Vertex getVertexFromString(String s, long timestamp) {
		final StringTokenizer t = new StringTokenizer(s, " ");
		t.nextToken();
		final String name = t.nextToken();
		final VertexState state = new VertexState();
		while (t.hasMoreTokens()) {
			final String prop = t.nextToken();
			state.addToState(prop.split("=")[0], prop.split("=")[1], timestamp);
		}
		return new Vertex(name, state);
	}

	private static final Pair getEdgeFromString(String s, long timestamp) {
		final StringTokenizer t = new StringTokenizer(s, " ");
		t.nextToken();
		final String source = t.nextToken();
		final String destination = t.nextToken();
		return new Pair<String, String>(source, destination);
	}

}
