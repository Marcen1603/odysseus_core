package de.uniol.inf.is.odysseus.net.recovery.backupinformation.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.recovery.backupinformation.BackupInformation;
import de.uniol.inf.is.odysseus.net.recovery.backupinformation.IBackupInformationAccess;

public class BackupInformationAccess implements IBackupInformationAccess {

	private static final String ddcKey = "recovery.data.backupinfo";

	private static IDistributedDataManager dataManager;

	public static IDistributedDataManager bindDataManager() {
		return dataManager;
	}

	public static void unbindDataManager(IDistributedDataManager dataManager) {
		BackupInformationAccess.dataManager = dataManager;
	}

	@Override
	public HashMap<Integer, BackupInformation> getBackupInformation(String nodeId) throws DistributedDataException {
		UUID id = buildDDCId(nodeId);
		HashMap<Integer, BackupInformation> retVal = new HashMap<>();
		Optional<IDistributedData> data = dataManager.get(id);
		if (data.isPresent()) {
			try {
				// Key must be local query id
				Map<String, Object> value = JSONtoMap(data.get().getData());
				value.keySet().stream().forEach(key -> {
					try {
						retVal.put(Integer.valueOf(key), BackupInformation.fromJSON((JSONObject) value));
					} catch (NumberFormatException | JSONException e) {
						e.printStackTrace();
					}
				});
			} catch (JSONException e) {
				throw new DistributedDataException(e);
			}
		}
		return retVal;
	}

	private static UUID buildDDCId(String nodeId) {
		return UUID.fromString(ddcKey + "_" + nodeId);
	}

	private static Map<String, Object> JSONtoMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			map.put(key, object.get(key));
		}
		return map;
	}

}