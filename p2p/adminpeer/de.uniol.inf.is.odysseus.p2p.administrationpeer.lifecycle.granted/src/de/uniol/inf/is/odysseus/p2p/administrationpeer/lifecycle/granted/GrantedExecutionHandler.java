package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.granted;

import java.util.HashMap;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class GrantedExecutionHandler extends AbstractExecutionHandler<JxtaMessageSender> {

	public GrantedExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.GRANTED);
	}
	
	public GrantedExecutionHandler(
			GrantedExecutionHandler grantedExecutionHandler) {
		super(grantedExecutionHandler);
	}

	@Override
	public IExecutionHandler<JxtaMessageSender> clone()  {
		return new GrantedExecutionHandler(this);
	}

	@Override
	public String getName() {
		return "GrantedExecutionHandler";
	}

	@Override
	public void run() {
		synchronized (this) {
			try {
				this.wait(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		GrantedMessageHandler handler = new GrantedMessageHandler(getExecutionListenerCallback().getQuery(), getExecutionListenerCallback(), getPeer());
		handler.setInterestedNamespace("Granted"+getExecutionListenerCallback().getQuery().getId());
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
						.createSimpleMessage("Granted"+getExecutionListenerCallback().getQuery().getId()+"_"+ s.getId(), messageElements), ((BidJxtaImpl)s.getBiddings().get(0)).getResponseSocket());
			}
			
		}

		synchronized (this) {
			try {
				this.wait(5000);
				getPeer().deregisterMessageHandler(handler);
			} catch (InterruptedException e) {
				e.printStackTrace();
				getPeer().deregisterMessageHandler(handler);
			}
		}
	}

	@Override
	public void setPeer(IOdysseusPeer peer) {
		super.setPeer(peer);
		setFunction((JxtaMessageSender) getPeer().getMessageSender());
	}
	
}
