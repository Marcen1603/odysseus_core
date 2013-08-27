package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.MasterNotificationAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.JxtaServicesProviderService;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

public class CentralizedDistributorAdvertisementManager implements IAdvertisementListener {
	private static final Logger LOG = LoggerFactory.getLogger(CentralizedDistributorAdvertisementManager.class);
	private IServerExecutor executor;
	
	// the PeerID of the Master
	private PeerID masterID;
	// the PeerID of this instance
	private PeerID localID;
	private static CentralizedDistributorAdvertisementManager instance;
	
	
	// activator
	public void activate() {
		instance = this;
		localID = P2PDictionaryService.get().getLocalPeerID();
		// TODO: check via parameters, if this peer is designated as the master
		if(true) {
			this.masterID = localID;
			// broadcast a request for their physical plans to all known peers and let them know, that this instance is their master
			for(PeerID peerID : P2PDictionaryService.get().getRemotePeerIDs()) {
				notifyPeerOfMaster(masterID, peerID);
			}
		}
		LOG.debug("AdvertisementController for the centralized distributor activated");
	}
	

	
	public static CentralizedDistributorAdvertisementManager getInstance() {
		return instance;
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender,
			Advertisement a) {
		if(a instanceof PhysicalQueryPlanAdvertisement) {
			PhysicalQueryPlanAdvertisement adv = (PhysicalQueryPlanAdvertisement) a;
			// This node is the master and received a PhysicalQueryPlanAdvertisement intended for it
			if(isMaster() && adv.getMasterPeerID().equals(this.localID)) {
				// At this point, the Advertisement should be rebuild already because jxta handles this upon receiving the message
				// This means, that it should already have the connected operators attached to it
				CentralizedDistributor.getInstance().setPhysicalPlan(adv.getPeerID(), adv.getOpObjects());
			}
		} else if (a instanceof MasterNotificationAdvertisement) {
			MasterNotificationAdvertisement adv = (MasterNotificationAdvertisement) a;
			// This node is a normal peer and received a MasterNotificationAdvertisement from the master
			if(!isMaster() && adv.getPeerID().equals(this.localID)) {
				this.masterID = adv.getMasterPeerID();
				// send a PhysicalQueryPlanAdvertisement to the master to let it know of the plans currently running on this node
				sendQueryPlanToMaster();				
			}
		} else if (a instanceof PhysicalQueryPartAdvertisement) {
			PhysicalQueryPartAdvertisement adv = (PhysicalQueryPartAdvertisement) a;
			// this node is a normal peer and received a physical query-part to place within the local plan
			if(!isMaster() && adv.getPeerID().equals(this.localID)) {
				//this.getExecutor().addQuery(physicalPlan, user, queryBuildConfigurationName)
			}	
		}
	}

	private void sendQueryPlanToMaster() {
		final PhysicalQueryPlanAdvertisement adv = (PhysicalQueryPlanAdvertisement) AdvertisementFactory.newAdvertisement(PhysicalQueryPlanAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID()));
		adv.setMasterPeerID(this.masterID);
		adv.setPeerID(this.localID);
		Collection<IPhysicalQuery> queries = this.getExecutor().getExecutionPlan().getQueries();
		Map<Integer,IPhysicalOperator> operators = new HashMap<Integer,IPhysicalOperator>();
		for(IPhysicalQuery q : queries) {
			for(IPhysicalOperator o : q.getAllOperators()) {
				operators.put(o.hashCode(), o);
			}
		}
		adv.setOpObjects(operators);
		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(this.masterID.toString(), adv, 15000);
		
		LOG.debug("Sent physicalQueryPlan of Peer " + this.localID + " to MasterPeer " + this.masterID);
	}



	@Override
	public void advertisementRemoved(IAdvertisementManager sender,
			Advertisement adv) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isMaster() {
		return this.masterID.equals(this.localID);
	}
	
	private static void notifyPeerOfMaster(PeerID masterPeer, PeerID destinationPeer) {
		final MasterNotificationAdvertisement adv = (MasterNotificationAdvertisement) AdvertisementFactory.newAdvertisement(MasterNotificationAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID()));
		adv.setPeerID(destinationPeer);
		adv.setMasterPeerID(masterPeer);
		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(destinationPeer.toString(), adv, 15000);
		
		LOG.debug("Notified Peer " + destinationPeer + " of Master " + masterPeer);
	}
	
	public void bindExecutor(IExecutor exe) {
		executor = (IServerExecutor) exe;
		LOG.debug("Executor bound: " + exe);
	}
	
	public void unbindExecutor(IExecutor exe) {
		executor = null;
		LOG.debug("Executor unbound: " + exe);
	}
	
	private IServerExecutor getExecutor() {
		return this.executor;
	}
}
