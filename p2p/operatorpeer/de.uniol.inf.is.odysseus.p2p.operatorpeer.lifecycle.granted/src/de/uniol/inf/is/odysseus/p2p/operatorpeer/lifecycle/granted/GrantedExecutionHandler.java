package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.granted;

import java.util.ArrayList;
import java.util.List;

import net.jxta.endpoint.Message;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.MessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class GrantedExecutionHandler extends AbstractExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>>{
	private List<GrantedMessageHandler> handlerList = null;
	
	public GrantedExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.GRANTED);
		handlerList = new ArrayList<GrantedMessageHandler>();
	}

	@SuppressWarnings("unchecked")
	public IExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>> clone()  {
		IExecutionHandler<AbstractPeer, MessageSender<PeerGroup,Message,PipeAdvertisement>> handler = new GrantedExecutionHandler();
		handler.setFunction((MessageSender<PeerGroup, Message, PipeAdvertisement>) getPeer().getMessageSender());
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
		for(String sub : getExecutionListenerCallback().getQuery().getSubPlans().keySet()) {
			
			//Fix, da an einer anderen Stelle ein Subplan mehrfahc eingetragen wird.
			boolean createMessageHandler = true;
//			for(GrantedMessageHandler handler : this.handlerList) {
//				
//				
//				
//				
////				if(handler.getQuery().getId().equals(getExecutionListenerCallback().getQuery().getId()) && handler.getQuery().getSubPlans().containsKey(sub)) {
////					createMessageHandler = true;
////				}
//			}
//			if(createMessageHandler) {
				System.out.println("Erzeuge GrantedMessageHandler " +"Granted"+getExecutionListenerCallback().getQuery().getId()+"_"+sub);
				GrantedMessageHandler handl = new GrantedMessageHandler(getExecutionListenerCallback(), "Granted"+getExecutionListenerCallback().getQuery().getId()+"_"+sub, getFunction());
				this.handlerList.add(handl);
				getPeer().registerMessageHandler(handl);
//			}
		}
		
		
		synchronized (this) {
			try {
				this.wait(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		for(GrantedMessageHandler mHandler : this.handlerList) {
//			if(!mHandler.isGranted()) {
//				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
//			}
//		}
		getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
	}

}
