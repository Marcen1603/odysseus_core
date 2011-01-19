package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.granted;

import java.util.ArrayList;
import java.util.List;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class GrantedMessageHandler extends AbstractMessageHandler implements Runnable {

	private Query query;
	private IExecutionListenerCallback callback;
	private IOdysseusPeer peer;
	private List<String> confirmed;
	
	public GrantedMessageHandler(Query query, IExecutionListenerCallback callback, IOdysseusPeer peer) {
		this.query = query;
		this.callback = callback;
		this.setPeer(peer);
		this.confirmed = new ArrayList<String>();
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void handleMessage(Object msg, String namespace) {		
		String subplanId = MessageTool.getMessageElementAsString(
				namespace, "subplanId", (Message)msg);
		synchronized (confirmed) {
			this.confirmed.add(subplanId);	
		}
	}
	
	@Override
	public void run() {
		synchronized (this) {
			try {
				this.wait(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			synchronized (confirmed) {
				for(String sub : query.getSubPlans().keySet()) {
					if(!this.confirmed.contains(sub)) {
						getCallback().changeState(Lifecycle.FAILED);
						return;
					}
				}
			}
			getCallback().changeState(Lifecycle.SUCCESS);
			return;
		}
	}
	
	public IExecutionListenerCallback getCallback() {
		return callback;
	}
	
	public Query getQuery() {
		return query;
	}

	public void setPeer(IOdysseusPeer peer) {
		this.peer = peer;
	}

	public IOdysseusPeer getPeer() {
		return peer;
	}
}
