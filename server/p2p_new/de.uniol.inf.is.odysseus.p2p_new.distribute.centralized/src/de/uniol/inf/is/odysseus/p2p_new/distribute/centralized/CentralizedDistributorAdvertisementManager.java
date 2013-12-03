package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.IdenticalQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.MasterNotificationAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.ResourceUsageUpdateAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.resourceusage.IResourceUsageUpdateListener;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.resourceusage.ResourceUsageMonitor;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.AdvertisementManagerService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.PlanIntersection;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.SimulationResult;

public class CentralizedDistributorAdvertisementManager implements IAdvertisementListener, IResourceUsageUpdateListener, IServiceStatusListener, IP2PDictionaryListener {
	private static final Logger LOG = LoggerFactory.getLogger(CentralizedDistributorAdvertisementManager.class);
	private static final String MASTER_STATUS_SYS_PROPERTY = "isCentralizedDistributorMaster";
	private ResourceUsageMonitor monitor;
	private boolean activated = false;
	private IDataDictionary dd;
	// prefer a result of another peer, if the placement of a result would raise
	// a peer's usage more than this value about all the peer's average usage
	@SuppressWarnings("unused")
	private double maxUsageDelta;
	
	// the PeerID of the Master
	private PeerID masterID;
	// the PeerID of this instance
	private PeerID localID;
	private static CentralizedDistributorAdvertisementManager instance;
	
	private List<ID> processedAdvertisements = new ArrayList<ID>();
	
	private CentralizedDistributorAdvertisementManager() {

	}
	
