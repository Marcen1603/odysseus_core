package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator;

import java.util.Observable;
import java.util.Observer;

import net.jxta.peer.PeerID;

/**
 * Listener that is informed when LoadBalancing is finished and sends according message.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingFinishedListener implements Observer {
	private final PeerID initiatingPeer;
	private final String pipeId;
	private final ParallelTrackMessageDispatcher dispatcher;
	
	/**
	 * Constructor
	 * @param peerId Peer to inform (initiating Peer)
	 */
	LoadBalancingFinishedListener(ParallelTrackMessageDispatcher dispatcher, PeerID peerId, String pipeId) {
		this.initiatingPeer = peerId;
		this.dispatcher = dispatcher;
		this.pipeId = pipeId;
	}

	@Override
	/**
	 * Called when update is finished. Sends message.
	 */
	public void update(Observable o, Object arg) {
		dispatcher.sendSyncFinished(initiatingPeer,pipeId);
	}
	
	
}
