package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.listener.AbstractQueryDistributionListener;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryPreprocessorListener;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryStrategySender;

// TODO Die ganze Klasse gefällt mir nicht. Es ist, glaube ich, nicht zweckmäßig (nur) auf den lokalen Distributor zu horchen. 
// Es muss auch möglich sein, die Backup Informationen zentral vom Distributor aus zu erstellen. M.B.

/**
 * A helper class for backup information (e.g., update of multiple stores).
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationHelper extends AbstractQueryDistributionListener
		implements IPlanModificationListener, IRecoveryPreprocessorListener {

	/**
	 * The logger instance for this class.
	 */
	private static Logger LOG = LoggerFactory
			.getLogger(BackupInformationHelper.class);

	private static IBackupInformationAccess backupInformationAccess;

	/**
	 * Keep those query ids in mind, for which the local peer is the master
	 */
	private static final Map<ID, ILogicalQuery> cSharedQueryIds = Collections
			.synchronizedMap(new HashMap<ID, ILogicalQuery>());

	/**
	 * Keep the chosen recovery strategies for those query parts in mind, for
	 * which a strategy has been set.
	 */
	private static final Map<ILogicalQueryPart, String> cStrategiesToPart = Maps
			.newHashMap();

	/**
	 * Keep the chosen recovery strategies for those shared query ids in mind,
	 * for which a strategy has been set and which are to be executed locally.
	 */
	private static final Map<ID, String> cStrategiesToID = Collections
			.synchronizedMap(new HashMap<ID, String>());

	/**
	 * Keep the local query ids in mind.
	 */
	private static final Map<String, Integer> cLocalQueryIdToPQL = Collections
			.synchronizedMap(new HashMap<String, Integer>());

	private static final int NUM_RUNS = 60;
	private static final long WAIT = 1000;

	private class WaitForSharedQueryIdThread extends Thread {

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
			String strategy = null;
			for (int i = 0; i < NUM_RUNS; i++) {
				if (sharedQuery == null) {
					synchronized (cController.get()) {
						sharedQuery = cController.get().getSharedQueryID(
								this.mQueryId);
					}
				}

				if (sharedQuery != null && master == null) {
					synchronized (cSharedQueryIds) {
						master = cSharedQueryIds.keySet().contains(sharedQuery);
						if (master) {
							cSharedQueryIds.remove(sharedQuery);
						}
					}
				}

				if (sharedQuery != null && strategy == null) {
					synchronized (cStrategiesToID) {
						strategy = cStrategiesToID.get(sharedQuery);
						cStrategiesToID.remove(sharedQuery);
					}
				}

				if (sharedQuery != null && master != null) {
					LOG.debug("Found shared query id for local query {}: {}",
							this.mQueryId, sharedQuery);
					LOG.debug(
							"Found master/slave information for local query {}: Is master = {}",
							this.mQueryId, master);
					if (strategy != null)
						LOG.debug(
								"Found recovery strategy for local query {}: {}",
								this.mQueryId, strategy);
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
					master, strategy);
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
			int queryID = ((IPhysicalQuery) eventArgs.getValue()).getID();
			BackupInfo info = new BackupInfo();
			info.pql = RecoveryHelper.getPQLFromRunningQuery(queryID);
			info.state = ((IPhysicalQuery) eventArgs.getValue()).getState()
					.toString();
			cLocalQueryIdToPQL.put(info.pql, queryID);
			WaitForSharedQueryIdThread thread = new WaitForSharedQueryIdThread(
					queryID, info);
			thread.start();
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

	private static ILogicalQueryPart findPart(
			Collection<ILogicalQueryPart> parts, ILogicalQueryPart part) {
		// Only for parts with id
		if (part.getID() == -1) {
			return null;
		}
		for (ILogicalQueryPart p : parts) {
			if (part.getID() == p.getID()) {
				return p;
			}
		}
		return null;
	}

	@Override
	public void afterTransmission(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap, ID sharedQueryId) {
		// Called after setRecoveryStrategy
		synchronized (cSharedQueryIds) {
			cSharedQueryIds.put(sharedQueryId, query);
		}

		Collection<ILogicalQueryPart> processedParts = Lists.newArrayList();
		for (ILogicalQueryPart part : cStrategiesToPart.keySet()) {
			ILogicalQueryPart allocatedPart = findPart(allocationMap.keySet(),
					part);
			if (allocatedPart == null) {
				continue;
			} else if (allocationMap.get(allocatedPart).equals(
					cP2PNetworkManager.get().getLocalPeerID())) {
				// Local part
				synchronized (cStrategiesToID) {
					cStrategiesToID.put(sharedQueryId,
							cStrategiesToPart.get(part));
				}
				processedParts.add(part);
			} else {
				// Remote part
				RecoveryStrategySender.getInstance().sendRecoveryStrategy(
						allocationMap.get(allocatedPart), allocatedPart,
						cStrategiesToPart.get(part), cCommunicator.get());
				processedParts.add(part);
			}
		}
		for (ILogicalQueryPart part : processedParts) {
			cStrategiesToPart.remove(part);
		}
	}

	@Override
	public void setRecoveryStrategy(String name,
			Collection<ILogicalQueryPart> queryParts) {
		// Called before afterTransmission
		for (ILogicalQueryPart part : queryParts) {
			cStrategiesToPart.put(part, name);
		}
	}

	public static void setRecoveryStrategy(final String name, final String pql) {
		// Called from another peer via message
		Thread t = new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < NUM_RUNS; i++) {
					int queryId = -1;
					synchronized (cLocalQueryIdToPQL) {
						if (cLocalQueryIdToPQL.containsKey(pql)) {
							queryId = cLocalQueryIdToPQL.get(pql);
						}
					}
					if (queryId != -1) {
						BackupInfo info = backupInformationAccess
								.getBackupInformation().get(queryId);
						backupInformationAccess.saveBackupInformation(queryId,
								pql, info.state, info.sharedQuery, info.master,
								name);
						synchronized (cLocalQueryIdToPQL) {
							cLocalQueryIdToPQL.remove(pql);
						}
						break;
					}
					try {
						Thread.sleep(WAIT);
					} catch (InterruptedException e) {
						LOG.error("Thread interrupted", e);
						break;
					}
				}
			}
		};
		t.start();
	}

}