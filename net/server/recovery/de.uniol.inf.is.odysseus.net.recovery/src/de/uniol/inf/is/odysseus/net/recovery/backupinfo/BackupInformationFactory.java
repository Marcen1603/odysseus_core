package de.uniol.inf.is.odysseus.net.recovery.backupinfo;

import org.json.JSONException;
import org.json.JSONObject;

import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.recovery.backupinfo.internal.BackupInformation;

/**
 * Factory to create instances of {@link IBackupInformation}.
 * 
 * @author Michael Brand
 * @version 1.0
 */
public final class BackupInformationFactory {

	/**
	 * Creates a new empty backup information object.
	 */
	public static IBackupInformation createEmptyBackupInformation() {
		return new BackupInformation();
	}

	/**
	 * Creates a new backup information from JSON object that was distributed by
	 * the {@link IDistributedDataManager}.
	 */
	public static IBackupInformation createBackupInformationFromJSON(JSONObject json) throws JSONException {
		return BackupInformation.fromJSON(json);
	}

	/**
	 * Creates a copy if an existing backup information object.
	 */
	public static IBackupInformation createBackupInformationFromOther(IBackupInformation other) {
		return ((BackupInformation) other).clone();
	}

}