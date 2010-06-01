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
	//Soll hier nicht den gesamten ExecutionListener kennen, sondern nur das ihm zugehörige CallbackObjekt
	public BiddingMessageResultHandler(HashMap<Query, IExecutionListener> hashMap) {
		this.managedQueries = hashMap;

	}


	@Override
	public String getInterestedNamespace() {
		return "BiddingProvider";
	}

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
		Log.logAction(queryId, "Gebot ("+bid+") von einem Operator-Peer eingegangen.");
		boolean exists = false;
		Query actualQuery = null;
		for(Query q : getManagedQueries().keySet()) {
			if(q.getId().equals(queryId)) {
				exists = true;
				actualQuery = q;
				break;
			}
		}
		if(exists && bid.equals("positive")) {
			//Füge das Gebot dem entsprechenden Subplan der Query hinzu
			((QueryJxtaImpl)actualQuery).addBidding(pipeAdv, peerId, subplanId, bid);
//			for(Query query : getManagedQueries().keySet()) {
//				if(query.getId() == queryId) {
					Log.addBid(queryId, actualQuery.getSubPlans().get(subplanId).getBiddings().size());		
//				}
//			}
			
		}
	}


	public HashMap<Query, IExecutionListener> getManagedQueries() {
		return managedQueries;
	}

	public void setManagedQueries(
			HashMap<Query, IExecutionListener> managedQueries) {
		this.managedQueries = managedQueries;
	}


	@Override
	public void setInterestedNamespace(String namespace) {
		// TODO Auto-generated method stub
		
	}

}