	@Override
	public void serviceBound(Object o) {
		if(activated) {
			return;
		}
		for(Class<?> c : o.getClass().getInterfaces()) {
			if(c.equals(IP2PDictionary.class)) {
				LOG.debug("Found P2PDictionary");
				this.activate();
			} else if(c.equals(IAdvertisementManager.class)) {
				LOG.debug("Found AdvertisementManager");
				this.activate();
			} else if(ServerExecutorService.isBound()) {
				LOG.debug("Found IExecutor");
				dd = ServerExecutorService.getServerExecutor().getDataDictionary(UserManagementProvider.getSessionmanagement().loginSuperUser(null,UserManagementProvider.getDefaultTenant().getName()).getTenant());
				this.activate();
			}
		}
	}
	// activator
	public void activate() {
		if(activated) {
			return;
		}
		if(!P2PDictionaryService.isBound()) {
			LOG.debug("No P2P-Dictionary bound yet, delaying activation");
			P2PDictionaryService.addListener(this);
			return;
		} else if (!AdvertisementManagerService.isBound()) {
			LOG.debug("No AdvertisementManager bound yet, delaying activation");
			AdvertisementManagerService.addListener(this);
			return;
		} else if(!ServerExecutorService.isBound()) {
			LOG.debug("No IExecutor bound yet, delaying activation");
			ServerExecutorService.addListener(this);
			return;
		}
		P2PDictionaryService.get().addListener(this);
		AdvertisementManagerService.getAdvertisementManager().addAdvertisementListener(this);
		PhysicalQueryPartController.getInstance().bindExecutor(getExecutor());
		
		instance = this;
		localID = P2PNetworkManagerService.get().getLocalPeerID();
		LOG.debug("The local ID of this peer is " + localID);
		//check via parameters, if this peer is designated as the master
		boolean isMaster = determineMasterStatus();
		if(isMaster) {
			this.masterID = localID;
			LOG.debug("Peer " + localID + " is the master.");
			// broadcast a request for their physical plans to all known peers and let them know, that this instance is their master
			for(PeerID peerID : P2PDictionaryService.get().getRemotePeerIDs()) {
				notifyPeerOfMaster(masterID, peerID);
			}
		} else {
			LOG.debug("Peer " + localID + " is a peer.");
			// if it's a peer, start the performance-monitoring
			monitor = new ResourceUsageMonitor(this);
			monitor.start();
		}
		LOG.debug("AdvertisementController for the centralized distributor activated");
		activated = true;
	}
	

	
	public static CentralizedDistributorAdvertisementManager getInstance() {
		if(instance == null) {
			instance =  new CentralizedDistributorAdvertisementManager();
		}
		return instance;
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender,
			Advertisement a) {
		if(processedAdvertisements.contains(a.getID())) {
			//System.out.println("Discarded Advertisement " + a.getID().toString() + " of type " + a.getAdvType());
			return;
		}
		
		if(a instanceof PhysicalQueryPlanAdvertisement) {
			PhysicalQueryPlanAdvertisement adv = (PhysicalQueryPlanAdvertisement) a;
			// This node is the master and received a PhysicalQueryPlanAdvertisement intended for it
			if(isMaster() && adv.getMasterPeerID().equals(this.localID)) {
				LOG.debug("Peer " + this.localID + " received a PhysicalQueryPlanAdvertisement from peer " + adv.getPeerID());
				// At this point, the Advertisement should be rebuild already because jxta handles this upon receiving the message
				// This means, that it should already have the connected operators attached to it
				CentralizedDistributor.getInstance().setPhysicalPlan(adv.getPeerID(), adv.getOpObjects());
				CentralizedDistributor.getInstance().updatePlanCostEstimateForPeer(adv.getPeerID(), adv.getOpObjects());
				if(CentralizedDistributor.getInstance().getResourceUsageForPeer(adv.getPeerID()) == null) {
					CentralizedDistributor.getInstance().setInitialResourceUsageForPeer(adv.getPeerID());
				}
			}
		} else if (a instanceof MasterNotificationAdvertisement) {
			MasterNotificationAdvertisement adv = (MasterNotificationAdvertisement) a;
			// This node is a normal peer and received a MasterNotificationAdvertisement from the master
			if(!isMaster() && adv.getPeerID().equals(this.localID)) {
				LOG.debug("Peer " + this.localID + " received a MasterNotificationAdvertisement");
				this.masterID = adv.getMasterPeerID();
				LOG.debug("Peer " + localID + " is now aware of Peer " + masterID + "'s master status.");
				// send a PhysicalQueryPlanAdvertisement to the master to let it know of the plans currently running on this node
				sendQueryPlanToMaster();				
			}
		} else if (a instanceof PhysicalQueryPartAdvertisement) {
			PhysicalQueryPartAdvertisement adv = (PhysicalQueryPartAdvertisement) a;
			// this node is a normal peer and received a physical query-part to place within the local plan
			if(!isMaster() && adv.getPeerID().equals(this.localID)) {
				LOG.debug("Peer " + this.localID + " received a QueryPart for query " + adv.getSharedQueryID());
				
				//EVALUATION
				LOG.debug(adv.toString());
				//EVALUATION
				
				// Add the operators under the ID they were sent, because this is how the master knows them.
				// Since master and peer hold their own objects, we have to keep things consistent
				// and synchronize their references by the communicated IDs.
				adv.handleOperatorStatement();
				adv.handleSubscriptionsStatement();
				adv.handlePlanIntersectionStatement();
				Map<Integer, IPhysicalOperator> newOperators = adv.getQueryPartOperatorObjects();
				CentralizedDistributor.getInstance().addOperators(localID, newOperators);
				List<PlanIntersection> intersections = adv.getIntersections();
				CentralizedDistributor.getInstance().processIntersections(adv.getPeerID(),intersections);
				String queryBuildConfigurationName = "Standard";
				ISession user = UserManagementProvider.getSessionmanagement().loginSuperUser(null, "");
				int queryID = this.getExecutor().addQuery(new ArrayList<IPhysicalOperator>(newOperators.values()), user, queryBuildConfigurationName);
				ID sharedQueryID = adv.getSharedQueryID();
				LOG.debug("Trying to register Query " + queryID + " as slave under the sharedQueryID " + sharedQueryID);
				List<Integer> ids = new ArrayList<Integer>();
				ids.add(queryID);
				PhysicalQueryPartController.getInstance().registerAsSlave(ids, sharedQueryID);
				this.getExecutor().startQuery(queryID, user);
				List<Integer> newOpIDs = new ArrayList<Integer>();
				newOpIDs.addAll(newOperators.keySet());
				CentralizedDistributor.getInstance().setOpsForQueryForPeer(this.localID, sharedQueryID, newOpIDs);
			}
		} else if (a instanceof IdenticalQueryAdvertisement) {
			IdenticalQueryAdvertisement adv = (IdenticalQueryAdvertisement)a;
			if(!isMaster() && adv.getPeerID().equals(this.localID)) {
				String queryBuildConfigurationName = "Standard";
				ISession user = UserManagementProvider.getSessionmanagement().loginSuperUser(null, "");
				ILogicalQuery emptyLogicalQuery = new LogicalQuery();
				emptyLogicalQuery.setParserId("");
				emptyLogicalQuery.setQueryText("");
				emptyLogicalQuery.setName("");
				emptyLogicalQuery.setUser(user);
				emptyLogicalQuery.setLogicalPlan(new TopAO(), true);
				int idOfRunningQuery = PhysicalQueryPartController.getInstance().getLocalQueryCorrespondingToSharedQueryID(adv.getOldSharedQueryID());
				ID newSharedQueryID = adv.getNewSharedQueryID();
				int newQueryID = this.getExecutor().addIdenticalQuery(idOfRunningQuery, emptyLogicalQuery, user, queryBuildConfigurationName);
				Collection<Integer> ids = new ArrayList<Integer>();
				ids.add(newQueryID);
				PhysicalQueryPartController.getInstance().registerAsSlave(ids, newSharedQueryID);
				List<Integer> opIDsOfOldAndNewQuery = CentralizedDistributor.getInstance().getOpsForQueriesForPeer(localID).get(adv.getOldSharedQueryID());
				// explain to the centralized distributor, that the operators in question are used by the new queries as well
				CentralizedDistributor.getInstance().setOpsForQueryForPeer(localID, newSharedQueryID, opIDsOfOldAndNewQuery);
				this.getExecutor().startQuery(newQueryID, user);
			}
		} else if (a instanceof ResourceUsageUpdateAdvertisement) {
			ResourceUsageUpdateAdvertisement adv = (ResourceUsageUpdateAdvertisement) a;
			if(isMaster() && adv.getMasterID().equals(this.localID)) {
				LOG.trace("Peer " + this.localID + " received a ResourceUsageUpdateAdvertisement from " + adv.getPeerID());
				//LOG.debug(adv.toString());
				CentralizedDistributor.getInstance().updateResourceUsage(adv);
			}
		} else {
			LOG.debug("I honestly have no idea what exactly this is, but someone sent me this: " + a.getAdvType());
		}
		processedAdvertisements.add(a.getID());
	}

