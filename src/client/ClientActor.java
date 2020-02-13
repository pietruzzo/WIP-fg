package client;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import shared.AkkaMessages.FireMsg;
import shared.AkkaMessages.HelloClientMsg;
import shared.AkkaMessages.LaunchMsg;
import shared.AkkaMessages.TerminateMsg;
import shared.PropertyHandler;

import java.io.Serializable;


class ClientActor extends AbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private final String jobManagerAddr;
	private ActorSelection jobManager = null;

	private ClientActor(String jobManagerAddr) {
		this.jobManagerAddr = jobManagerAddr;
	}

	@Override
	public void preStart() throws Exception {
		super.preStart();
		jobManager = getContext().actorSelection(jobManagerAddr);

		if (!Boolean.parseBoolean(PropertyHandler.getProperty("debugLog"))) {
			getContext().getSystem().eventStream().setLogLevel(0);
		}
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder(). //
		match(LaunchMsg.class, this::onStartMsg).
		match(FireMsg.class, this::onFireMsg).
		match(Serializable.class, this::onUpdateGraphMsg).
		match(HelloClientMsg.class, this::onHelloClientMsg).
				match(TerminateMsg.class, this::onTerminateMsg).
		build();

	}

	/**
	 * Message processing
	 */


	private final void onHelloClientMsg(HelloClientMsg msg) {
		log.info("HelloClientMsg");
		jobManager.tell(msg, self());
	}

	private final void onStartMsg(LaunchMsg msg) {
		log.info("StartMsg");
		jobManager.tell(msg, self());
	}

	private final void onTerminateMsg(TerminateMsg msg) {
		log.info("TerminateMsg");
		jobManager.tell(msg, self());
	}

	private final void onFireMsg(FireMsg msg) {
		log.info("FireMsg: " + msg.toString());
	}

	private final void onUpdateGraphMsg(Serializable msg) {
		log.info(msg.toString());
		jobManager.tell(msg, self());
	}

	@Override
	public void postStop() throws Exception {
		PropertyHandler.exit();
		super.postStop();
	}

	static final Props props(String jobManagerAddr) {
		return Props.create(ClientActor.class, jobManagerAddr);
	}
}
