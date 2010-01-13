package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.granted;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.MessageSender;
import net.jxta.endpoint.Message;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;

public class GrantedExecutionHandler extends AbstractExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>> {

	public GrantedExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.GRANTED);
	}
	
	@Override
	public IExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>> clone() throws CloneNotSupportedException {
		IExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>> handler = new GrantedExecutionHandler();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		return handler;
	}

	@Override
	public String getName() {
		return "GrantedExecutionHandler";
	}

	@Override
	public void run() {
		GrantedMessageHandler handler = new GrantedMessageHandler(getExecutionListenerCallback().getQuery(), getExecutionListenerCallback());
		getPeer().registerMessageHandler(handler);
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		//Sende Anfrage an die bestätigten Peers
		messageElements.put("queryId", getExecutionListenerCallback().getQuery().getId());
//		messageElements.put("peerId", PeerGroupTool.getPeerGroup().getPeerID().toString());

		messageElements.put("pipeAdvertisement", (PipeAdvertisement)getPeer().getServerResponseAddress());
		for(Subplan s : getExecutionListenerCallback().getQuery().getSubPlans().values()) {
			if(s.getBiddings().get(0) instanceof BidJxtaImpl) {
				messageElements.put("subplanId", s.getId());
				getFunction().sendMessage(PeerGroupTool.getPeerGroup(), MessageTool
						.createSimpleMessage(handler.getInterestedNamespace(), messageElements), ((BidJxtaImpl)s.getBiddings().get(0)).getResponseSocket());
			}
			
		}
		
		Thread t = new Thread(handler);
		t.start();
		while(true) {
			try {
				this.wait(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!t.isAlive()) {
				getPeer().deregisterMessageHandler(handler);
				break;
			}
		}
	}

}
