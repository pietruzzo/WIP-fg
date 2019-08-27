package client;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


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
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder(). //
		    //match(StartMsg.class, this::onStartMsg). //
		    //match(InstallComputationMsg.class, this::onInstallComputationMsg). //
		    //match(ChangeGraphMsg.class, this::onUpdateGraphMsg). //
		    build();

	}

	/**
	 * Message processing
	 */

	/*
	private final void onStartMsg(StartMsg msg) {
		log.info("StartMsg");
		jobManager.tell(msg, self());
	}

	private final void onInstallComputationMsg(InstallComputationMsg msg) {
		log.info("InstallComputation");
		jobManager.tell(msg, self());
	}

	private final void onUpdateGraphMsg(ChangeGraphMsg msg) {
		log.info(msg.toString());
		jobManager.tell(msg, self());
	}
*/
	static final Props props(String jobManagerAddr) {
		return Props.create(ClientActor.class, jobManagerAddr);
	}
}
