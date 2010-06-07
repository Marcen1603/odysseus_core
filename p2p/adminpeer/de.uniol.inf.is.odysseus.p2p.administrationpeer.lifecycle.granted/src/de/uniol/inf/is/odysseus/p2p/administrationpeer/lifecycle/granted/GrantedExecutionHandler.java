package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.granted;

import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.MessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class GrantedExecutionHandler extends AbstractExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>> {

	public GrantedExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.GRANTED);
	}
	
	@Override
	public IExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>> clone()  {
		IExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>> handler = new GrantedExecutionHandler();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		handler.setProvidedLifecycle(getProvidedLifecycle());
		return handler;
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
//		
//		Thread t = new Thread(handler);
//		t.start();
//		while(true) {
		synchronized (this) {
			try {
				this.wait(5000);
				getPeer().deregisterMessageHandler(handler);
			} catch (InterruptedException e) {
				e.printStackTrace();
				getPeer().deregisterMessageHandler(handler);
			}
		}
//			if(!t.isAlive()) {
//				getPeer().deregisterMessageHandler(handler);
//				break;
//			}
//		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setPeer(AbstractPeer peer) {
		super.setPeer(peer);
		setFunction((MessageSender<PeerGroup, Message, PipeAdvertisement>) getPeer().getMessageSender());
//		Method[] methods = peer.getClass().getMethods();
//		for(Method m : methods) {
//			if(m.getReturnType().toString().equals("interface de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.IMessageSender")) {
//				try {
//					setFunction((MessageSender<PeerGroup,Message,PipeAdvertisement>) m.invoke((Object[])null));
//					break;
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
}
