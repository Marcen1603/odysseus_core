package de.uniol.inf.is.odysseus.net.recovery.backupinfo;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

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
public interface IBackupInformationManager {

	/**
	 * The logger for this interface and it's implementations.
	 */
	public static final Logger logger = LoggerFactory.getLogger(IBackupInformationManager.class);

	/**
	 * The name of distributed data that are backup informations. See
	 * {@link IDistributedData#getName()}.
	 */
	public static final String distributedDataName = "net.recovery.backupinformation";

	/**
	 * Checks if backup information about a node are available.
	 */
	public boolean containsInformationAbout(OdysseusNodeID nodeId);

	/**
	 * Gets all backup information about a node mapped to their local query id.
	 */
	public Map<Integer, IBackupInformation> getBackupInformations(OdysseusNodeID nodeId);

	/**
	 * Saves a backup information as {@link IDistributedData} and shares it with
	 * all other nodes.
	 * 
	 * @throws NullPointerException
	 *             if <code>backupInfo</code> is null
	 */
	public void saveBackupInformation(final IBackupInformation backupInfo)
			throws NullPointerException;

	/**
	 * Saves changes of a backup information as {@link IDistributedData} and
	 * shares it with all other nodes.
	 * 
	 * @throws NullPointerException
	 *             if <code>oldBackupInfo</code> or <code>newBackupInfo</code>
	 *             is null
	 * @throws DistributedDataException
	 *             See
	 *             {@link IDistributedDataManager#create(org.json.JSONObject, String, boolean)}.
	 */
	public void updateBackupInformation(final IBackupInformation oldBackupInfo, final IBackupInformation newBackupInfo)
			throws NullPointerException, DistributedDataException;

	/**
	 * Deletes an existing backup information.
	 * 
	 * @throws NullPointerException
	 *             if <code>backupInfo</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>backupInfo</code> is unknown.
	 * @throws DistributedDataException
	 *             See {@link IDistributedDataManager#destroy(UUID)}.
	 */
	public void deleteBackupInformation(final IBackupInformation backupInfo)
			throws NullPointerException, IllegalArgumentException, DistributedDataException;

}