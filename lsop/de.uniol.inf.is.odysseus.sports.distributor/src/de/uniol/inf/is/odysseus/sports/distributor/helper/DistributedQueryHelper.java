package de.uniol.inf.is.odysseus.sports.distributor.helper;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.listener.AbstractQueryDistributionListener;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryDistributionNotifier;

public class DistributedQueryHelper {
	private static IP2PDictionary p2pDictionary;	
	private static IQueryPartController queryPartController;
	private static IServerExecutor serverExecutor;

	private static final Logger LOG = LoggerFactory.getLogger(DistributedQueryHelper.class);

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}
	
	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = null;
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

	
	public static ILogicalQuery getLocalQueryWithTopOperator(String sharedQueryId, ISession session) {
		ID qid = null;
		try {
			qid =  IDFactory.fromURI(new URI(sharedQueryId));
		} catch (final URISyntaxException ex) {
			LOG.error("Could not convert string {} to id", sharedQueryId, ex);
			return null;
		}
		Collection<Integer> localQueryIds = queryPartController.getLocalIds(qid);
		for (Integer localQueryId : localQueryIds) {
			ILogicalQuery query = serverExecutor.getLogicalQueryById(localQueryId, session);
			ILogicalOperator topOp = query.getLogicalPlan();
			for (LogicalSubscription subs : topOp.getSubscribedToSource()) {
				ILogicalOperator op = subs.getTarget();
				if (!(op instanceof JxtaSenderAO)) {
					return query;
				}
			}					
		}
		return null;
		
	}

	public static DistributedQueryInfo executeQuery(String sportsQL, ISession activeSession,String queryBuildConfigurationName) {
		final DistributedQueryInfo info = new DistributedQueryInfo();
		QueryDistributionNotifier.bindListener(new AbstractQueryDistributionListener() {
			@Override
			public void afterTransmission(ILogicalQuery query,Map<ILogicalQueryPart, PeerID> allocationMap, ID sharedQueryId) {
				for (Entry<ILogicalQueryPart, PeerID> entry : allocationMap.entrySet()) {
					for (ILogicalOperator op : entry.getKey().getOperators()) {
						if (!(op instanceof JxtaSenderAO)) {
							String address = p2pDictionary.getRemotePeerAddress(entry.getValue()).orNull();
							String ip = removePort(address);
							String sharedQueryIdString = sharedQueryId.toURI().toString();
							info.setSharedQueryId(sharedQueryIdString);
							info.setTopOperatorIP(ip);				
							info.setTopOperatorPeerWebservicePort(9680);
							info.setQueryDistributed(true);
							//QueryDistributionNotifier.unbindListener(this);
							return;
						}
					}
				}	
			}
		});
		serverExecutor.addQuery(sportsQL, "OdysseusScript", activeSession, queryBuildConfigurationName, Context.empty());
		int loops = 10;
		while(!info.isQueryDistributed() && loops > 0) {
			loops--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		return info;
	}
	
	
	private static String removePort(String address) {
		return address.substring(0, address.indexOf(':'));
	}

}
