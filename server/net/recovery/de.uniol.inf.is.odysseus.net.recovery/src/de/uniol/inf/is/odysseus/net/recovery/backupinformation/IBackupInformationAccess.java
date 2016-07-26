package de.uniol.inf.is.odysseus.net.recovery.backupinformation;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.net.data.DistributedDataException;

public interface IBackupInformationAccess {

	/**
	 * Gets backup information about a node. The key of the return value is the
	 * local query id.
	 * @throws DistributedDataException 
	 */
	public HashMap<Integer, BackupInformation> getBackupInformation(String nodeId) throws DistributedDataException;

}