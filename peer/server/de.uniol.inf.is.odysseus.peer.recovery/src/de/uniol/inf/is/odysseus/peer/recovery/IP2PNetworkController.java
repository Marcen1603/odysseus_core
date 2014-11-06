package de.uniol.inf.is.odysseus.peer.recovery;

import net.jxta.peer.PeerID;

/**
 * The P2P network controller provides services to detect if any peer enters or
 * leaves the network.
 * 
 * @author Simon Kuespert & Michael Brand
 * 
 */
public interface IP2PNetworkController {

	/**
	 * Listener will be notified, if any peer enters or leaves the network.
	 * 
	 * @author Michael Brand
	 *
	 */
	public interface IP2PNetworkControllerListener {

		/**
		 * Notification, that a new peer entered the network.
		 * 
		 * @param peer
		 *            The ID of the peer. <br />
		 *            Must be not null.
		 */
		public void onPeerEntered(PeerID peer);

		/**
		 * Notification, that a new peer leaved the network.
		 * 
		 * @param peer
		 *            The ID of the peer. <br />
		 *            Must be not null.
		 */
		public void onPeerLeaved(PeerID peer);

	}

	/**
	 * Starts the detection of entered or left peers.
	 */
	public void start();

	/**
	 * Stops the detection of entered or left peers.
	 */
	public void stop();

}
