package de.uniol.inf.is.odysseus.net.data.console;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class DistributedDataConsole implements CommandProvider {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat();

	private static IDistributedDataManager distributedDataManager;

	// called by OSGi-DS
	public static void bindDistributedDataManager(IDistributedDataManager serv) {
		distributedDataManager = serv;
		if (!distributedDataManager.isLocal()) {
			distributedDataManager.addListener(new IDistributedDataListener() {

				@Override
				public void distributedDataAdded(IDistributedDataManager manager, IDistributedData addedData) {
					System.err.println("Remotely added distributed data: " + addedData);
				}

				@Override
				public void distributedDataModified(IDistributedDataManager manager, IDistributedData oldData, IDistributedData newData) {
					System.err.println("Remotely modified distributed data: " + oldData + " to " + newData);
				}

				@Override
				public void distributedDataRemoved(IDistributedDataManager manager, IDistributedData removedData) {
					System.err.println("Remotely removed distributed data: " + removedData);
				}

				@Override
				public void distributedDataManagerStarted(IDistributedDataManager sender) {
					System.err.println("Distributed data manager started");
				}

				@Override
				public void distributedDataManagerStopped(IDistributedDataManager sender) {
					System.err.println("Distributed data manager stopped");
				}
			});
		}
	}

	// called by OSGi-DS
	public static void unbindDistributedDataManager(IDistributedDataManager serv) {
		if (distributedDataManager == serv) {
			distributedDataManager = null;
		}
	}

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Distributed Data Commands ---\n");
		sb.append("  listDistributedData/ls... <filter>                      - Lists stored data in distributed data container\n");
		sb.append("  describeDistributedData/desc... <ddid | ddname>         - Prints more details about a specific distributed data element\n");
		sb.append("  isDistributedDataLocal                                  - Shows if distributed data is used locally\n");
		sb.append("  listDistributedDataUUIDs/ls...                          - Lists all UUIDs of distributed data\n");
		sb.append("  listDistributedDataNames/ls...                          - Lists all names of distributed data\n");
		sb.append("  isDistributedData <ddid | ddname>                       - Shows if the identifier is reserved for distributed data\n");
		return sb.toString();
	}

	public void _listDistributedData(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		try {
			Collection<String> names = distributedDataManager.getNames();
			if (names.isEmpty()) {
				ci.println("Distributed data container is empty");
				return;
			}

			for (String name : names) {

				if (Strings.isNullOrEmpty(filter) || name.contains(filter)) {
					Collection<IDistributedData> datas = distributedDataManager.get(name);
					if (!datas.isEmpty()) {
						ci.println(name);
						for (IDistributedData data : datas) {
							ci.print("\t");
							ci.print(data.getUUID());
							ci.print(":");
							ci.println(data.getData().toString());
						}
						ci.println();
					}
				}
			}

		} catch (DistributedDataException e) {
			ci.println("Could not determine distributed data: " + e);
			ci.printStackTrace(e);
		}
	}

	public void _lsDistributedData(CommandInterpreter ci) {
		_listDistributedData(ci);
	}

	public void _describeDistributedData(CommandInterpreter ci) {
		String dataText = ci.nextArgument();
		if (Strings.isNullOrEmpty(dataText)) {
			ci.println("usage: describeDistributedData <ddname | ddid>");
			return;
		}

		Collection<IDistributedData> distributedDatas = determineDistributedData(dataText, ci);
		if (!distributedDatas.isEmpty()) {
			for (IDistributedData distributedData : distributedDatas) {
				printDistributedDataDetails(ci, distributedData);
				ci.println();
			}
		}
	}

	private static Collection<IDistributedData> determineDistributedData(String dataText, CommandInterpreter ci) {
		try {
			Optional<UUID> optUUID = tryToUUID(dataText);
			if (optUUID.isPresent()) {
				UUID distributedDataUUID = optUUID.get();

				Optional<IDistributedData> optDistributedData = distributedDataManager.get(distributedDataUUID);
				if (optDistributedData.isPresent()) {
					return Lists.newArrayList(optDistributedData.get());
				}
				ci.println("Distributed data with UUID " + distributedDataUUID + " not found");
				return Lists.newArrayList();

			}

			Collection<IDistributedData> distributedDatas = distributedDataManager.get(dataText);
			if (!distributedDatas.isEmpty()) {
				return distributedDatas;
			}
			ci.println("There is no distributed data with name " + dataText);
		} catch (DistributedDataException e) {
			ci.println("Could not determine distributed data: " + e);
			ci.printStackTrace(e);
		}
		return Lists.newArrayList();
	}

	private static void printDistributedDataDetails(CommandInterpreter ci, IDistributedData distributedData) {
		ci.println("UUID          : " + distributedData.getUUID());
		ci.println("Name          : " + distributedData.getName());
		ci.println("Creator NodeID: " + distributedData.getCreator());
		ci.println("Persistent    : " + distributedData.isPersistent());
		ci.println("Lifetime      : " + distributedData.getLifetimeMillis());
		ci.println("Timestamp     : " + distributedData.getTimestamp() + " (" + convertTimestampToDate(distributedData.getTimestamp()) + ")");
		ci.println("Data          : " + distributedData.getData());
	}

	private static Optional<UUID> tryToUUID(String text) {
		try {
			return Optional.of(UUID.fromString(text));
		} catch (IllegalArgumentException ex) {
			return Optional.absent();
		}
	}

	private static String convertTimestampToDate(long startupTimestamp) {
		return DATE_FORMAT.format(new Date(startupTimestamp));
	}

	public void _descDistributedData(CommandInterpreter ci) {
		_describeDistributedData(ci);
	}

	public void _createDistributedData(CommandInterpreter ci) {
		String name = ci.nextArgument();
		if (Strings.isNullOrEmpty(name)) {
			ci.println("usage: createDistributedData <name> <someText>");
			return;
		}

		String text = ci.nextArgument();
		if (Strings.isNullOrEmpty(text)) {
			ci.println("usage: createDistributedData <name> <someText>");
			return;
		}
		
		new Thread("DataDistributionThread") {
			
			@Override
			public void run() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("text", text);

					IDistributedData createdDistributedData = distributedDataManager.create(obj, name, false);

					ci.println("Distributed data created. UUID is " + createdDistributedData.getUUID());

				} catch (JSONException | DistributedDataException e) {
					ci.println("Could not create distributed data: " + e.getMessage());
					ci.printStackTrace(e);
				}
			}
		}.start();
	}

	public void _destroyDistributedData(CommandInterpreter ci) {
		String dataText = ci.nextArgument();
		if (Strings.isNullOrEmpty(dataText)) {
			ci.println("usage: destroyDistributedData <ddname | ddid>");
			return;
		}

		Optional<UUID> optUUID = tryToUUID(dataText);
		if (optUUID.isPresent()) {
			UUID distributedDataUUID = optUUID.get();

			try {
				Optional<IDistributedData> optDistributedData = distributedDataManager.destroy(distributedDataUUID);
				if (optDistributedData.isPresent()) {
					ci.println("Distributed data destroyed");
				} else {
					ci.println("No distributed data destroyed");
				}
			} catch (DistributedDataException e) {
				ci.println("Could not destroy distributed data: " + e);
				ci.printStackTrace(e);
			}
		} else {
			try {
				Collection<IDistributedData> destroyedDistributedDatas = distributedDataManager.destroy(dataText);
				if (!destroyedDistributedDatas.isEmpty()) {
					ci.println("Destroyed " + destroyedDistributedDatas.size() + " distributed data");
					for (IDistributedData destroyedDistributedData : destroyedDistributedDatas) {
						ci.println("\t" + destroyedDistributedData.getUUID());
					}
				} else {
					ci.println("There is no distributed data with name " + dataText);
				}
			} catch (DistributedDataException e) {
				ci.println("Could not destroy distributed data: " + e);
				ci.printStackTrace(e);
			}
		}
	}
	
	public void _destroyAllDistributedData( CommandInterpreter ci ) {
		try {
			Collection<IDistributedData> destroyedDistributedData = distributedDataManager.destroyOwn();
			if( !destroyedDistributedData.isEmpty() ) {
				ci.println("Destroyed " + destroyedDistributedData.size() + " distributed data:");
				for( IDistributedData data : destroyedDistributedData ) {
					ci.println("\t" + data.getUUID());
				}
			} else {
				ci.println("No distributed data destroyed");
			}
		} catch (DistributedDataException e) {
			ci.println("Could not destroy own distributed data: " + e);
			ci.printStackTrace(e);
		}
	}

	public void _isDistributedDataLocal(CommandInterpreter ci) {
		ci.println(distributedDataManager.isLocal());
	}

	public void _lsDistributedDataUUIDs(CommandInterpreter ci) {
		try {
			Collection<UUID> uuids = distributedDataManager.getUUIDs();
			if (!uuids.isEmpty()) {
				for (UUID uuid : uuids) {
					ci.println("\t" + uuid.toString());
				}
			} else {
				ci.println("No distributed data with uuids available");
			}
		} catch (DistributedDataException e) {
			ci.println("Could not determine list of distributed data UUIDs" + e);
			ci.printStackTrace(e);
		}
	}

	public void _listDistributedDataUUIDs(CommandInterpreter ci) {
		_lsDistributedDataUUIDs(ci);
	}

	public void _lsDistributedDataNames(CommandInterpreter ci) {
		try {
			Collection<String> names = distributedDataManager.getNames();
			if (!names.isEmpty()) {
				for (String name : names) {
					ci.println(name);
				}
			} else {
				ci.println("No distributed data with names available");
			}
		} catch (DistributedDataException e) {
			ci.println("Could not determine list of distributed data names " + e);
			ci.printStackTrace(e);
		}
	}

	public void _listDistributedDataNames(CommandInterpreter ci) {
		_listDistributedDataNames(ci);
	}

	public void _lsDistributedDataNodeIDs(CommandInterpreter ci) {
		try {
			Collection<OdysseusNodeID> nodeIDs = distributedDataManager.getOdysseusNodeIDs();
			if (!nodeIDs.isEmpty()) {
				for (OdysseusNodeID nodeID : nodeIDs) {
					ci.println("\t" + nodeID.toString());
				}
			} else {
				ci.println("No distributed data with specified node id available");
			}
		} catch (DistributedDataException e) {
			ci.println("Could not determine list of distributed data node ids" + e);
			ci.printStackTrace(e);
		}
	}

	public void _listDistributedDataNodeIDs(CommandInterpreter ci) {
		_lsDistributedDataNodeIDs(ci);
	}

	public void _isDistributedData(CommandInterpreter ci) {
		String dataText = ci.nextArgument();
		if (Strings.isNullOrEmpty(dataText)) {
			ci.println("usage: isDistributedData <ddid|ddname>");
			return;
		}

		Optional<UUID> optUUID = tryToUUID(dataText);
		if (optUUID.isPresent()) {
			UUID uuid = optUUID.get();
			try {
				ci.println(distributedDataManager.containsUUID(uuid));
			} catch (DistributedDataException e) {
				ci.println("Could not determine if specified uuid is present: " + e);
				ci.printStackTrace(e);
			}
		} else {
			try {
				ci.println(distributedDataManager.containsName(dataText));
			} catch (DistributedDataException e) {
				ci.println("Could not determine if specified name is present: " + e);
				ci.printStackTrace(e);
			}
		}
	}

}
