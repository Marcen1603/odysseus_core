package de.uniol.inf.is.odysseus.p2p.tp.executor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p.IExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.IThinPeer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.IThinPeerListener;

public class ThinPeerExecutor implements IExecutor, IThinPeerListener{

	static Logger logger = LoggerFactory.getLogger(ThinPeerExecutor.class);
	
	IThinPeer thinPeer;
	List<IExtendedPeerAdvertisement> adminPeers = new LinkedList<IExtendedPeerAdvertisement>();
	List<ISourceAdvertisement> sources = new LinkedList<ISourceAdvertisement>();
	
	public void registerThinPeer(IThinPeer peer){
		this.thinPeer = peer;
		thinPeer.registerListener(this);
		logger.debug("New Thin Peer registered "+peer);
	}
	
	public void removeThinPeer(IThinPeer peer){
		thinPeer.removeListener(this);
		this.thinPeer = null;
	}
	
	
	@Override
	public void foundAdminPeer(IExtendedPeerAdvertisement peer) {
		if (!adminPeers.contains(peer)){
			logger.debug("Found New Admin Peer "+peer.getPeerId());	
			adminPeers.add(peer);
		}
	}
	
	@Override
	public void foundSource(ISourceAdvertisement adv, ISession caller) {
		if (!sources.contains(adv)){
			logger.debug("Found new Source "+adv.getPeerID()+" with "+adv.getSourceName());
			sources.add(adv);
		}
	}
	
	

	@Override
	public List<Integer> addQuery(String query, String parserID,
			ISession user, String queryBuildConfigurationName)
			throws PlanManagementException {
		thinPeer.publishQuerySpezification(query, parserID, user);
		return null;
	}



	@Override
	public String getName() {
		return "Thin Peer Executor";
	}

	
	public void activate() {
		System.out.println("Thin Peer Executor activated");
	}

	@Override
	public void removeQuery(int queryID, ISession caller)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startQuery(int queryID, ISession caller)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopQuery(int queryID, ISession caller)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Collection<String> getQueryBuildConfigurationNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getSupportedQueryParsers()
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Integer> startAllClosedQueries(ISession user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulingStrategies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setScheduler(String scheduler, String schedulerStrategy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCurrentSchedulerID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentSchedulingStrategyID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISession login(String username, byte[] password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout(ISession caller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(
			ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer addQuery(ILogicalOperator logicalPlan, ISession user,
			String queryBuildConfigurationName) throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addQuery(List<IPhysicalOperator> physicalPlan,
			ISession user, String queryBuildConfigurationName)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ILogicalQuery getLogicalQuery(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<Integer> getLogicalQueryIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPhysicalOperator> getPhyiscalRoots(int queryID) {
		// TODO Auto-generated method stub
		return null;
	}

}
