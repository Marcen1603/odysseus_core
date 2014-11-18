package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Interface for a Listener which handles failed Messages in P2P network.
 * 
 * @author Carsten Cordes
 *
 */
public interface IMessageDeliveryFailedListener {

	/***
	 * Called when Message delivery fails.
	 * 
	 * @param message
	 *            failed message
	 * @param peerID
	 *            destination peer.
	 */
	public void update(IMessage message, PeerID peerID);
}
