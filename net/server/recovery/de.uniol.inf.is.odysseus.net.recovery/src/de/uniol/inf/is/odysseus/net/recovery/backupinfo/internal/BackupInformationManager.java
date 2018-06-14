package de.uniol.inf.is.odysseus.net.recovery.backupinfo.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.BackupInformationFactory;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.IBackupInformation;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.IBackupInformationManager;

/**
 * Management of backup informations: saving, deletion and access. <br />
 * Saving and deletion distributes that information automatically with help of
 * {@link IDistributedDataManager}. With the same mechanism, the backup
 * information manager retrieves savings and deletions of backup information on
 * other nodes. So every backup information manager has all backup informations
 * of all reachable nodes.
 * 
 * @author Michael Brand
 * @version 1.0
 */
public final class BackupInformationManager implements IBackupInformationManager, IDistributedDataListener {

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
	 * All known backup information: outer key = node id, inner key = local
	 * query id, value = the backup information.
	 */
	private final Map<OdysseusNodeID, Map<Integer, IBackupInformation>> knownBackupInfos = new HashMap<>();

	/**
	 * All used distributed data ids mapped the backup information packed in the
	 * data object.
	 */
	private final Map<IBackupInformation, UUID> usedDistributedDataIds = new HashMap<>();

	@Override
	public boolean containsInformationAbout(OdysseusNodeID nodeId) {
		return this.knownBackupInfos.containsKey(nodeId);
	}

	@Override
	public Map<Integer, IBackupInformation> getBackupInformations(OdysseusNodeID nodeId) {
		return this.knownBackupInfos.get(nodeId);
	}

	@Override
	public void saveBackupInformation(final IBackupInformation backupInfo) throws NullPointerException {
		if (backupInfo == null) {
			throw new NullPointerException("backupInfo musst be not null!");
		}
		
		new Thread(() -> {
			try {
				final JSONObject json = new JSONObject(backupInfo);
				UUID dataId = dataManager.create(json, distributedDataName, false).getUUID();
				saveBackupInformation_intern(backupInfo, dataId);
			} catch (DistributedDataException e) {
				logger.error("Could not save backup information!", e);
			}
		}).start();
	}

	/**
	 * Adds backup info to {@link #knownBackupInfos} and
	 * {@link #usedDistributedDataIds}.
	 * 
	 * @throws NullPointerException
	 *             if <code>backupInfo</code> or <code>distributedDataId</code>
	 *             is null.
	 */
	private void saveBackupInformation_intern(final IBackupInformation backupInfo, final UUID distributedDataId)
			throws NullPointerException {
		if (backupInfo == null) {
			throw new NullPointerException("backupInfo musst be not null!");
		} else if (distributedDataId == null) {
			throw new NullPointerException("distributedDataId musst be not null!");
		}

		final OdysseusNodeID nodeId = OdysseusNodeID.fromString(backupInfo.getNodeId());
		final int localQueryId = backupInfo.getLocalQueryId();
		if (this.knownBackupInfos.containsKey(nodeId)) {
			this.knownBackupInfos.get(nodeId).put(localQueryId, backupInfo);
		} else {
			final Map<Integer, IBackupInformation> mapToSave = new HashMap<>();
			mapToSave.put(localQueryId, backupInfo);
			this.knownBackupInfos.put(nodeId, mapToSave);
		}
		this.usedDistributedDataIds.put(backupInfo, distributedDataId);
		logger.debug("Saved backup information {}", backupInfo);
	}

	@Override
	public void updateBackupInformation(final IBackupInformation oldBackupInfo, IBackupInformation newBackupInfo)
			throws NullPointerException, DistributedDataException {
		if (oldBackupInfo == null) {
			throw new NullPointerException("oldBackupInfo musst be not null!");
		} else if (newBackupInfo == null) {
			throw new NullPointerException("newBackupInfo musst be not null!");
		}
		
		Optional<UUID> dataId = Optional.ofNullable(usedDistributedDataIds.get(oldBackupInfo));
		if(dataId.isPresent()) {
			new Thread(() -> {
				try {
					final JSONObject json = new JSONObject(newBackupInfo);
					dataManager.update(dataId.get(), json);
					updateBackupInformationInternal(oldBackupInfo, newBackupInfo, dataId.get());
				} catch (DistributedDataException e) {
					logger.error("Could not update backup information!", e);
				}
			}).start();
		} else {
			logger.warn(
					"Calling updateBackupInformation with an unknown oldBackupInfo. saveBackupInformation(newBackupInfo) will be called.");
			saveBackupInformation(newBackupInfo);
		}
	}

