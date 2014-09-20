package de.uniol.inf.is.odysseus.peer.recovery;

import net.jxta.peer.PeerID;

/**
 * A recovery communicator handles the communication between peers for recovery
 * mechanisms.
 * 
 * @author Tobias Brandt & Michael Brand
 *
 */
public interface IRecoveryCommunicator {

	public void recover(PeerID failedPeer);

}