package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.granted;

import java.util.ArrayList;
import java.util.List;

import net.jxta.endpoint.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class GrantedMessageHandler extends AbstractJxtaMessageHandler implements
		Runnable {

	static Logger logger = LoggerFactory.getLogger(GrantedMessageHandler.class);

	private P2PQuery query;
	private IExecutionListenerCallback callback;
	private IOdysseusPeer peer;
	private List<String> confirmed;

	public GrantedMessageHandler(P2PQuery query,
			IExecutionListenerCallback callback, IOdysseusPeer peer,
			String interestedNamespace) {
		this.query = query;
		this.callback = callback;
		this.setPeer(peer);
		this.confirmed = new ArrayList<String>();
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String subplanId = meas(namespace, "subplanId", (Message) msg);
		synchronized (confirmed) {
			this.confirmed.add(subplanId);
			confirmed.notifyAll();
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
				logger.debug("Subplan " + sub + " not confirmed. Query failed");
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

	public P2PQuery getQuery() {
		return query;
	}

	public void setPeer(IOdysseusPeer peer) {
		this.peer = peer;
	}

	public IOdysseusPeer getPeer() {
		return peer;
	}
}