	/**
	 * Replaces backup info in {@link #knownBackupInfos} and
	 * {@link #usedDistributedDataIds}.
	 * 
	 * @throws NullPointerException
	 *             if any argument is null.
	 */
	private void updateBackupInformationInternal(IBackupInformation oldBackupInfo, IBackupInformation newBackupInfo,
			UUID distributedDataId) {
		if (oldBackupInfo == null) {
			throw new NullPointerException("oldBackupInfo musst be not null!");
		} else if (newBackupInfo == null) {
			throw new NullPointerException("newBackupInfo musst be not null!");
		} else if (distributedDataId == null) {
			throw new NullPointerException("distributedDataId musst be not null!");
		}

		final OdysseusNodeID nodeId = OdysseusNodeID.fromString(oldBackupInfo.getNodeId());
		final int localQueryId = oldBackupInfo.getLocalQueryId();
		this.knownBackupInfos.get(nodeId).put(localQueryId, newBackupInfo);
		this.usedDistributedDataIds.remove(oldBackupInfo);
		this.usedDistributedDataIds.put(newBackupInfo, distributedDataId);
		logger.debug("Updated backup information {} to {}", oldBackupInfo, newBackupInfo);
	}

	@Override
	public void deleteBackupInformation(IBackupInformation backupInfo)
			throws NullPointerException, IllegalArgumentException, DistributedDataException {
		if (backupInfo == null) {
			throw new NullPointerException("backupInfo musst be not null!");
		} else if (!this.usedDistributedDataIds.containsKey(backupInfo)) {
			throw new IllegalArgumentException("Unknown backup information: " + backupInfo);
		}
		
		new Thread(() -> {
			try {
				dataManager.destroy(usedDistributedDataIds.get(backupInfo));
				deleteBackupInformation_intern(backupInfo);
			} catch (DistributedDataException e) {
				logger.error("Could not delete backup information!", e);
			}
		}).start();
	}

	/**
	 * Removes backup info from {@link #knownBackupInfos} and
	 * {@link #usedDistributedDataIds}.
	 * 
	 * @throws NullPointerException
	 *             if <code>backupInfo is null</code>
	 */
	private void deleteBackupInformation_intern(final IBackupInformation backupInfo) throws NullPointerException {
		if (backupInfo == null) {
			throw new NullPointerException("data musst be not null!");
		}

		final OdysseusNodeID nodeId = OdysseusNodeID.fromString(backupInfo.getNodeId());
		final int localQueryId = backupInfo.getLocalQueryId();
		if (this.knownBackupInfos.containsKey(nodeId)) {
			this.knownBackupInfos.get(nodeId).remove(localQueryId);
			if (this.knownBackupInfos.get(nodeId).isEmpty()) {
				this.knownBackupInfos.remove(nodeId);
			}
		}
		this.usedDistributedDataIds.remove(backupInfo);
		logger.debug("Removed backup information {}", backupInfo);
	}

	@Override
	public void distributedDataAdded(IDistributedDataManager sender, IDistributedData addedData) {
		if (addedData.getName().equals(distributedDataName)) {
			try {
				final UUID dataId = addedData.getUUID();
				final IBackupInformation backupInfo = BackupInformationFactory
						.createBackupInformationFromJSON(addedData.getData());
				saveBackupInformation_intern(backupInfo, dataId);
			} catch (JSONException e) {
				logger.error("Could not handle added data '{}'", addedData, e);
			}
		}
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData,
			IDistributedData newData) {
		if (newData.getName().equals(distributedDataName)) {
			try {
				final UUID newDataId = newData.getUUID();
				final IBackupInformation oldBackupInfo = BackupInformationFactory
						.createBackupInformationFromJSON(oldData.getData());
				final IBackupInformation newBackupInfo = BackupInformationFactory
						.createBackupInformationFromJSON(newData.getData());
				deleteBackupInformation_intern(oldBackupInfo);
				saveBackupInformation_intern(newBackupInfo, newDataId);
			} catch (JSONException e) {
				logger.error("Could not handle changed data '{}'", newData, e);
			}
		}

	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		if (removedData.getName().equals(distributedDataName)) {
			try {
				final IBackupInformation backupInfo = BackupInformationFactory
						.createBackupInformationFromJSON(removedData.getData());
				deleteBackupInformation_intern(backupInfo);
			} catch (JSONException e) {
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

}
