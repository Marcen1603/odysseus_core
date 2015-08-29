package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.common;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;

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
