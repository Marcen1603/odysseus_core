package de.uniol.inf.is.odysseus.net.logging;

import org.json.JSONObject;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentAdapter;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class OdysseusNetLoggingComponent extends OdysseusNetComponentAdapter implements IOdysseusNetComponent {

	private static final String IS_RECEIVING_LOGGING_CONFIG_KEY = "net.logging.receive";
	private static final boolean IS_RECEIVING_LOGGING_DEFAULT_VALUE = false;
	public static final boolean IS_RECEIVING_LOGGING = determineIsReceivingLogging();

	public static final String LOGGING_DISTRIBUTED_DATA_NAME = "net.logging.nodes";

	private static IDistributedDataListener logDestinationListener;
	private static LogMessageListener logMessageListener;
	private static OdysseusNetLogAppender appender;

	private static IDistributedDataManager dataManager;
	private static IOdysseusNodeCommunicator communicator;

	// called by OSGi-DS
	public static void bindDistributedDataManager(IDistributedDataManager serv) {
		dataManager = serv;
	}

	// called by OSGi-DS
	public static void unbindDistributedDataManager(IDistributedDataManager serv) {
		if (dataManager == serv) {
			removeListeners();

			dataManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		communicator = serv;
		
		communicator.registerMessageType(LogMessage.class);
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (communicator == serv) {
			communicator.unregisterMessageType(LogMessage.class);
			
			communicator = null;
		}
	}

	@Override
	public void start() throws OdysseusNetException {
		if (IS_RECEIVING_LOGGING) {
			new Thread("SourceDistributionThread") {

				@Override
				public void run() {
					try {
						dataManager.create(new JSONObject(), LOGGING_DISTRIBUTED_DATA_NAME, false);
						logMessageListener = new LogMessageListener(communicator);
					} catch (DistributedDataException e) {
						e.printStackTrace();
					}
				}
			}.start();

		} else {

			// we want to send our log messages to other nodes!
			logDestinationListener = new LogDestinationDistributedDataListener();

			dataManager.addListener(logDestinationListener);

			appender = new OdysseusNetLogAppender(communicator);
			org.apache.log4j.Logger.getRootLogger().addAppender(appender);
		}
	}

	@Override
	public void stop() {
		removeListeners();
	}

	private static void removeListeners() {
		if (logDestinationListener != null && dataManager != null) {
			dataManager.removeListener(logDestinationListener);

			logDestinationListener = null;
		}

		if (logMessageListener != null) {
			logMessageListener.dispose();

			logMessageListener = null;
		}

		if (appender != null) {
			org.apache.log4j.Logger.getRootLogger().removeAppender(appender);

			appender = null;
		}
	}

	private static boolean determineIsReceivingLogging() {
		String isReceivingStr = OdysseusNetConfiguration.get(IS_RECEIVING_LOGGING_CONFIG_KEY, "" + IS_RECEIVING_LOGGING_DEFAULT_VALUE);
		return tryToBoolean(isReceivingStr, IS_RECEIVING_LOGGING_DEFAULT_VALUE);
	}

	private static boolean tryToBoolean(String isReceivingStr, boolean isReceivingLoggingDefaultValue) {
		try {
			return Boolean.valueOf(isReceivingStr);
		} catch (Throwable t) {
			return isReceivingLoggingDefaultValue;
		}
	}
}
