package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartControllerListener;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;

/**
 * A helper class for backup information (e.g., update of multiple stores).
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationHelper implements IPlanModificationListener, IQueryPartControllerListener, Observer {

	private static IBackupInformationAccess backupInformationAccess;
	private List<Integer> noSocketInfoQueryIds;

	public BackupInformationHelper() {
		SocketService.getInstance().addObserver(this);
		noSocketInfoQueryIds = new ArrayList<Integer>(1);
	}

	public static void bindBackupInformationAccess(IBackupInformationAccess infoAccess) {
		backupInformationAccess = infoAccess;
	}

	public static void unbindBackupInformationAccess(IBackupInformationAccess infoAccess) {
		if (backupInformationAccess == infoAccess) {
			backupInformationAccess = null;
		}
	}

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static IP2PNetworkManager cNetworkManager;

	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The P2P network manager to bind. <br />
	 *            Must be not null.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager manager) {

		Preconditions.checkNotNull(manager, "The P2P network manager to bind must be not null!");
		cNetworkManager = manager;

	}

	/**
	 * Unbinds a P2P network manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The P2P network manager to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager manager) {

		Preconditions.checkNotNull(manager, "The P2P network manager to unbind must be not null!");
		if (cNetworkManager != null && cNetworkManager.equals(manager)) {
			cNetworkManager = null;
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

		Preconditions.checkNotNull(executor, "The executor to bind must be not null!");
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

		Preconditions.checkNotNull(executor, "The executor to unbind must be not null!");
		if (cExecutor != null && cExecutor.equals(executor)) {

			((IServerExecutor) executor).removePlanModificationListener(this);
			cExecutor = null;

		}

	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {

		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
			int queryID = ((IPhysicalQuery) eventArgs.getValue()).getID();
			backupInformationAccess.removeBackupInformation(queryID);
		} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs.getEventType())) {
			IPhysicalQuery physQuery = (IPhysicalQuery) eventArgs.getValue();
			int queryID = physQuery.getID();
			String pql = RecoveryHelper.getPQLFromRunningQuery(queryID);
			String state = physQuery.getState().toString();

			// Try to get socket information
			noSocketInfoQueryIds.add(queryID);

			backupInformationAccess.saveBackupInformation(queryID, pql, state, null, false, null);
		} else {
			int queryID = ((IPhysicalQuery) eventArgs.getValue()).getID();
			QueryState state = ((IPhysicalQuery) eventArgs.getValue()).getState();
			String pql = backupInformationAccess.getBackupPQL(queryID);
			String sharedQuery = backupInformationAccess.getBackupSharedQuery(queryID);
			boolean master = backupInformationAccess.isBackupMaster(queryID);
			String masterId = backupInformationAccess.getBackupMasterId(queryID);
			backupInformationAccess
					.saveBackupInformation(queryID, pql, state.toString(), sharedQuery, master, masterId);
		}
	}

	@Override
	public void afterRegisterAsSlave(Collection<Integer> ids, ID sharedQueryID, PeerID masterPeerID) {
		for (int queryId : ids) {
			BackupInfo info = backupInformationAccess.getBackupInformation(queryId);

			backupInformationAccess.saveBackupInformation(queryId, info.pql, info.state, sharedQueryID.toString(),
					false, masterPeerID.toString());
		}
	}

	@Override
	public void afterRegisterAsMaster(ILogicalQuery query, int queryID, ID sharedQueryID, Collection<PeerID> otherPeers) {
		BackupInfo info = backupInformationAccess.getBackupInformation(queryID);
		backupInformationAccess.saveBackupInformation(queryID, info.pql, info.state, sharedQueryID.toString(), true,
				cNetworkManager.getLocalPeerID().toString());
	}

	@Override
	public void afterUnregisterAsMaster(ID sharedQueryID) {
		// TODO Auto-generated method stub
		// TODO Implement Me :)

	}

	@Override
	public void afterUnregisterAsSlave(ID sharedQueryID, Collection<Integer> ids) {
		// TODO Auto-generated method stub
		// TODO Implement Me :)
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof SocketService.ClientIpInfo) {
			SocketService.ClientIpInfo clientIpInfo = (SocketService.ClientIpInfo) arg;

			// Save info
			backupInformationAccess.addSocketInfoForQuery(clientIpInfo.queryId, clientIpInfo.clientIp);
		}
	}

}