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
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.rest.dto.LocalQueryInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.OperatorInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.SharedQueryInfo;
import de.uniol.inf.is.odysseus.peer.rest.dto.request.GetLocalQueriesRequestDTO;
import de.uniol.inf.is.odysseus.peer.rest.dto.response.GetLocalQueriesResponseDTO;
import de.uniol.inf.is.odysseus.peer.rest.serverresources.GetLocalQueriesServerResource;
import de.uniol.inf.is.odysseus.peer.rest.webservice.WebserviceAdvertisementListener;
import de.uniol.inf.is.odysseus.peer.rest.webservice.WebserviceAdvertisementSender;

public class SharedQueryGraphHelper {
	private static IServerExecutor executor;	
	private static IQueryPartController queryPartController;
	private static IPeerDictionary peerDictionary;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IJxtaServicesProvider jxtaServicesProvider;

	private static WebserviceAdvertisementListener webserviceAdvListener = new WebserviceAdvertisementListener();
	private static WebserviceAdvertisementSender webserviceAdvSender = new WebserviceAdvertisementSender();


	public static IServerExecutor getExecutor() {
		return executor;
	}
	
	public static Collection<LocalQueryInfo> getLocalQueries(ISession session, String sharedQueryId) {
		ID sharedQueryID = null;
		try {
			sharedQueryID = IDFactory.fromURI(new URI(sharedQueryId));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Collection<Integer> col = queryPartController.getLocalIds(sharedQueryID);
		Collection<LocalQueryInfo> result = new HashSet<LocalQueryInfo>();
		for (Integer localId : col) {
			result.add(createLocalQueryInfo(localId, session));
		}	
		return result;

	}
	
	private static LocalQueryInfo createLocalQueryInfo(int queryId,ISession session) {
		ILogicalQuery query = executor.getLogicalQueryById(queryId, session);
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
		operatorInfo.setPeerName(p2pNetworkManager.getLocalPeerName());		

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
		Collection<ID> col =  queryPartController.getSharedQueryIds();
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
				
		Collection<Integer> localIds = SharedQueryGraphHelper.getQueryPartController().getLocalIds(sharedQueryID);
		Collection<LocalQueryInfo> queryCol = new HashSet<LocalQueryInfo>();
		for (Integer id : localIds) {
			queryCol.add(createLocalQueryInfo(id, session));
		}
		result.put(p2pNetworkManager.getLocalPeerID().toString(), queryCol);

		Collection<PeerID> col = SharedQueryGraphHelper.getQueryPartController().getOtherPeers(sharedQueryID);
		for (PeerID peerID : col) {
			Collection<LocalQueryInfo> queries = null;
			String address = removePort(peerDictionary.getRemotePeerAddress(peerID).orNull());
			int port = webserviceAdvListener.getRestPort(peerID);
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

	
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager.addListener(P2PNetworkListener.newInstance());
	}

	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		peerDictionary = null;
	}
	
	
	public void bindExecutor(IExecutor ex) {
		executor = (IServerExecutor) ex;
	}

	public void unbindExecutor(IExecutor ex) {
		executor = null;
	}
		
	
	public static void bindQueryPartController(IQueryPartController controller) {
		queryPartController = controller;
	}
	
	public static void unbindQueryPartController(IQueryPartController controller) {
		queryPartController = null;
	}
	
	public static IQueryPartController getQueryPartController() {
		return queryPartController;
	}
	
	public static void bindPeerDictionary(IPeerDictionary dictionary) {
		peerDictionary = dictionary;
	}
	
	public static void unbindPeerDictionary(IPeerDictionary dictionary) {
		peerDictionary = null;
	}
	
	public static IPeerDictionary getPeerDictionary() {
		return peerDictionary;
	}
	
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

	
	private static class P2PNetworkListener implements IP2PNetworkListener {
		
		public static P2PNetworkListener newInstance() {
			return new P2PNetworkListener();
		}

		@Override
		public void networkStarted(IP2PNetworkManager sender) {
			p2pNetworkManager.addAdvertisementListener(webserviceAdvListener);
			webserviceAdvSender.publishWebserviceAdvertisement(jxtaServicesProvider, p2pNetworkManager.getLocalPeerID(),p2pNetworkManager.getLocalPeerGroupID());
		}
		

		@Override
		public void networkStopped(IP2PNetworkManager p2pNetworkManager) {

		}
	}
}
