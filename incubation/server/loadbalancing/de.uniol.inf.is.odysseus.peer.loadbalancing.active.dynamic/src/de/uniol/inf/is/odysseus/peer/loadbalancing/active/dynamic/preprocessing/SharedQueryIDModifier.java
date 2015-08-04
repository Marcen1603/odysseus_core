package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing;

import java.util.ArrayList;
import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class SharedQueryIDModifier {
	
	public static void addSharedQueryIDIfNeccessary(int queryID, IServerExecutor executor,IQueryPartController queryPartController,IP2PNetworkManager networkManager,ISession session) {
		if(queryPartController.getSharedQueryID(queryID)==null) {
			ID sharedQueryID = IDFactory.newContentID(networkManager.getLocalPeerGroupID(), false, String.valueOf(System.currentTimeMillis()).getBytes());
			Collection<PeerID> otherPeers = new ArrayList<PeerID>();
			queryPartController.registerAsMaster(executor.getLogicalQueryById(queryID, session), queryID, sharedQueryID, otherPeers);
			//Sleep 1 MS to avoid having the same sharedQueryID for multiple Queries.
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
