package de.uniol.inf.is.odysseus.net.recovery.backupinfo;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

/**
 * Management of backup informations: creation, saving, access. <br />
 * The implementation of this interface automatically saves, updates and deletes
 * backup information when queries get installed, deleted or their state
 * changed.
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
	 * Creates a new backup information.
	 * 
	 * @param nodeId
	 *            The id of the node on that the query is installed.
	 * @param localQueryId
	 *            The local id of the query.
	 * @param pqlStatement
	 *            The PQL statement for the query.
	 * @param queryState
	 *            The State of the query.
	 * @throws NullPointerException
	 *             if any parameter is null.
	 */
	public IBackupInformation createBackupInformation(final OdysseusNodeID nodeId, final int localQueryId,
			final String pqlStatement, final QueryState queryState) throws NullPointerException;

	/**
	 * Creates a new backup information from JSON.
	 * 
	 * @throws NullPointerException
	 *             if <code>json<code> is null.
	 * @throws JSONException if <code>json</code> could not be transformed to a
	 *             valid backup information.
	 */
	public IBackupInformation createBackupInformation(final JSONObject json) throws NullPointerException, JSONException;

	/**
	 * Updates an existing backup information by setting the shared query id and
	 * the id of the node that distributed the query. <br />
	 * Only for distributed queries.
	 * 
	 * @throws NullPointerException
	 *             if any parameter is null.
	 * @throws IllegalArgumentException
	 *             if <code>backupInfo</code> is an object of a not supported
	 *             class.
	 */
	public void updateBackupInformation(final IBackupInformation backupInfo, final UUID sharedQueryId,
			final OdysseusNodeID distributorId) throws NullPointerException, IllegalArgumentException;

	/**
	 * Updates an existing backup information by changing the query state.
	 * 
	 * @throws NullPointerException
	 *             if any parameter is null.
	 * @throws IllegalArgumentException
	 *             if <code>backupInfo</code> is an object of a not supported
	 *             class.
	 */
	public void updateBackupInformation(final IBackupInformation backupInfo, final QueryState queryState)
			throws NullPointerException, IllegalArgumentException;

	/**
	 * Saves a backup information as {@link IDistributedData} and shares it with
	 * all other nodes.
	 * 
	 * @throws NullPointerException
	 *             if <code>backupInfo</code> is null
	 * @throws DistributedDataException
	 *             See
	 *             {@link IDistributedDataManager#create(org.json.JSONObject, String, boolean)}.
	 */
	public void saveBackupInformation(final IBackupInformation backupInfo)
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