package de.uniol.inf.is.odysseus.peer.rest.sharedquerygraph;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;


import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.rest.PeerServiceBinding;
import de.uniol.inf.is.odysseus.peer.rest.dto.LocalQueryInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.OperatorInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.SharedQueryInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.request.GetLocalQueriesRequestDTO;
import de.uniol.inf.is.odysseus.peer.rest.dto.response.GetLocalQueriesResponseDTO;
import de.uniol.inf.is.odysseus.peer.rest.serverresources.GetLocalQueriesServerResource;

public class SharedQueryGraphHelper {

	
	public static Collection<LocalQueryInfo> getLocalQueries(ISession session, String sharedQueryId) {
		ID sharedQueryID = null;
		try {
			sharedQueryID = IDFactory.fromURI(new URI(sharedQueryId));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Collection<Integer> col = PeerServiceBinding.getQueryPartController().getLocalIds(sharedQueryID);
		Collection<LocalQueryInfo> result = new HashSet<LocalQueryInfo>();
		for (Integer localId : col) {
			result.add(createLocalQueryInfo(localId, session));
		}	
		return result;

	}
	
	private static LocalQueryInfo createLocalQueryInfo(int queryId,ISession session) {
		ILogicalQuery query = PeerServiceBinding.getExecutor().getLogicalQueryById(queryId, session);
		LocalQueryInfo queryInfo = new LocalQueryInfo();
		queryInfo.setQueryId(queryId);
		queryInfo.setName(query.getName());
		queryInfo.setNotice(query.getNotice());
		queryInfo.setParserId(query.getParserId());
		queryInfo.setPriority(query.getPriority());
		queryInfo.setQueryText(query.getQueryText());		
		
		List<OperatorInfo> operators = new ArrayList<OperatorInfo>();
		List<ILogicalOperator> logOperators = new ArrayList<ILogicalOperator>();
		for (LogicalSubscription subs : query.getLogicalPlan().getSubscribedToSource()) {
			addOperator(logOperators, operators, subs.getTarget());
		}
		queryInfo.setOperators(operators);				
		return queryInfo;
	}
	
	private static void addOperator(List<ILogicalOperator> logicalOperators, List<OperatorInfo> operatorInfos, ILogicalOperator operator) {
		logicalOperators.add(operator);		
		
		OperatorInfo operatorInfo = new OperatorInfo();
		if (operator instanceof JxtaReceiverAO) {
			JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
			operatorInfo.setPipeId(receiver.getPipeID());
			operatorInfo.setReceiver(true);
		
		} else if(operator instanceof JxtaSenderAO) {
			JxtaSenderAO sender = (JxtaSenderAO) operator;
			operatorInfo.setPipeId(sender.getPipeID());
			operatorInfo.setSender(true);			
		} 
		operatorInfo.setName(operator.getName());
		operatorInfo.setDestinationName(operator.getDestinationName());
		operatorInfo.setParameterInfos(operator.getParameterInfos());
		operatorInfo.setOutputSchema(createSchemaMap(operator.getOutputSchema()));
		operatorInfo.setClassName(operator.getClass().getSimpleName());
		operatorInfo.setHash(operator.hashCode());
		operatorInfo.setSource(operator.isSourceOperator());
		operatorInfo.setPipe(operator.isPipeOperator());
		operatorInfo.setSink(operator.isSinkOperator());		
		operatorInfo.setOwnerIDs(operator.getOwnerIDs());	
		operatorInfo.setPeerName(PeerServiceBinding.getP2PNetworkManager().getLocalPeerName());		

		if (operator instanceof StreamAO) {
			operatorInfo.setSource(true);
			operatorInfo.setPipe(false);
			operatorInfo.setSink(false);
		}

		Map<Integer, Integer> childs = new HashMap<Integer, Integer>();
		operatorInfo.setChildOperators(childs);
		operatorInfos.add(operatorInfo);		
		for (LogicalSubscription subs : operator.getSubscribedToSource()) {
			ILogicalOperator targetOp = subs.getTarget();
			if (!logicalOperators.contains(targetOp)) {
				addOperator(logicalOperators, operatorInfos, targetOp);
			} 
			childs.put(logicalOperators.indexOf(targetOp), subs.getSourceOutPort());
		}
	}
	
	private static Map<String, String> createSchemaMap(SDFSchema schema) {
		Map<String, String> attributes = new HashMap<String, String>();
		for (SDFAttribute attribute : schema.getAttributes()) {
			attributes.put(attribute.getAttributeName(), attribute.getDatatype().getURI());
		}
		return attributes;
	}
	

	public static Collection<SharedQueryInfo> getSharedQueryIds() {
		Collection<ID> col =  PeerServiceBinding.getQueryPartController().getSharedQueryIds();
		Collection<SharedQueryInfo> result = new HashSet<SharedQueryInfo>();
		for (ID id : col) {
			result.add(new SharedQueryInfo(id.toString()));
		}
		return result;
	}

	public static Map<String, Collection<LocalQueryInfo>> getSharedQueryInfo(ISession session, String sharedQueryId, String tenant, String username, String password) {
		ID sharedQueryID = null;
		try {
			sharedQueryID = IDFactory.fromURI(new URI(sharedQueryId));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Map<String, Collection<LocalQueryInfo>> result = new HashMap<String, Collection<LocalQueryInfo>>();
				
		Collection<Integer> localIds = PeerServiceBinding.getQueryPartController().getLocalIds(sharedQueryID);
		Collection<LocalQueryInfo> queryCol = new HashSet<LocalQueryInfo>();
		for (Integer id : localIds) {
			queryCol.add(createLocalQueryInfo(id, session));
		}
		result.put(PeerServiceBinding.getP2PNetworkManager().getLocalPeerID().toString(), queryCol);

		Collection<PeerID> col = PeerServiceBinding.getQueryPartController().getOtherPeers(sharedQueryID);
		for (PeerID peerID : col) {
			Collection<LocalQueryInfo> queries = null;
			String address = removePort(PeerServiceBinding.getPeerDictionary().getRemotePeerAddress(peerID).orNull());
			int port = PeerServiceBinding.getWebserviceAdvListener().getRestPort(peerID);
			String url = "http://"+address+":"+port+"/peer/"+GetLocalQueriesServerResource.PATH;
			try {
				Client client = new Client(new org.restlet.Context(), Protocol.HTTP);
				ClientResource res = new ClientResource(url);
				res.setNext(client);
				GetLocalQueriesRequestDTO request = new GetLocalQueriesRequestDTO();
				request.setSharedQueryId(sharedQueryID.toString());
				request.setTenant(tenant);
				request.setUsername(username);
				request.setPassword(password);
				GetLocalQueriesResponseDTO resp = res.post(request, GetLocalQueriesResponseDTO.class);
				queries = resp.getLocalQueries();
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put(peerID.toString(), queries);
		}			
		return result;
	}
	
	private static String removePort(String address) {
		return address.substring(0, address.indexOf(':'));
	}

}
