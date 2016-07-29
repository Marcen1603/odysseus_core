package de.uniol.inf.is.odysseus.net.recovery.backupinfo.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.IBackupInformation;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.IBackupInformationManager;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

/**
 * Management of backup informations: creation, saving, access. <br />
 * This implementation automatically saves, updates and deletes backup
 * information when queries get installed, deleted or their state changed.
 * 
 * @author Michael Brand
 * @version 1.0
 */
public final class BackupInformationManager
		implements IBackupInformationManager, IPlanModificationListener, IDistributedDataListener {

	/**
	 * The bound data manager for data distribution.
	 */
	private static IDistributedDataManager dataManager;

	/**
	 * Called by OSGi
	 */
	public void bindDataManager(final IDistributedDataManager manager) {
		dataManager = manager;
		manager.addListener(this);
	}

	/**
	 * Called by OSGi
	 */
	public void unbindDataManager(final IDistributedDataManager manager) {
		if (dataManager == manager) {
			manager.removeListener(this);
			dataManager = null;
		}
	}

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
	public void bindStartupManager(IOdysseusNetStartupManager manager) throws OdysseusNetException {
		ownNodeId = manager.getLocalOdysseusNode().getID();
	}

	/**
	 * The bound PQL generator.
	 */
	private static IPQLGenerator pqlGenerator;

	/**
	 * Called by OSGi
	 */
	public void bindPQLGenerator(IPQLGenerator generator) {
		pqlGenerator = generator;
	}

	/**
	 * Called by OSGi
	 */
	public void unbindPQLGenerator(IPQLGenerator generator) {
		if (pqlGenerator == generator) {
			pqlGenerator = null;
		}
	}

	@Override
	public IBackupInformation createBackupInformation(final OdysseusNodeID nodeId, final int localQueryId,
			final String pqlStatement, final QueryState queryState) throws NullPointerException {
		// Check args
		StringBuffer argForErrorMessage = new StringBuffer("");
		if (nodeId == null) {
			argForErrorMessage.append("nodeId, ");
		}
		if (pqlStatement == null) {
			argForErrorMessage.append("pqlStatement, ");
		}
		if (queryState == null) {
			argForErrorMessage.append("queryState, ");
		}
		if (argForErrorMessage.length() > 0) {
			throw new NullPointerException(
					argForErrorMessage.substring(0, argForErrorMessage.length() - 2) + " musst be not null!");
		}

		final BackupInformation info = new BackupInformation();
		info.setNodeId(nodeId.toString());
		info.setLocalQueryId(localQueryId);
		info.setPqlStatement(pqlStatement);
		info.setQueryState(queryState.toString());
		logger.debug("Created new backup information: {}", info);
		return info;
	}

	@Override
	public IBackupInformation createBackupInformation(JSONObject json) throws NullPointerException, JSONException {
		if (json == null) {
			throw new NullPointerException("json musst be not null!");
		}
		final BackupInformation info = new BackupInformation();
		info.fromJSON(json);
		return info;
	}

	// TODO Use it
	// TODO Test it
	@Override
	public void updateBackupInformation(final IBackupInformation backupInfo, final UUID sharedQueryId,
			final OdysseusNodeID distributorId) throws NullPointerException, IllegalArgumentException {
		// Check args
		StringBuffer argForErrorMessage = new StringBuffer("");
		if (backupInfo == null) {
			argForErrorMessage.append("backupInfo, ");
		}
		if (sharedQueryId == null) {
			argForErrorMessage.append("sharedQueryId, ");
		}
		if (distributorId == null) {
			argForErrorMessage.append("distributorId, ");
		}
		if (argForErrorMessage.length() > 0) {
			throw new NullPointerException(
					argForErrorMessage.substring(0, argForErrorMessage.length() - 2) + " musst be not null!");
		}
		if (!(backupInfo instanceof BackupInformation)) {
			throw new IllegalArgumentException(backupInfo.getClass().getName() + " is not supported!");
		}

		final BackupInformation info = (BackupInformation) backupInfo;
		final BackupInformation oldInfo = ((BackupInformation) backupInfo).clone();
		info.setSharedQueryId(sharedQueryId.toString());
		info.setDistributorId(distributorId.toString());
		logger.debug("Updated backup information: {} -> {}", oldInfo, info);
	}

	@Override
	public void updateBackupInformation(IBackupInformation backupInfo, QueryState queryState)
			throws NullPointerException, IllegalArgumentException {
		// Check args
		StringBuffer argForErrorMessage = new StringBuffer("");
		if (backupInfo == null) {
			argForErrorMessage.append("backupInfo, ");
		}
		if (queryState == null) {
			argForErrorMessage.append("queryState, ");
		}
		if (argForErrorMessage.length() > 0) {
			throw new NullPointerException(
					argForErrorMessage.substring(0, argForErrorMessage.length() - 2) + " musst be not null!");
		}
		if (!(backupInfo instanceof BackupInformation)) {
			throw new IllegalArgumentException(backupInfo.getClass().getName() + " is not supported!");
		}

		final BackupInformation info = (BackupInformation) backupInfo;
		final BackupInformation oldInfo = ((BackupInformation) backupInfo).clone();
		info.setQueryState(queryState.toString());
		logger.debug("Updated backup information: {} -> {}", oldInfo, info);
	}

	/**
	 * All known backup information: outer key = node id, inner key = local
	 * query id, value = distributed data containing the backup information.
	 */
	private final Map<String, Map<Integer, IDistributedData>> backupInfosAsDistributedData = new HashMap<>();

	@Override
	public void saveBackupInformation(final IBackupInformation backupInfo)
			throws NullPointerException, DistributedDataException {
		if (backupInfo == null) {
			throw new NullPointerException("backupInfo musst be not null!");
		}
		final JSONObject json = new JSONObject(backupInfo);
		IDistributedData data = dataManager.create(json, distributedDataName, false);
		try {
			saveBackupInformation_intern(data);
		} catch (JSONException e) {
			// Should not happen
			logger.error("Could not save backup information", e);
		}
	}

	/**
	 * Adds data to {@link #backupInfosAsDistributedData}.
	 * 
	 * @throws NullPointerException
	 *             if <code>data is null</code>
	 * @throws JSONException
	 *             if the JSON object in <code>data</code> is not a valid backup
	 *             information.
	 */
	private void saveBackupInformation_intern(final IDistributedData data) throws NullPointerException, JSONException {
		if (data == null) {
			throw new NullPointerException("data musst be not null!");
		}

		final String nodeId = data.getData().getString("nodeId");
		final int localQueryId = data.getData().getInt("localQueryId");
		if (this.backupInfosAsDistributedData.containsKey(nodeId)) {
			this.backupInfosAsDistributedData.get(nodeId).put(localQueryId, data);
		} else {
			final Map<Integer, IDistributedData> mapToSave = new HashMap<>();
			mapToSave.put(localQueryId, data);
			this.backupInfosAsDistributedData.put(nodeId, mapToSave);
		}

		logger.debug("Saved backup information as distributed data object {}", data);
	}

	@Override
	public void deleteBackupInformation(IBackupInformation backupInfo)
			throws NullPointerException, DistributedDataException {
		if (backupInfo == null) {
			throw new NullPointerException("backupInfo musst be not null!");
		}
		final IllegalArgumentException exception = new IllegalArgumentException(
				"Unknown backup information: " + backupInfo);
		Map<Integer, IDistributedData> dataSet = Optional
				.ofNullable(this.backupInfosAsDistributedData.get(ownNodeId.toString())).orElseThrow(() -> exception);
		IDistributedData data = Optional.ofNullable(dataSet.remove(backupInfo.getLocalQueryId()))
				.orElseThrow(() -> exception);
		dataManager.destroy(data.getUUID());
		try {
			deleteBackupInformation_intern(data);
		} catch (JSONException e) {
			// Should not happen
			logger.error("Could not delete backup information", e);
		}
	}

	/**
	 * Removes data from {@link #backupInfosAsDistributedData}.
	 * 
	 * @throws NullPointerException
	 *             if <code>data is null</code>
	 * @throws JSONException
	 *             if the JSON object in <code>data</code> is not a valid backup
	 *             information.
	 */
	private void deleteBackupInformation_intern(final IDistributedData data)
			throws NullPointerException, JSONException {
		if (data == null) {
			throw new NullPointerException("data musst be not null!");
		}

		final String nodeId = data.getData().getString("nodeId");
		final int localQueryId = data.getData().getInt("localQueryId");
		this.backupInfosAsDistributedData.get(nodeId).remove(localQueryId);

		logger.debug("Removed backup information as distributed data object {}", data);
	}

	@Override
	public void distributedDataAdded(IDistributedDataManager sender, IDistributedData addedData) {
		if (addedData.getName().equals(distributedDataName)) {
			try {
				saveBackupInformation_intern(addedData);
			} catch (JSONException | NullPointerException e) {
				logger.error("Could not handle added data '{}'", addedData, e);
			}
		}
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData,
			IDistributedData newData) {
		if (newData.getName().equals(distributedDataName)) {
			try {
				saveBackupInformation_intern(newData);
			} catch (JSONException | NullPointerException e) {
				logger.error("Could not handle changed data '{}'", newData, e);
			}
		}
	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		if (removedData.getName().equals(distributedDataName)) {
			try {
				deleteBackupInformation_intern(removedData);
			} catch (JSONException | NullPointerException e) {
				logger.error("Could not handle removed data '{}'", removedData, e);
			}
		}
	}

	@Override
	public void distributedDataManagerStarted(IDistributedDataManager sender) {
		// Nothing to do.
	}

	@Override
	public void distributedDataManagerStopped(IDistributedDataManager sender) {
		// Nothing to do.
	}

	@Override
	public void planModificationEvent(final AbstractPlanModificationEvent<?> eventArgs) {
		if (!(eventArgs.getEventType() instanceof PlanModificationEventType)) {
			return;
		}

		final IPhysicalQuery physQuery = (IPhysicalQuery) eventArgs.getValue();
		final ILogicalQuery logQuery = physQuery.getLogicalQuery();
		final int queryId = logQuery.getID();

		switch ((PlanModificationEventType) eventArgs.getEventType()) {
		case QUERY_ADDED:
			final String pqlStatement = pqlGenerator.generatePQLStatement(logQuery.getLogicalPlan());
			final QueryState queryState = physQuery.getState();
			final IBackupInformation infoToSave = createBackupInformation(ownNodeId, queryId, pqlStatement, queryState);
			try {
				saveBackupInformation(infoToSave);
			} catch (NullPointerException | DistributedDataException e) {
				logger.error("Could not save backup information!", e);
			}
			break;
		case QUERY_REMOVE:
			// TODO does not work: LocalDistributedDataContainer adds them again after deletion.
			try {
				final IDistributedData dataToDelete = this.backupInfosAsDistributedData.get(ownNodeId.toString())
						.get(queryId);
				final IBackupInformation infoToDelete = createBackupInformation(dataToDelete.getData());
				deleteBackupInformation(infoToDelete);
			} catch (JSONException | NullPointerException | DistributedDataException e) {
				logger.error("Could not delte backup information!", e);
			}
			break;
		case QUERY_PARTIAL:
		case QUERY_RESUME:
		case QUERY_START:
		case QUERY_STOP:
		case QUERY_SUSPEND:
			// TODO does not work: saveBackupInformation is the wrong method to call here
			try {
				final IDistributedData dataToChange = this.backupInfosAsDistributedData.get(ownNodeId.toString())
						.get(queryId);
				final IBackupInformation infoToChange = createBackupInformation(dataToChange.getData());
				updateBackupInformation(infoToChange, physQuery.getState());
				saveBackupInformation(infoToChange);
			} catch (JSONException | NullPointerException | DistributedDataException e) {
				logger.error("Could not delte backup information!", e);
			}
			break;
		default:
			break;

		}

	}

}