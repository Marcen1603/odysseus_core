package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing;

import java.util.ArrayList;
import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

/**
 * Adds shared QueryID to Query to allow for distribution of Query.
 * @author Carsten Cordes
 *
 */
public class SharedQueryIDModifier {
	
	/**
	 * Add shared Query ID to query (if it has none yet)
	 * @param queryID Query 
	 * @param session active Session
	 * @param executor Executor
	 * @param queryPartController QueryPart Controller
	 * @param networkManager Network Manager
	 */
	public static void addSharedQueryIDIfNeccessary(int queryID,ISession session,IServerExecutor executor, IQueryPartController queryPartController, IP2PNetworkManager networkManager) {

		
		if(queryPartController.getSharedQueryID(queryID)==null) {
			//Using Nanoseconds to avoid toooo fast random ID generation ;)
			ID sharedQueryID = IDFactory.newContentID(networkManager.getLocalPeerGroupID(), false, String.valueOf(System.nanoTime()).getBytes());
			Collection<PeerID> otherPeers = new ArrayList<PeerID>();
			queryPartController.registerAsMaster(executor.getLogicalQueryById(queryID, session), queryID, sharedQueryID, otherPeers);
		}
	}

}
