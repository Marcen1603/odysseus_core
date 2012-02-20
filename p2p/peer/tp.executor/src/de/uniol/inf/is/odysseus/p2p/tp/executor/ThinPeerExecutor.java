package de.uniol.inf.is.odysseus.p2p.tp.executor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.IExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.IThinPeer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.IThinPeerListener;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

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
	public Collection<ILogicalQuery> addQuery(String query, String parserID,
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
	public String getInfos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() throws ExecutorInitializeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExecutionConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IQueryBuildConfiguration getQueryBuildConfiguration(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, IQueryBuildConfiguration> getQueryBuildConfigurations() {
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
	public List<IPhysicalQuery> startAllClosedQueries(ISession user) {
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
	public List<ILogicalQuery> translateQuery(String query, String parserID,
			ISession user) throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPhysicalQuery transform(ILogicalQuery query,
			TransformationConfiguration transformationConfiguration,
			ISession caller) throws TransformationException {
		// TODO Auto-generated method stub
		return null;
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
	public IPhysicalQuery addQuery(ILogicalOperator logicalPlan, ISession user,
			String queryBuildConfigurationName) throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPhysicalQuery addQuery(List<IPhysicalOperator> physicalPlan,
			ISession user, String queryBuildConfigurationName)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

}
