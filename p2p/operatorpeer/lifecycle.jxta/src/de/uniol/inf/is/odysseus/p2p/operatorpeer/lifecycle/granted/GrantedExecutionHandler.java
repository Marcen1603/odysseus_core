package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.granted;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class GrantedExecutionHandler extends AbstractExecutionHandler<JxtaMessageSender>{

	static Logger logger = LoggerFactory.getLogger(GrantedExecutionHandler.class);
		
	private List<GrantedMessageHandler> handlerList = null;
	
	public GrantedExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.GRANTED);
		handlerList = new ArrayList<GrantedMessageHandler>();
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
		for(String sub : getExecutionListenerCallback().getQuery().getSubPlans().keySet()) {
			
			//Fix, da an einer anderen Stelle ein Subplan mehrfahc eingetragen wird.
//			boolean createMessageHandler = true;
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
				logger.debug("Erzeuge GrantedMessageHandler " +"Granted"+getExecutionListenerCallback().getQuery().getId()+"_"+sub);
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