	private void sendQueryPlanToMaster() {
		if(this.getExecutor() == null) {
			LOG.debug("no executor!");
			return;
		} else if (this.getExecutor().getExecutionPlan() == null) {
			LOG.debug("no executionPlan!");
			return;
		} else if (this.getExecutor().getExecutionPlan().getQueries() == null || this.getExecutor().getExecutionPlan().getQueries().isEmpty()) {
			LOG.debug("no queries in the plan, sending the empty plan anyway to inform master of this peer's status");
		}
		final PhysicalQueryPlanAdvertisement adv = (PhysicalQueryPlanAdvertisement) AdvertisementFactory.newAdvertisement(PhysicalQueryPlanAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setMasterPeerID(this.masterID);
		adv.setPeerID(this.localID);

		Collection<IPhysicalQuery> queries = this.getExecutor().getExecutionPlan().getQueries();
		
		Map<Integer,IPhysicalOperator> operators = CentralizedDistributor.getInstance().getOperatorPlans().get(this.localID);
		// if the local plan isn't saved already, retrieve it and store it
		if(operators == null) {
			operators = new HashMap<Integer,IPhysicalOperator>();
			for(IPhysicalQuery q : queries) {
				for(IPhysicalOperator o : q.getAllOperators()) {
					operators.put(o.hashCode(), o);
				}
			}
			CentralizedDistributor.getInstance().setPhysicalPlan(this.localID, operators);
		}
		adv.setOpObjects(operators);
		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(this.masterID.toString(), adv, 15000);
		
		LOG.debug("Sent physicalQueryPlan of Peer " + this.localID + " to MasterPeer " + this.masterID);
	}
	
	public void sendPhysicalPlanToPeer(SimulationResult r, ID sharedQueryID) {
		final PhysicalQueryPartAdvertisement adv = (PhysicalQueryPartAdvertisement) AdvertisementFactory.newAdvertisement(PhysicalQueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setSharedQueryID(sharedQueryID);
		adv.setPeerID(r.getPeer());
		adv.setMasterPeerID(this.masterID);
		adv.setQueryPartOperatorObjects(r.getPlan(true));
		adv.setIntersections(r.getIntersections());
		adv.generateOperatorsDocument(MimeMediaType.XMLUTF8);
		adv.generateSubscriptionsDocument(MimeMediaType.XMLUTF8);
		adv.generatePlanIntersectionsStatement(MimeMediaType.XMLUTF8);
		//LOG.debug("-----------------------------------------------");
		//LOG.debug(adv.getNewOperatorsStatement().toString());
		//LOG.debug(adv.getDocument(MimeMediaType.XMLUTF8).toString());
		//Enumeration<LiteXMLElement> lxmle = ((LiteXMLElement)adv.getNewOperatorsStatement().getChildren("de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO").nextElement()).getChildren();
		//LOG.debug("has more elements: " + lxmle.hasMoreElements());
		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(r.getPeer().toString(), adv, 15000);
		LOG.debug("Sent physicalQueryPart to Peer " + r.getPeer().toString());
		//LOG.debug(adv.toString());
		CentralizedDistributor.getInstance().setOpsForQueryForPeer(r.getPeer(), sharedQueryID, r.getOperatorIDsOfNewQuery(true));
	}
	
	public void sendIdenticalQueryAdvertisementToPeer(SimulationResult r, ID oldSharedQueryID, ID newSharedQueryID) {
		final IdenticalQueryAdvertisement adv = (IdenticalQueryAdvertisement) AdvertisementFactory.newAdvertisement(IdenticalQueryAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setNewSharedQueryID(newSharedQueryID);
		adv.setMasterPeerID(localID);
		adv.setPeerID(r.getPeer());
		adv.setOldSharedQueryID(oldSharedQueryID);
		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(r.getPeer().toString(), adv, 15000);
		LOG.debug("Sent identical Query to Peer " + r.getPeer().toString());
		List<Integer> opIDsOfOldAndNewQuery = CentralizedDistributor.getInstance().getOpsForQueriesForPeer(r.getPeer()).get(oldSharedQueryID);
		// explain to the centralized distributor, that the operators in question are used by the new queries as well
		CentralizedDistributor.getInstance().setOpsForQueryForPeer(r.getPeer(), newSharedQueryID, opIDsOfOldAndNewQuery);
	}
	
	@Override
	public void updateResourceUsage(double cpuUsage, double mem_free, double mem_total, double mem_used, double networkUsage) {
		sendResourceUsageToMaster(cpuUsage, mem_free, mem_total, mem_used, networkUsage);
	}
	
	private void sendResourceUsageToMaster(double cpuUsage, double mem_free, double mem_total, double mem_used, double networkUsage) {
		if(masterID == null) {
			return;
		}
		ResourceUsageUpdateAdvertisement adv = new ResourceUsageUpdateAdvertisement();
		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setCpu_usage(cpuUsage);
		adv.setNetworkUsage(networkUsage);
		adv.setMem_free(mem_free);
		adv.setMem_total(mem_total);
		adv.setMem_used(mem_used);
		adv.setTimestamp(System.currentTimeMillis());
		adv.setMasterID(this.masterID);
		adv.setPeerID(this.localID);
		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(this.masterID.toString(), adv, 15000);
		LOG.debug("Sent ResourceUsageUpdateAdvertisement to peer " + masterID.toString());
		LOG.debug("CPU-Usage: " + cpuUsage + ", Mem-Usage: " + (mem_used/mem_total));
	}



	@Override
	public void advertisementRemoved(IAdvertisementManager sender,
			Advertisement adv) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isMaster() {
//		
//		if(localID == null) {
//			localID = P2PDictionaryService.get().getLocalPeerID();
//			if(determineMasterStatus()) {
//				masterID = localID;
//			}
//		}

		return this.localID.equals(this.masterID);
	}
	
	private static void notifyPeerOfMaster(PeerID masterPeer, PeerID destinationPeer) {
		final MasterNotificationAdvertisement adv = (MasterNotificationAdvertisement) AdvertisementFactory.newAdvertisement(MasterNotificationAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setPeerID(destinationPeer);
		adv.setMasterPeerID(masterPeer);
		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(destinationPeer.toString(), adv, 15000);
		
		LOG.debug("Notified Peer " + destinationPeer + " of Master " + masterPeer);
	}
	
	private IServerExecutor getExecutor() {
		return ServerExecutorService.getServerExecutor();
	}



	public PeerID getMasterID() {
		return masterID;
	}



	public void setMasterID(PeerID masterID) {
		this.masterID = masterID;
	}



	public PeerID getLocalID() {
		return localID;
	}



	public void setLocalID(PeerID localID) {
		this.localID = localID;
	}

	@Override
	public void sourceAdded(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sourceRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sourceImported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sourceExported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		if(id.equals(localID)) {
			return;
		}
		if(isMaster()) {
			System.out.println("Peer " + name + " has been added, trying to notify it of this master's status!");
			notifyPeerOfMaster(masterID, id);
		}
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		// TODO Auto-generated method stub	
	}
	
	private static boolean determineMasterStatus() {
		String isMaster = System.getProperty(MASTER_STATUS_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(isMaster)) {
			return Boolean.parseBoolean(isMaster);
		}

		isMaster = OdysseusConfiguration.get(MASTER_STATUS_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(isMaster)) {
			return Boolean.parseBoolean(isMaster);
		}

		return false;
	}
	
	@Override
	public String getPeerName() {
		return P2PNetworkManagerService.get().getLocalPeerName();
	}

	public IDataDictionary getDd() {
		return dd;
	}
}
