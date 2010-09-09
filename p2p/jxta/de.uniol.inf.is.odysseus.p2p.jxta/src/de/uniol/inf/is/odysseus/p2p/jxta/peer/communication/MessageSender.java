package de.uniol.inf.is.odysseus.p2p.jxta.peer.communication;

import net.jxta.endpoint.Message;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;

public class MessageSender<N extends PeerGroup,M extends Message, R extends PipeAdvertisement> implements IMessageSender<N, M, R> {

	@Override
	public void sendMessage(N network, M message, R to) {
		MessageTool.sendMessage(network, to, message);
	}

}
