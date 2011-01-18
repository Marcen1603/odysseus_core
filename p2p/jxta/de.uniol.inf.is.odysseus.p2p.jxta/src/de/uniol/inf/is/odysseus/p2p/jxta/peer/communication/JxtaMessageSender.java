package de.uniol.inf.is.odysseus.p2p.jxta.peer.communication;

import net.jxta.endpoint.Message;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;

public class JxtaMessageSender implements IMessageSender<PeerGroup, Message, PipeAdvertisement> {

	@Override
	public void sendMessage(PeerGroup network, Message message, PipeAdvertisement to) {
		MessageTool.sendMessage(network, to, message);
	}

}
