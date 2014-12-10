package de.uniol.inf.is.odysseus.sports.distributor.webservice;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.listener.AbstractQueryDistributionListener;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryDistributionNotifier;
import de.uniol.inf.is.odysseus.rest.service.RestService;

/**
 * Helper class to get information to a distributed query
 * @author Thore Stratmann
 *
 */
public class DistributedQueryHelper {
	private static IPeerDictionary peerDictionary;
	private static IQueryPartController queryPartController;
	private static IServerExecutor serverExecutor;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IJxtaServicesProvider jxtaServicesProvider;
	
	private static WebserviceAdvertisementListener webserviceAdvListener = new WebserviceAdvertisementListener();
	private static WebserviceAdvertisementSender webserviceAdvSender = new WebserviceAdvertisementSender();
	private static QueryDistributionListener queryDistributionListener = new QueryDistributionListener();

	
	private static final Logger LOG = LoggerFactory.getLogger(DistributedQueryHelper.class);

	
	/**
	 * Executes the given query and returns information about distribution to the executed query (sharedQueryId, ip and port of top operator)
	 * @param sportsQL
	 * @param activeSession
	 * @param queryBuildConfigurationName
	 * @return null if query distribution was not successful
	 */
	public static DistributedQueryInfo executeQuery(String query,String parser,
			ISession activeSession, String queryBuildConfigurationName) {
		serverExecutor.addQuery(query, parser, activeSession, Context.empty());
		return queryDistributionListener.getNextDistributedQueryInfo();
	}

	/**
	 * Returns the top/root operator of the given query that is not an instance of {@link JxtaSenderPO}
	 * @param sharedQueryId
	 * @param session
	 * @return
	 */
	public static IPhysicalOperator getTopOperatorOfQuery(int queryId, ISession session) {
		IExecutionPlan plan = serverExecutor.getExecutionPlan();
		IPhysicalQuery query = plan.getQueryById(queryId);
		for (IPhysicalOperator op : query.getRoots()) {
			if (!(op instanceof JxtaSenderPO)) {
				return op;
			}
		}	
		return null;

	}
	
	/**
	 * Returns the local query to the sharedQueryId that has not a {@link JxtaSenderPO} as top/root operator
	 * @param sharedQueryId
	 * @param session
	 * @return
	 */
	public static Integer getQueryIdWithTopOperator(String sharedQueryId, ISession session) {
		ID qid = null;
		try {
			qid = IDFactory.fromURI(new URI(sharedQueryId));
		} catch (final URISyntaxException ex) {
			LOG.error("Could not convert string {} to id", sharedQueryId, ex);
			return null;
		}
		IExecutionPlan plan = serverExecutor.getExecutionPlan();
		Collection<Integer> localQueryIds = queryPartController.getLocalIds(qid);
		for (Integer localQueryId : localQueryIds) {
			IPhysicalQuery query = plan.getQueryById(localQueryId);
			for (IPhysicalOperator op : query.getRoots()) {
				if (!(op instanceof JxtaSenderPO)) {
					return localQueryId;
				}
			}			
		}
		return null;
	}

	

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = null;
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager.addListener(P2PNetworkListener.newInstance());
	}

	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		peerDictionary = null;
	}

	// called by OSGi-DS
	public static void bindQueryPartController(IQueryPartController serv) {
		queryPartController = serv;
	}

	// called by OSGi-DS
	public static void unbindQueryPartController(IQueryPartController serv) {
		queryPartController = null;
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		serverExecutor = null;
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
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
			registerAdvertisementTypes();
			p2pNetworkManager.addAdvertisementListener(webserviceAdvListener);
			webserviceAdvSender.publishWebserviceAdvertisement(jxtaServicesProvider, p2pNetworkManager.getLocalPeerID(),p2pNetworkManager.getLocalPeerGroupID());
			QueryDistributionNotifier.bindListener(queryDistributionListener);
		}
		
		private void registerAdvertisementTypes() {
			AdvertisementFactory.registerAdvertisementInstance(
					WebserviceAdvertisement.getAdvertisementType(),
					new WebserviceAdvertisementInstantiator());
		}

		@Override
		public void networkStopped(IP2PNetworkManager p2pNetworkManager) {
			QueryDistributionNotifier.unbindListener(queryDistributionListener);
		}
		
	}
	
	private static class QueryDistributionListener extends AbstractQueryDistributionListener {
		private List<DistributedQueryInfo> distributedQueryInfoList = Collections.synchronizedList(new ArrayList<DistributedQueryInfo>());
		
		@Override
		public void afterTransmission(ILogicalQuery query,Map<ILogicalQueryPart, PeerID> allocationMap,	ID sharedQueryId) {
			for (Entry<ILogicalQueryPart, PeerID> entry : allocationMap.entrySet()) {
				for (ILogicalOperator op : entry.getKey().getOperators()) {
					if (op.getSubscriptions().size() == 0 && !(op instanceof JxtaSenderAO) || (op.getSubscriptions().size() ==1 && op.getSubscriptions().iterator().next().getTarget() instanceof TopAO)) {
						DistributedQueryInfo info = new DistributedQueryInfo();
						String address = peerDictionary.getRemotePeerAddress(entry.getValue()).orNull();
						String ip = removePort(address);
						String sharedQueryIdString = sharedQueryId.toURI().toString();
						Integer port = webserviceAdvListener.getRestPort(entry.getValue());
						if (port != null) {
							info.setTopOperatorPeerRestPort(port);
						} else {
							info.setTopOperatorPeerRestPort(RestService.getPort());
						}
						info.setSharedQueryId(sharedQueryIdString);
						info.setTopOperatorPeerIP(ip);
						distributedQueryInfoList.add(info);
						return;
					}
				}
			}
		}
		
		private String removePort(String address) {
			if (address.contains(":")) {
				return address.substring(0, address.indexOf(':'));
			} else {
				return address;
			}
		}
		
		public synchronized DistributedQueryInfo getNextDistributedQueryInfo() {			
			int loops = 10;
			while (distributedQueryInfoList.size() == 0 && loops > 0) {
				loops--;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (distributedQueryInfoList.size()>0) {
				return distributedQueryInfoList.remove(0);
			} else {
				return null;
			}
		}
	}

	public static boolean isDistributionPossible() {
		return peerDictionary.getRemotePeerIDs().size()>0;
	}
	
	

}
