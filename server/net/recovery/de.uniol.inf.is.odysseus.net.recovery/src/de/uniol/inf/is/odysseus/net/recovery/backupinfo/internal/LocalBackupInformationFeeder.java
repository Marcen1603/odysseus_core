package de.uniol.inf.is.odysseus.net.recovery.backupinfo.internal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartController;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartControllerListener;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.BackupInformationFactory;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.IBackupInformation;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.IBackupInformationManager;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

/**
 * Class that listens for {@link PlanModificationEvent}s and that creates, saves
 * or deletes backup information based on those events. <br />
 * Additionally, it listens for events of an {@link IQueryPartController} and
 * that updates backup information based on those events (shared query id,
 * distributor id).
 * 
 * @author Michael Brand
 * @version 1.0
 */
public final class LocalBackupInformationFeeder implements IPlanModificationListener, IQueryPartControllerListener {

	/**
	 * Called by OSGi
	 */
	public void bindServerExecutor(IExecutor executor) {
		if (executor instanceof IServerExecutor) {
			((IServerExecutor) executor).addPlanModificationListener(this);
		}
	}

	/**
	 * Called by OSGi
	 */
	public void unbindServerExecutor(IExecutor executor) {
		if (executor instanceof IServerExecutor) {
			((IServerExecutor) executor).removePlanModificationListener(this);
		}
	}

	/**
	 * The id of the local node.
	 */
	private static OdysseusNodeID ownNodeId;

	/**
	 * Called by OSGi
	 * 
	 * @throws OdysseusNetException
	 *             if the id of the local node could not be get.
	 */
	public static void bindStartupManager(IOdysseusNetStartupManager manager) throws OdysseusNetException {
		ownNodeId = manager.getLocalOdysseusNode().getID();
	}

	/**
	 * The bound PQL generator.
	 */
	private static IPQLGenerator pqlGenerator;

	/**
	 * Called by OSGi
	 */
	public static void bindPQLGenerator(IPQLGenerator generator) {
		pqlGenerator = generator;
	}

	/**
	 * Called by OSGi
	 */
	public static void unbindPQLGenerator(IPQLGenerator generator) {
		if (pqlGenerator == generator) {
			pqlGenerator = null;
		}
	}

	/**
	 * The bound Backup information manager.
	 */
	private static IBackupInformationManager infoManager;

	/**
	 * Called by OSGi
	 */
	public static void bindBackupInformationManager(IBackupInformationManager manager) {
		infoManager = manager;
	}

