package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.listener.AbstractQueryDistributionListener;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryPreprocessorListener;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;

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

	private static final Map<ID, ILogicalQuery> cSharedQueryIds = Collections
			.synchronizedMap(new HashMap<ID, ILogicalQuery>());

	private static final Map<ILogicalQuery, String> cStrategies = Collections
			.synchronizedMap(new HashMap<ILogicalQuery, String>());

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
					}
				}

				if (sharedQuery != null && strategy == null) {
					ILogicalQuery query = null;
					synchronized (cSharedQueryIds) {
						query = cSharedQueryIds.get(sharedQuery);
						cSharedQueryIds.remove(sharedQuery);
					}
					if (query != null) {
						synchronized (cStrategies) {
							strategy = cStrategies.get(query);
							cStrategies.remove(query);
						}
					}
				}

				if (sharedQuery != null && master != null) {
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
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional
			.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a recovery communicator.", communicator
				.getClass().getSimpleName());

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
			String strategy = backupInformationAccess.getBackupStrategy(queryID);
			backupInformationAccess.saveBackupInformation(queryID, pql,
					state.toString(), sharedQuery, master, strategy);

		}

	}

	@Override
	public void afterTransmission(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap, ID sharedQueryId) {
		// Called after setRecoveryStrategy
		synchronized (cSharedQueryIds) {
			cSharedQueryIds.put(sharedQueryId, query);
		}
	}

	@Override
	public void setRecoveryStrategy(String name, ILogicalQuery query) {
		// Called before afterTransmission
		synchronized (cStrategies) {
			cStrategies.put(query, name);
		}
	}
}