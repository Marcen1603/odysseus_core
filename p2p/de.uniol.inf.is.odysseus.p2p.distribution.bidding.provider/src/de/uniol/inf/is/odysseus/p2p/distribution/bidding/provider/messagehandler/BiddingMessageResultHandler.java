package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler;

import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class BiddingMessageResultHandler implements IMessageHandler {
	
	private HashMap<Query, IExecutionListener> managedQueries;

	public BiddingMessageResultHandler(HashMap<Query, IExecutionListener> managedQueries) {
		this.setManagedQueries(managedQueries);
	}


	@Override
	public String getInterestedNamespace() {
		return "BiddingProvider";
	}

	//TODO BiddingStrategy hier einbauen, wenn Nachrichten ausgewertet werden
	@Override
	public void handleMessage(Object msg, String namespace) {
		String queryId = MessageTool.getMessageElementAsString(
				namespace, "queryId", (Message)msg);
		String bid = MessageTool.getMessageElementAsString(
				namespace, "ExecutionBid", (Message)msg);
		PipeAdvertisement pipeAdv = MessageTool
				.getResponsePipe(namespace, (Message)msg, 0);
		
		String peerId = MessageTool.getMessageElementAsString(
				namespace, "peerId", (Message)msg);
		String subplanId = MessageTool.getMessageElementAsString(
				namespace, "subplanId", (Message)msg);
		Log.logAction(queryId, "Gebot von einem Operator-Peer eingegangen.");
		if(getManagedQueries().get(queryId) != null && bid.equals("positive")) {
			//Füge das Gebot dem entsprechenden Subplan der Query hinzu
			((QueryJxtaImpl)getManagedQueries().get(queryId)).addBidding(pipeAdv, peerId, subplanId, bid);
			for(Query query : getManagedQueries().keySet()) {
				if(query.getId() == queryId) {
					Log.addBid(queryId, query.getSubPlans().get(subplanId).getBiddings().size());		
				}
			}
			
		}
	}


	public void setManagedQueries(HashMap<Query, IExecutionListener> managedQueries) {
		this.managedQueries = managedQueries;
	}


	public HashMap<Query, IExecutionListener> getManagedQueries() {
		return managedQueries;
	}



}
