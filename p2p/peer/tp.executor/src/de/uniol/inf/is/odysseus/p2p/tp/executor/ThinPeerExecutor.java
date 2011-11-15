package de.uniol.inf.is.odysseus.p2p.tp.executor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.p2p.IExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.IThinPeer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.IThinPeerListener;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager;
import de.uniol.inf.is.odysseus.usermanagement.User;

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
	public void foundSource(ISourceAdvertisement adv) {
		if (!sources.contains(adv)){
			logger.debug("Found new Source "+adv.getPeerID()+" with "+adv.getSourceName());
			sources.add(adv);
			thinPeer.addToDD(adv);
		}
	}
	
	
	@Override
	public void removeQuery(int queryID, User caller)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startQuery(int queryID, User caller)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopQuery(int queryID, User caller)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPlan getPlan() throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPlanModificationListener(IPlanModificationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePlanModificationListener(
			IPlanModificationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IExecutionPlan getExecutionPlan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startExecution() throws PlanManagementException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopExecution() throws PlanManagementException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRunning() throws PlanManagementException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addPlanExecutionListener(IPlanExecutionListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePlanExecutionListener(IPlanExecutionListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInfos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireErrorEvent(ErrorEvent eventArgs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void errorEventOccured(ErrorEvent eventArgs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICompiler getCompiler() throws NoCompilerLoadedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISchedulerManager getSchedulerManager() throws SchedulerException {
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
	public Collection<IQuery> addQuery(String query, String parserID,
			User user, IDataDictionary dd, String queryBuildConfigurationName)
			throws PlanManagementException {
		thinPeer.publishQuerySpezification(query, parserID, user);
		return null;
	}

	@Override
	public IQuery addQuery(ILogicalOperator logicalPlan, User user,
			IDataDictionary dd, String queryBuildConfigurationName)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IQuery addQuery(List<IPhysicalOperator> physicalPlan, User user,
			IDataDictionary dd, String queryBuildConfigurationName)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IQuery> startAllClosedQueries(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCompilerListener(ICompilerListener compilerListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBufferPlacementStrategy getBufferPlacementStrategy(String stratID) {
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
	public IScheduler getCurrentScheduler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentSchedulingStrategyID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OptimizationConfiguration getOptimizerConfiguration()
			throws NoOptimizerLoadedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISystemMonitor getDefaultSystemMonitor()
			throws NoSystemMonitorLoadedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISystemMonitor newSystemMonitor(long period)
			throws NoSystemMonitorLoadedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateExecutionPlan() throws NoOptimizerLoadedException,
			QueryOptimizationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "Thin Peer Executor";
	}

	@Override
	public IOptimizer getOptimizer() throws NoOptimizerLoadedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IQuery> getQueries() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void activate() {
		System.out.println("Thin Peer Executor activated");
	}
	
}
