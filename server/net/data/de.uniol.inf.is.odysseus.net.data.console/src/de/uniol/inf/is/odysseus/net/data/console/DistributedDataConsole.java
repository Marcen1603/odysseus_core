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

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class DistributedDataConsole implements CommandProvider {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat();

	private static IDistributedDataManager distributedDataManager;

	// called by OSGi-DS
	public static void bindDistributedDataManager(IDistributedDataManager serv) {
		distributedDataManager = serv;
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
		return sb.toString();
	}

	public void _listDistributedData(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		if (distributedDataManager == null) {
			ci.println("Distributed data manager not available");
			return;
		}

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
	}

	public void _lsDistributedData(CommandInterpreter ci) {
		_listDistributedData(ci);
	}

	public void _describeDistributedData(CommandInterpreter ci) {
		if (distributedDataManager == null) {
			ci.println("Distributed data manager not available");
			return;
		}

		String dataText = ci.nextArgument();
		if (Strings.isNullOrEmpty(dataText)) {
			ci.println("usage: describeDistributedData <ddname | ddid>");
			return;
		}

		Collection<IDistributedData> distributedDatas = determineDistributedData(dataText, ci);
		if (!distributedDatas.isEmpty()) {
			ci.println("There are " + distributedDatas.size() + " entries with name " + dataText);
			ci.println();
			for (IDistributedData distributedData : distributedDatas) {
				printDistributedDataDetails(ci, distributedData);
				ci.println();
			}
		}
	}

	private static Collection<IDistributedData> determineDistributedData(String dataText, CommandInterpreter ci) {
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
		return Lists.newArrayList();
	}

	private static void printDistributedDataDetails(CommandInterpreter ci, IDistributedData distributedData) {
		ci.println("UUID          : " + distributedData.getUUID());
		ci.println("Name          : " + distributedData.getName());
		ci.println("Creater NodeID: " + distributedData.getCreator());
		ci.println("Persistent    : " + distributedData.isPersistent());
		ci.println("Lifetime      : " + distributedData.getLifetime());
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
		if (distributedDataManager == null) {
			ci.println("Distributed data manager not available");
			return;
		}

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

		try {
			JSONObject obj = new JSONObject();
			obj.put("text", text);

			IDistributedData createdDistributedData = distributedDataManager.create(obj, name, false);

			ci.println("Distributed data created. UUID is " + createdDistributedData.getUUID());

		} catch (JSONException e) {
			ci.println("Could not create distributed data: " + e.getMessage());
			ci.printStackTrace(e);
		}
	}

	public void _destroyDistributedData(CommandInterpreter ci) {
		if (distributedDataManager == null) {
			ci.println("Distributed data manager not available");
			return;
		}

		String dataText = ci.nextArgument();
		if (Strings.isNullOrEmpty(dataText)) {
			ci.println("usage: destroyDistributedData <ddname | ddid>");
			return;
		}

		Collection<IDistributedData> distributedDatas = determineDistributedData(dataText, ci);
		if (!distributedDatas.isEmpty()) {
			int count = 0;
			for( IDistributedData distributedData : distributedDatas ) {
				if( distributedDataManager.destroy(distributedData.getUUID()).isPresent() ) {
					count++;
				}
			}
			if( count > 0 ) {
				ci.println("Destroyed " + count + " distribted data");
			} else {
				ci.println("No distributed data destroyed");
			}
		}
	}
}
