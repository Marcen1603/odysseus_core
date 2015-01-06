package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

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
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.IDDCListener;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
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
	 * Keep in mind, if there has been a strategy set for any query part (its
	 * PQL).
	 */
	private static final Map<String, String> cStrategyToPQL = Collections
			.synchronizedMap(new HashMap<String, String>());

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
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiedParts = LogicalQueryHelper
				.copyAndCutQueryParts(queryParts, 1);
		for (ILogicalQueryPart part : copiedParts.keySet()) {
			String pql = LogicalQueryHelper
					.generatePQLStatementFromQueryPart(copiedParts.get(part)
							.iterator().next());
			pql = cleanPQLFromOperatorNumbers(pql);
			synchronized (cStrategyToPQL) {
				cStrategyToPQL.put(pql, name);
			}

			// synchronized (cStrategyToQueryPart) {
			// cStrategyToQueryPart.put(copiedParts.get(part).iterator()
			// .next(), name);
			// }
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
		BackupInfo info = new BackupInfo();
		info.pql = RecoveryHelper.getPQLFromRunningQuery(queryID);
		info.state = physQuery.getState().toString();
		Optional<String> optStrategy = Optional.absent();
		synchronized (cStrategyToPQL) {
			optStrategy = findStrategy(info.pql, cStrategyToPQL);
		}
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
				String pql = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(part);
				Optional<String> optStrategy = Optional.absent();
				synchronized (cStrategyToPQL) {
					optStrategy = findStrategy(pql, cStrategyToPQL);
				}
				if (optStrategy.isPresent()) {
					String strategy = optStrategy.get();
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
		synchronized (cPQLAndStrategyToRemotePeer) {
			if (!cPQLAndStrategyToRemotePeer.containsKey(optPeer.get())) {
				// Not a wanted entry
				return;
			}
		}

		Map<Integer, BackupInfo> backupInfoMap = backupInformationAccess
				.getBackupInformation(key);
		for (int queryId : backupInfoMap.keySet()) {
			BackupInfo info = backupInfoMap.get(queryId);
			Optional<String> optStrategy = Optional.absent();
			synchronized (cPQLAndStrategyToRemotePeer) {
				optStrategy = findStrategy(info.pql,
						cPQLAndStrategyToRemotePeer.get(optPeer.get()));
			}
			if (optStrategy.isPresent()) {
				// Update strategy info
				backupInformationAccess.saveBackupInformation(key, queryId,
						info.pql, info.state, info.sharedQuery, info.master,
						optStrategy.get());
				LOG.debug(
						"Found recovery strategy for local query {} of peer {}: {}",
						new Object[] { queryId, key, optStrategy.get() });
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

	private static boolean equalPQLIgnoringOperatorNumbers(String pql1,
			String pql2) {
		String cleanedPQL1 = cleanPQLFromOperatorNumbers(pql1);
		String cleanedPQL2 = cleanPQLFromOperatorNumbers(pql2);
		return cleanedPQL1.equals(cleanedPQL2);
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

	private static Optional<String> findStrategy(String pqlWithJxta,
			Map<String, String> strategiesToPQLWithoutJxta) {
		String pqlWithoutJxta = pqlWithJxta;
//		String pqlWithoutJxta = cleanPQLFromJxta(pqlWithJxta);
		// TODO Remove occurrence of JXTA operators within the PQL
		// TODO 1. idea: parse to logical query -> collect all operators except JXTA operators -> generate query part -> call copy and cut -> generate PQL
		Optional<String> foundKey = Optional.absent();
		Optional<String> foundStrategy = Optional.absent();
		for (String pql : strategiesToPQLWithoutJxta.keySet()) {
			if (equalPQLIgnoringOperatorNumbers(pql, pqlWithoutJxta)) {
				foundKey = Optional.of(pql);
				foundStrategy = Optional
						.of(strategiesToPQLWithoutJxta.get(pql));
			}
		}
		if (foundKey.isPresent()) {
			strategiesToPQLWithoutJxta.remove(foundKey.get());
		}
		return foundStrategy;
	}

	@Override
	public void ddcEntryRemoved(DDCEntry ddcEntry) {
		// Nothing to do.
	}

}