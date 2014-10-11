package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public interface IMessageDeliveryFailedListener {

	public void update(IMessage message, PeerID peerID);
}