	/**
	 * Called by OSGi
	 */
	public static void unbindBackupInformationManager(IBackupInformationManager manager) {
		if (infoManager == manager) {
			infoManager = null;
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (!(eventArgs.getEventType() instanceof PlanModificationEventType)) {
			return;
		}

		final IPhysicalQuery physQuery = (IPhysicalQuery) eventArgs.getValue();
		final ILogicalQuery logQuery = physQuery.getLogicalQuery();
		final int queryId = logQuery.getID();
		final String queryState = physQuery.getState().toString();

		switch ((PlanModificationEventType) eventArgs.getEventType()) {
		case QUERY_ADDED:
			final String pqlStatement = pqlGenerator.generatePQLStatement(logQuery.getLogicalPlan());
			final IBackupInformation infoToSave = BackupInformationFactory.createEmptyBackupInformation();
			infoToSave.setNodeId(ownNodeId.toString());
			infoToSave.setLocalQueryId(queryId);
			infoToSave.setPqlStatement(pqlStatement);
			infoToSave.setQueryState(queryState);
			try {
				infoManager.saveBackupInformation(infoToSave);
			} catch (NullPointerException e) {
				IBackupInformationManager.logger.error("Could not save backup information '{}'!", infoToSave, e);
			}
			break;
		case QUERY_REMOVE:
			final IBackupInformation infoToDelete = infoManager.getBackupInformations(ownNodeId).get(queryId);
			try {
				infoManager.deleteBackupInformation(infoToDelete);
			} catch (NullPointerException | IllegalArgumentException | DistributedDataException e) {
				IBackupInformationManager.logger.error("Could not delete backup information '{}'!", infoToDelete, e);
			}
			break;
		case QUERY_PARTIAL:
		case QUERY_RESUME:
		case QUERY_START:
		case QUERY_STOP:
		case QUERY_SUSPEND:
			final IBackupInformation oldInfo = infoManager.getBackupInformations(ownNodeId).get(queryId);
			final IBackupInformation newInfo = BackupInformationFactory.createBackupInformationFromOther(oldInfo);
			newInfo.setQueryState(queryState);
			try {
				infoManager.updateBackupInformation(oldInfo, newInfo);
			} catch (NullPointerException | DistributedDataException e) {
				IBackupInformationManager.logger.error("Could not update backup information '{}'!", newInfo, e);
			}
			break;
		default:
			break;

		}
	}

	@Override
	public void afterRegisterAsMaster(ILogicalQuery query, int queryID, UUID sharedQueryID,
			Collection<IOdysseusNode> otherNodes) {
		afterRegisterAsMasterOrSlave(queryID, sharedQueryID, ownNodeId);
	}

	@Override
	public void afterRegisterAsSlave(Collection<Integer> ids, UUID sharedQueryID, IOdysseusNode masterNode) {
		for (int localQueryId : ids) {
			afterRegisterAsMasterOrSlave(localQueryId, sharedQueryID, masterNode.getID());
		}
	}

	/**
	 * Updated the backup information about a query with a shared query id and
	 * an id of the query distributor.
	 */
	private void afterRegisterAsMasterOrSlave(int localQueryId, UUID sharedQueryId, OdysseusNodeID distributorId) {
		// FIXME Does not work
		// final IBackupInformation oldInfo =
		// infoManager.getBackupInformations(ownNodeId).get(localQueryId);
		// final IBackupInformation newInfo =
		// BackupInformationFactory.createBackupInformationFromOther(oldInfo);
		// newInfo.setSharedQueryId(sharedQueryId.toString());
		// newInfo.setDistributorId(distributorId.toString());
		// try {
		// infoManager.updateBackupInformation(oldInfo, newInfo);
		// } catch (NullPointerException | DistributedDataException e) {
		// IBackupInformationManager.logger.error("Could not update backup
		// information '{}'!", newInfo, e);
		// }
	}

	@Override
	public void afterUnregisterAsMaster(UUID sharedQueryID) {
		afterUnregisterAsMasterOrSlave(sharedQueryID);
	}

	@Override
	public void afterUnregisterAsSlave(UUID sharedQueryID, Collection<Integer> ids) {
		afterUnregisterAsMasterOrSlave(sharedQueryID);
	}

	/**
	 * Updated the backup information about a query with a removed shared query
	 * id.
	 */
	private void afterUnregisterAsMasterOrSlave(UUID sharedQueryId) {
		// FIXME Does not work
		// final List<IBackupInformation> oldInfos =
		// retrieveBackupInfos(infoManager.getBackupInformations(ownNodeId),
		// sharedQueryId);
		// for (IBackupInformation oldInfo : oldInfos) {
		// final IBackupInformation newInfo =
		// BackupInformationFactory.createBackupInformationFromOther(oldInfo);
		// newInfo.setSharedQueryId(null);
		// newInfo.setDistributorId(null);
		// try {
		// infoManager.updateBackupInformation(oldInfo, newInfo);
		// } catch (NullPointerException | DistributedDataException e) {
		// IBackupInformationManager.logger.error("Could not update backup
		// information '{}'!", newInfo, e);
		// }
		// }
	}

	/**
	 * Gets all backup informations out of <code>backupInfo</code> that have
	 * <code>sharedQueryId</code> as shared query id.
	 */
	@SuppressWarnings("unused")
	private static List<IBackupInformation> retrieveBackupInfos(Map<Integer, IBackupInformation> backupInfo,
			UUID sharedQueryId) {
		return backupInfo.keySet().stream()
				.filter(localQueryId -> backupInfo.get(localQueryId).getSharedQueryId() != null
						&& backupInfo.get(localQueryId).getSharedQueryId().equals(sharedQueryId.toString()))
				.map(localQueryId -> backupInfo.get(localQueryId)).collect(Collectors.toList());
	}

}