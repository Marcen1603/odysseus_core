package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.Observable;
import java.util.Observer;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import net.jxta.peer.PeerID;

/**
 * Listener that is informed when LoadBalancing is finished and sends according message.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingFinishedListener implements Observer {
	private final int lbProcessId;
	private final PeerID initiatingPeer;
	private final IPeerCommunicator peerCommunicator;
	
	/**
	 * Constructor
	 * @param peerId Peer to inform (initiating Peer)
	 * @param lbProcessId Load Balancing Process Id.
	 */
	LoadBalancingFinishedListener(IPeerCommunicator peerCommunicator, PeerID peerId,int lbProcessId) {
		this.lbProcessId = lbProcessId;
		this.initiatingPeer = peerId;
		this.peerCommunicator = peerCommunicator;
	}

	@Override
	/**
	 * Called when update is finished. Sends message.
	 */
	public void update(Observable o, Object arg) {
		LoadBalancingMessageDispatcher.sendLoadBalancingFinishedMessage(peerCommunicator, initiatingPeer,lbProcessId);
	}
	
	
}
