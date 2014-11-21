package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;

public class ActivityInterpreterProcessor  {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static ActivityInterpreterProcessor instance = new ActivityInterpreterProcessor();

	private ActivityInterpreterProcessor() {

	}

	void executeActivityInterpreterQueries(ASmartDevice smartDevice) {
		for (Sensor sensor : smartDevice.getConnectedSensors()) {
			for (Entry<String, String> queryForRawValue : sensor
					.getQueriesForRawValues().entrySet()) {
				String viewName = queryForRawValue.getKey();
				String query = queryForRawValue.getValue();

				try {
					QueryExecutor.getInstance()
							.executeQueryNow(viewName, query);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

			for (ActivityInterpreter activityInterpreter : sensor
					.getActivityInterpreters()) {
				String activityName = activityInterpreter.getActivityName();
				String activitySourceName = activityInterpreter
						.getActivitySourceName();

				for (Entry<String, String> entry : activityInterpreter
						.getActivityInterpreterQueries(activityName).entrySet()) {
					String viewName = entry.getKey();
					String query = entry.getValue();

					try {
						QueryExecutor.getInstance().executeQueryNow(viewName,
								query);

					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}

				try {
					QueryExecutor.getInstance().exportWhenPossibleAsync(
							activitySourceName);
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}

	public synchronized static ActivityInterpreterProcessor getInstance() {
		return instance;
	}


}
