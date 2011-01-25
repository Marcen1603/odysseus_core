package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.granted;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class GrantedExecutionHandler extends AbstractExecutionHandler<JxtaMessageSender>{

	static Logger logger = LoggerFactory.getLogger(GrantedExecutionHandler.class);
		
	final private List<GrantedMessageHandler> handlerList;
	
	public GrantedExecutionHandler() {
		super(Lifecycle.GRANTED);
		handlerList = new ArrayList<GrantedMessageHandler>();
	}

	public GrantedExecutionHandler(
			GrantedExecutionHandler grantedExecutionHandler) {
		super(grantedExecutionHandler);
		handlerList = new ArrayList<GrantedMessageHandler>(grantedExecutionHandler.handlerList);
	}

	@Override
	public IExecutionHandler<JxtaMessageSender> clone()  {
		return new GrantedExecutionHandler(this);
	}

	@Override
	public void run() {
		for(String sub : getExecutionListenerCallback().getQuery().getSubPlans().keySet()) {
							logger.debug("Erzeuge GrantedMessageHandler " +"Granted"+getExecutionListenerCallback().getQuery().getId()+"_"+sub);
				GrantedMessageHandler handl = new GrantedMessageHandler(getExecutionListenerCallback(), "Granted"+getExecutionListenerCallback().getQuery().getId()+"_"+sub, getFunction(), log);
				this.handlerList.add(handl);
		}
		
		getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
	}
	
	@Override
	public void setPeer(IOdysseusPeer peer) {
		super.setPeer(peer);
		setFunction((JxtaMessageSender) getPeer().getMessageSender());
		this.log = getPeer().getLog();
	}

}
