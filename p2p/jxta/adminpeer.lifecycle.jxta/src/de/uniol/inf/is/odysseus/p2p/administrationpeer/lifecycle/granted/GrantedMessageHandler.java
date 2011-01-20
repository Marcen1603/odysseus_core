package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.granted;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class GrantedMessageHandler extends AbstractMessageHandler implements
		Runnable {

	static Logger logger = LoggerFactory.getLogger(GrantedMessageHandler.class);
	
	private Query query;
	private IExecutionListenerCallback callback;
	private IOdysseusPeer peer;
	private List<String> confirmed;

	public GrantedMessageHandler(Query query,
			IExecutionListenerCallback callback, IOdysseusPeer peer,
			String interestedNamespace) {
		this.query = query;
		this.callback = callback;
		this.setPeer(peer);
		this.confirmed = new ArrayList<String>();
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String subplanId = MessageTool.getMessageElementAsString(namespace,
				"subplanId", (Message) msg);
		synchronized (confirmed) {
			this.confirmed.add(subplanId);
			notifyAll();
		}
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();

		synchronized (confirmed) {
			// Wait max. 20 Seconds
			while (confirmed.size() != query.getSubPlans().size()
					&& (System.currentTimeMillis() - start) < 20000) {
				try {
					confirmed.wait(1000);
				} catch (InterruptedException e) {
					// Ignore
				}
			}

		}

		// Message Handler no longer needed
		peer.deregisterMessageHandler(this);

		// Check if all Subplans are confirmed
		for (String sub : query.getSubPlans().keySet()) {
			if (!this.confirmed.contains(sub)) {
				logger.debug("Subplan "+sub+" not confirmed. Query failed");
				getCallback().changeState(Lifecycle.FAILED);
				return;
			}
		}
		getCallback().changeState(Lifecycle.SUCCESS);
		return;

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
