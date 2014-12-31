package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.IDDCListener;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.listener.AbstractQueryDistributionListener;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryPreprocessorListener;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;

// TODO Die ganze Klasse gefällt mir nicht. Es ist, glaube ich, nicht zweckmäßig (nur) auf den lokalen Distributor zu horchen. 
// Es muss auch möglich sein, die Backup Informationen zentral vom Distributor aus zu erstellen. M.B.

/**
 * A helper class for backup information (e.g., update of multiple stores).
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationHelper extends AbstractQueryDistributionListener
		implements IPlanModificationListener, IRecoveryPreprocessorListener,
		IDDCListener {

	/**
	 * The logger instance for this class.
	 */
	private static Logger LOG = LoggerFactory
			.getLogger(BackupInformationHelper.class);

	private static IBackupInformationAccess backupInformationAccess;

	/**
	 * Keep those shared query ids in mind, for which the local peer is
	 * registered as master.
	 */
	private static final Set<ID> cSharedQueryIDsForMaster = Collections
			.synchronizedSet(new HashSet<ID>());

	/**
	 * Keep in mind, if there has been a strategy set for any query part.
	 */
	private static final Map<ILogicalQueryPart, String> cStrategyToQueryPart = Collections
			.synchronizedMap(new HashMap<ILogicalQueryPart, String>());

	/**
	 * Keep in mind, if there has been a strategy set for any query part on
	 * another peer.
	 */
	private static final Map<PeerID, Map<String, String>> cPQLAndStrategyToRemotePeer = Collections
			.synchronizedMap(new HashMap<PeerID, Map<String, String>>());

	private class WaitForSharedQueryIdThread extends Thread {

		private static final int NUM_RUNS = 60;
		private static final long WAIT = 1000;

		private final int mQueryId;
		private final BackupInfo mInfo;

		public WaitForSharedQueryIdThread(int queryId, BackupInfo info) {
			super("Wait for info about local query " + queryId);
			this.mQueryId = queryId;
			this.mInfo = info;
		}

		@Override
		public void run() {
			if (!cController.isPresent()) {
				LOG.error("No query part controller bound!");
				return;
			}

			ID sharedQuery = null;
			Boolean master = null;
			for (int i = 0; i < NUM_RUNS; i++) {
				if (sharedQuery == null) {
					synchronized (cController.get()) {
						sharedQuery = cController.get().getSharedQueryID(
								this.mQueryId);
					}
				}

				if (sharedQuery != null && master == null) {
					synchronized (cSharedQueryIDsForMaster) {
						master = cSharedQueryIDsForMaster.contains(sharedQuery);
						if (master) {
							cSharedQueryIDsForMaster.remove(sharedQuery);
						}
					}
				}

				if (sharedQuery != null && master != null) {
					LOG.debug("Found shared query id for local query {}: {}",
							this.mQueryId, sharedQuery);
					LOG.debug(
							"Found master/slave information for local query {}: Is master = {}",
							this.mQueryId, master);
					break;
				}

				try {
					Thread.sleep(WAIT);
				} catch (InterruptedException e) {
					LOG.error("Thread interrupted", e);
					break;
				}
			}
			backupInformationAccess.saveBackupInformation(this.mQueryId,
					this.mInfo.pql, this.mInfo.state, sharedQuery.toString(),
					master, this.mInfo.strategy);
		}

	}

	/**
	 * The peer communicator, if there is one bound.
	 */
	private static Optional<IPeerCommunicator> cCommunicator = Optional
			.absent();

	/**
	 * Binds a peer communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The peer communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IPeerCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The peer communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a peer communicator.", communicator.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to unbind must be not null!");
		if (cCommunicator.isPresent()
				&& cCommunicator.get().equals(communicator)) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", communicator
					.getClass().getSimpleName());

		}

	}

	public static void bindBackupInformationAccess(
			IBackupInformationAccess infoAccess) {
		backupInformationAccess = infoAccess;
	}

	public static void unbindBackupInformationAccess(
			IBackupInformationAccess infoAccess) {
		if (backupInformationAccess == infoAccess) {
			backupInformationAccess = null;
		}
	}

	/**
	 * The query part controller, if there is one bound.
	 */
	private static Optional<IQueryPartController> cController = Optional
			.absent();

	/**
	 * Binds a query part controller. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param controller
	 *            The query part controller to bind. <br />
	 *            Must be not null.
	 */
	public static void bindController(IQueryPartController controller) {

		Preconditions.checkNotNull(controller,
				"The query part controller to bind must be not null!");
		cController = Optional.of(controller);
		LOG.debug("Bound {} as a query part controller.", controller.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds a query part controller. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param controller
	 *            The query part controller to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindController(IQueryPartController controller) {

		Preconditions.checkNotNull(controller,
				"The query part controller to unbind must be not null!");
		if (cController.isPresent() && cController.get().equals(controller)) {

			cController = Optional.absent();
			LOG.debug("Unbound {} as a query part controller.", controller
					.getClass().getSimpleName());

		}

	}

	/**
	 * The executor, if there is one bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an executor. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param executor
	 *            The executor to bind. <br />
	 *            Must be not null.
	 */
	public void bindExecutor(IExecutor executor) {

		Preconditions.checkNotNull(executor,
				"The executor to bind must be not null!");
		IServerExecutor serverExecutor = (IServerExecutor) executor;
		serverExecutor.addPlanModificationListener(this);
		cExecutor = Optional.of(serverExecutor);
		LOG.debug("Bound {} as an executor.", executor.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds an executor. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param executor
	 *            The executor to unbind. <br />
	 *            Must be not null.
	 */
	public void unbindExecutor(IExecutor executor) {

		Preconditions.checkNotNull(executor,
				"The executor to unbind must be not null!");
		if (cExecutor.isPresent() && cExecutor.get().equals(executor)) {

			((IServerExecutor) executor).removePlanModificationListener(this);
			cExecutor = Optional.absent();
			LOG.debug("Unbound {} as an executor.", executor.getClass()
					.getSimpleName());

		}

	}

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional
			.absent();

	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to bind. <br />
	 *            Must be not null.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);
		cP2PNetworkManager = Optional.of(serv);
		LOG.debug("Bound {} as a P2P network manager.", serv.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds a P2P network manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);

		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {

			cP2PNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager.", serv.getClass()
					.getSimpleName());

		}

	}

	/*
	 * 1. Step: modifier sets strategy -> Keep in mind
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.peer.recovery.IRecoveryPreprocessorListener#
	 * setRecoveryStrategy(java.lang.String, java.util.Collection)
	 */
	@Override
	public void setRecoveryStrategy(String name,
			Collection<ILogicalQueryPart> queryParts) {
		synchronized (cStrategyToQueryPart) {
			cStrategyToQueryPart.clear();
		}
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiedParts = LogicalQueryHelper
				.copyAndCutQueryParts(queryParts, 1);
		for (ILogicalQueryPart part : copiedParts.keySet()) {
			synchronized (cStrategyToQueryPart) {
				cStrategyToQueryPart.put(copiedParts.get(part).iterator()
						.next(), name);
			}
		}
	}

	/*
	 * 2. Step: executor installs query -> (1) check, if there is a strategy set
	 * for that query (cStrategyToQueryPart) (2) create backup information with
	 * queryID, PQL, state and strategy (3) wait, if there is a shared query id
	 * available from QueryPartController
	 */
	private void afterQueryAdded(IPhysicalQuery physQuery) {
		int queryID = physQuery.getID();
		ILogicalQuery logQuery = LogicalQueryHelper.copyLogicalQuery(physQuery
				.getLogicalQuery());
		Collection<ILogicalOperator> operators = LogicalQueryHelper
				.getAllOperators(logQuery);
		LogicalQueryHelper.removeTopAOs(operators);
		ILogicalQueryPart searchedPart = new LogicalQueryPart(operators);
		Optional<String> optStrategy = findStrategy(searchedPart);
		BackupInfo info = new BackupInfo();
		info.pql = RecoveryHelper.getPQLFromRunningQuery(queryID);
		info.state = physQuery.getState().toString();
		if (optStrategy.isPresent()) {
			info.strategy = optStrategy.get();
			LOG.debug("Found recovery strategy for local query {}: {}",
					queryID, optStrategy.get());
		}
		WaitForSharedQueryIdThread thread = new WaitForSharedQueryIdThread(
				queryID, info);
		thread.start();
	}

	/*
	 * 3. Step: distributor notifies that transmission has been done -> (1) save
	 * shared query id for master determination (2) check, if there is a query
	 * part allocated to another peer, for which we have a strategy in mind and
	 * keep that in mind.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.peer.distribute.listener.
	 * AbstractQueryDistributionListener
	 * #afterTransmission(de.uniol.inf.is.odysseus
	 * .core.planmanagement.query.ILogicalQuery, java.util.Map, net.jxta.id.ID)
	 */
	@Override
	public void afterTransmission(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap, ID sharedQueryId) {
		synchronized (cSharedQueryIDsForMaster) {
			cSharedQueryIDsForMaster.add(sharedQueryId);
		}

		for (ILogicalQueryPart part : allocationMap.keySet()) {
			PeerID peer = allocationMap.get(part);
			if (!peer.equals(cP2PNetworkManager.get().getLocalPeerID())) {
				Optional<String> optStrategy = findStrategy(part);
				if (optStrategy.isPresent()) {
					String strategy = optStrategy.get();
					String pql = RecoveryHelper.convertToPQL(part);
					synchronized (cPQLAndStrategyToRemotePeer) {
						if (cPQLAndStrategyToRemotePeer.containsKey(peer)) {
							cPQLAndStrategyToRemotePeer.get(peer).put(pql,
									optStrategy.get());
						} else {
							Map<String, String> map = Maps.newHashMap();
							map.put(pql, strategy);
							cPQLAndStrategyToRemotePeer.put(peer, map);
						}
					}
				}
			}
		}
	}

	/*
	 * Step 5: DDC fires addEntry event -> if cPQLAndStrategyToRemotePeer
	 * contains PeerID and PQL, update backup information
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.peer.ddc.IDDCListener#ddcEntryAdded(de.uniol
	 * .inf.is.odysseus.peer.ddc.DDCEntry)
	 */
	@Override
	public void ddcEntryAdded(DDCEntry entry) {
		String key = entry.getKey().toString();
		Optional<PeerID> optPeer = Optional.absent();
		try {
			URI uri = URI.create(key);
			optPeer = Optional.fromNullable(PeerID.create(uri));
		} catch (Throwable t) {
			// Not a peer id
			return;
		}
		if (!optPeer.isPresent()) {
			// Not a peer id
			return;
		}

		Optional<Map<String, String>> optStrategyToPQL = Optional.absent();
		synchronized (cPQLAndStrategyToRemotePeer) {
			if (cPQLAndStrategyToRemotePeer.containsKey(optPeer.get())) {
				optStrategyToPQL = Optional.of(cPQLAndStrategyToRemotePeer
						.get(optPeer.get()));
			}
		}
		if (!optStrategyToPQL.isPresent()) {
			// Not a wanted entry
			return;
		}

		Map<Integer, BackupInfo> backupInfoMap = backupInformationAccess
				.getBackupInformation(key);
		Set<String> foundPQLs = Sets.newHashSet();
		for (int queryId : backupInfoMap.keySet()) {
			BackupInfo info = backupInfoMap.get(queryId);
			for (String pql : optStrategyToPQL.get().keySet()) {
				if(foundPQLs.contains(pql)) {
					continue;
				} else if (equalPQLIgnoringOperatorNumbersAndJxtaOperators(pql, info.pql)) {
					// Update strategy info
					backupInformationAccess.saveBackupInformation(key, queryId, info.pql,
							info.state, info.sharedQuery, info.master,
							optStrategyToPQL.get().get(pql));
					LOG.debug("Found recovery strategy for remote part {}: {}",
							pql, optStrategyToPQL.get().get(pql));
					foundPQLs.add(pql);
				}
			}
		}

		if (foundPQLs.size() == optStrategyToPQL.get().keySet().size()) {
			synchronized (cPQLAndStrategyToRemotePeer) {
				cPQLAndStrategyToRemotePeer.remove(optPeer.get());
			}
		} else {
			for (String pql : foundPQLs) {
				synchronized (cPQLAndStrategyToRemotePeer) {
					cPQLAndStrategyToRemotePeer.get(optPeer.get()).remove(pql);
				}
			}
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (!cController.isPresent()) {
			LOG.error("No query part controller bound!");
			return;
		}

		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs
				.getEventType())) {
			int queryID = ((IPhysicalQuery) eventArgs.getValue()).getID();
			backupInformationAccess.removeBackupInformation(queryID);
		} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs
				.getEventType())) {
			afterQueryAdded((IPhysicalQuery) eventArgs.getValue());
		} else {
			int queryID = ((IPhysicalQuery) eventArgs.getValue()).getID();
			QueryState state = ((IPhysicalQuery) eventArgs.getValue())
					.getState();
			String pql = backupInformationAccess.getBackupPQL(queryID);
			String sharedQuery = backupInformationAccess
					.getBackupSharedQuery(queryID);
			boolean master = backupInformationAccess.isBackupMaster(queryID);
			String strategy = backupInformationAccess
					.getBackupStrategy(queryID);
			backupInformationAccess.saveBackupInformation(queryID, pql,
					state.toString(), sharedQuery, master, strategy);
		}
	}

	private static boolean equalPQLIgnoringOperatorNumbersAndJxtaOperators(
			String pql1, String pql2) {
		String cleanedPQL1 = cleanPQLFromJxtaOperators(pql1);
		String cleanedPQL2 = cleanPQLFromJxtaOperators(pql2);
		cleanedPQL1 = cleanPQLFromOperatorNumbers(cleanedPQL1);
		cleanedPQL2 = cleanPQLFromOperatorNumbers(cleanedPQL2);
		return cleanedPQL1.equals(cleanedPQL2);
	}

	private static String cleanPQLFromJxtaOperators(String pql) {
		String cleanedPQL = pql;
		while (true) {
			int indexOfJxtaStart = cleanedPQL.indexOf("Jxta");
			if (indexOfJxtaStart == -1) {
				break;
			}

			int indexOfJxtaEnd = cleanedPQL.indexOf(")",
					indexOfJxtaStart + 1);
			cleanedPQL = cleanedPQL.substring(0, indexOfJxtaStart)
					+ cleanedPQL.substring(indexOfJxtaEnd + 1,
							cleanedPQL.length());
		}
		return cleanedPQL;
	}

	private static String cleanPQLFromOperatorNumbers(String pql) {
		String cleanedPQL = pql;
		while (true) {
			int indexOfUnderbar = cleanedPQL.indexOf('_');
			if (indexOfUnderbar == -1) {
				break;
			}

			int lastIndexOfNumeric = indexOfUnderbar;
			for (int index = indexOfUnderbar + 1; index < cleanedPQL.length(); index++) {
				if (cleanedPQL.charAt(index) >= '0'
						&& cleanedPQL.charAt(index) <= '9') {
					lastIndexOfNumeric = index;
				} else {
					break;
				}
			}
			cleanedPQL = cleanedPQL.substring(0, indexOfUnderbar)
					+ cleanedPQL.substring(lastIndexOfNumeric + 1,
							cleanedPQL.length());
		}
		return cleanedPQL;
	}

	private static Optional<String> findStrategy(ILogicalQueryPart searchedPart) {
		Collection<ILogicalQueryPart> partsToScan;
		synchronized (cStrategyToQueryPart) {
			partsToScan = cStrategyToQueryPart.keySet();
		}
		Optional<ILogicalQueryPart> foundKey = Optional.absent();
		Optional<String> foundStrategy = Optional.absent();
		for (ILogicalQueryPart part : partsToScan) {
			if (equalPartsIgnoringJxtaOperators(part, searchedPart)) {
				synchronized (cStrategyToQueryPart) {
					foundStrategy = Optional.of(cStrategyToQueryPart.get(part));
				}
			}
		}
		if (foundKey.isPresent()) {
			synchronized (cStrategyToQueryPart) {
				cStrategyToQueryPart.remove(foundKey.get());
			}
		}
		return foundStrategy;
	}

	private static boolean equalPartsIgnoringJxtaOperators(
			ILogicalQueryPart partWithoutJxtaOperators,
			ILogicalQueryPart partWithJxtaOperators) {
		for (ILogicalOperator operator1 : partWithJxtaOperators.getOperators()) {
			if (isJxtaOperator(operator1)) {
				continue;
			}
			boolean found = false;
			for (ILogicalOperator operator2 : partWithoutJxtaOperators
					.getOperators()) {
				if (equalOperatorsIngoringJxtaOperators(operator1, operator2)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	private static boolean equalOperatorsIngoringJxtaOperators(
			ILogicalOperator operator1, ILogicalOperator operator2) {
		Collection<ILogicalOperator> visitedOperators = Lists.newArrayList();
		return equalOperatorsIngoringJxtaOperatorsImpl(operator1, operator2,
				visitedOperators);
	}

	private static boolean equalOperatorsIngoringJxtaOperatorsImpl(
			ILogicalOperator operator1, ILogicalOperator operator2,
			Collection<ILogicalOperator> visitedOperators) {
		if (visitedOperators.contains(operator1)) {
			return true;
		}
		visitedOperators.add(operator1);
		if (operator1 instanceof JxtaReceiverAO) {
			return operator2.getSubscribedToSource().isEmpty();
		} else if (operator1 instanceof JxtaSenderAO) {
			return operator2.getSubscriptions().isEmpty();
		}
		return operator1.getClass() == operator2.getClass()
				&& sameParameterInfo(operator1, operator2)
				&& checkSubscriptions(operator1.getSubscribedToSource(),
						operator2.getSubscribedToSource(), visitedOperators)
				&& checkSubscriptions(operator1.getSubscriptions(),
						operator2.getSubscriptions(), visitedOperators);
	}

	private static boolean checkSubscriptions(
			Collection<LogicalSubscription> subs1,
			Collection<LogicalSubscription> subs2,
			Collection<ILogicalOperator> visitedOperators) {
		Iterator<LogicalSubscription> iter1 = subs1.iterator();
		Iterator<LogicalSubscription> iter2 = subs2.iterator();
		while (iter1.hasNext()) {
			ILogicalOperator target1 = iter1.next().getTarget();
			if (!iter2.hasNext()) {
				if (!isJxtaOperator(target1)) {
					return false;
				}
			} else if (!equalOperatorsIngoringJxtaOperatorsImpl(target1, iter2
					.next().getTarget(), visitedOperators)) {
				return false;
			}
		}
		return true;
	}

	private static boolean sameParameterInfo(ILogicalOperator operator1,
			ILogicalOperator operator2) {
		Map<String, String> info1 = operator1.getParameterInfos();
		Map<String, String> info2 = operator2.getParameterInfos();
		if (info1 == null) {
			return info2 == null;
		} else if (info1.size() != info2.size()) {
			return false;
		}
		for (String key : info1.keySet()) {
			if (!info2.keySet().contains(key)
					|| !info1.get(key).equals(info2.get(key))) {
				return false;
			}
		}
		return true;
	}

	private static boolean isJxtaOperator(ILogicalOperator operator) {
		return operator instanceof JxtaReceiverAO
				|| operator instanceof JxtaSenderAO;
	}

	@Override
	public void ddcEntryRemoved(DDCEntry ddcEntry) {
		// Nothing to do.
	}

}