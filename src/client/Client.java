package client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.flink.api.java.utils.ParameterTool;
import shared.AkkaMessages.HelloClientMsg;
import shared.AkkaMessages.LaunchMsg;
import shared.AkkaMessages.TerminateMsg;
import shared.PropertyHandler;
import shared.Utils;
import shared.antlr4.InputParser;

import java.io.*;
import java.util.*;


public class Client {

	public static void main(String[] args) throws IOException {
		final ParameterTool param = ParameterTool.fromArgs(args);

		final String configFile = param.get("config", "conf/client.conf");
		final String jobManagerAddr = PropertyHandler.getProperty("masterIp");
		final int jobManagerPort = Integer.parseInt(PropertyHandler.getProperty("masterPort"));

		final String jobManager = "akka.tcp://JobManager@" + jobManagerAddr + ":" + jobManagerPort + "/user/JobManager";

		final Config conf = ConfigFactory.parseFile(new File(configFile));
		final ActorSystem sys = ActorSystem.create("Client", conf);
		final ActorRef clientActor = sys.actorOf(ClientActor.props(jobManager), "Client");
		clientActor.tell(new HelloClientMsg(), ActorRef.noSender());

		try {
			if (PropertyHandler.getProperty("autonomousMode").equals("true")){
				automaticMode(clientActor);
			} else {
				manualMode(clientActor);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientActor.tell(new TerminateMsg(), ActorRef.noSender());
	}

	private static void manualMode(ActorRef clientActor) {
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
	}

	private static void automaticMode(ActorRef clientActor) throws IOException {

		int numRecords = Integer.parseInt(PropertyHandler.getProperty("numRecordsAsInput"));
		long numTotalRecords = Utils.countLines(PropertyHandler.getProperty("datasetPath"));
		try {

			BufferedReader lineReader = new BufferedReader(new FileReader(PropertyHandler.getProperty("datasetPath")));

			for (int i = 0; i < (numTotalRecords-numRecords); i++) {
				lineReader.readLine();
			}

			for (int i = (int)(numTotalRecords-numRecords); i < numTotalRecords; i++) {
				String line = lineReader.readLine();
				Serializable parsedMessage = InputParser.parse(line);
				if (parsedMessage == null) throw new NullPointerException();
				clientActor.tell(parsedMessage, ActorRef.noSender());

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Parsing error, no message has been sent to Job Manager. Try again");
		}
	}

}
