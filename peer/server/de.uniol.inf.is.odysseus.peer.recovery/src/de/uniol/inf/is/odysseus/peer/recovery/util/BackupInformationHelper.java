package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartControllerListener;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;

/**
 * A helper class for backup information (e.g., update of multiple stores).
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationHelper implements IPlanModificationListener, IQueryPartControllerListener {

	private static IBackupInformationAccess backupInformationAccess;

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
	 * The executor, if there is one bound.
	 */
	private static IServerExecutor cExecutor;

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
		cExecutor = serverExecutor;

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
		if (cExecutor != null && cExecutor.equals(executor)) {

			((IServerExecutor) executor).removePlanModificationListener(this);
			cExecutor = null;

		}

	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs
				.getEventType())) {
			int queryID = ((IPhysicalQuery) eventArgs.getValue()).getID();
			backupInformationAccess.removeBackupInformation(queryID);
		} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs
				.getEventType())) {
			IPhysicalQuery physQuery = (IPhysicalQuery) eventArgs.getValue();
			int queryID = physQuery.getID();
			String pql = RecoveryHelper.getPQLFromRunningQuery(queryID);
			String state = physQuery.getState().toString();
			backupInformationAccess.saveBackupInformation(queryID, pql,
					state, null, false);
		} else {
			int queryID = ((IPhysicalQuery) eventArgs.getValue()).getID();
			QueryState state = ((IPhysicalQuery) eventArgs.getValue())
					.getState();
			String pql = backupInformationAccess.getBackupPQL(queryID);
			String sharedQuery = backupInformationAccess
					.getBackupSharedQuery(queryID);
			boolean master = backupInformationAccess.isBackupMaster(queryID);
			backupInformationAccess.saveBackupInformation(queryID, pql,
					state.toString(), sharedQuery, master);
		}
	}

	@Override
	public void afterRegisterAsSlave(Collection<Integer> ids, ID sharedQueryID) {
		for (int queryId : ids) {
			BackupInfo info = backupInformationAccess
					.getBackupInformation(queryId);
			backupInformationAccess.saveBackupInformation(queryId, info.pql,
					info.state, sharedQueryID.toString(), false);
		}
	}

	@Override
	public void afterRegisterAsMaster(ILogicalQuery query, int queryID,
			ID sharedQueryID, Collection<PeerID> otherPeers) {
		BackupInfo info = backupInformationAccess
				.getBackupInformation(queryID);
		backupInformationAccess.saveBackupInformation(queryID, info.pql,
				info.state, sharedQueryID.toString(), true);
	}

}