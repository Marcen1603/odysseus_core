package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.parser.keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.ComplexSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class SmartDeviceAddSensorNamePreParserKeyword extends
		AbstractPreParserKeyword {

	public static final String KEYWORD = "SMARTDEVICE_ADD_SENSOR_NAME";

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("Parameter needed for #"
					+ KEYWORD);

		Object sensorRawSourceName = variables
				.get(SmartDeviceSensorRawSourceNamePreParserKeyword.KEYWORD);
		if (sensorRawSourceName == null
				|| !(sensorRawSourceName instanceof String)
				|| sensorRawSourceName.equals("")) {
			throw new OdysseusScriptException("#"
					+ SmartDeviceSensorRawSourceNamePreParserKeyword.KEYWORD
					+ " needed for the sensor.");
		}

		Object sensorQueryName = variables.get("QNAME");
		if (sensorQueryName == null || !(sensorQueryName instanceof String)
				|| sensorQueryName.equals("")) {
			throw new OdysseusScriptException("#QNAME needed for the sensor.");
		}

		String[] splitted = parameter.trim().split(" ");
		@SuppressWarnings("unused")
		List<String> parameters = generateParameterList(splitted);

		variables.put(KEYWORD, parameter);
	}

	private static List<String> generateParameterList(String[] splitted) {
		ArrayList<String> list = new ArrayList<>();

		for (int i = 0; i < splitted.length; i++) {
			list.add(splitted[i]);
		}

		return list;
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		variables.put(KEYWORD, parameter);

		String sensorName = parameter;
		final String sensorRawSourceName = (String) variables
				.get(SmartDeviceSensorRawSourceNamePreParserKeyword.KEYWORD);
		String sensorQueryName = (String) variables.get("QNAME");
		//final String queryForRawValues = "";

		LOG.debug("SensorName:" + sensorName);
		LOG.debug("SensorRawSourceName:" + sensorRawSourceName);
		LOG.debug("SensorQueryName:" + sensorQueryName);

		try {
			ComplexSensor sensor = new ComplexSensor(sensorName, "", "");
			sensor.setRawSourceName(sensorRawSourceName);

			SmartDevicePublisher.getInstance().getLocalSmartDevice()
					.addConnectedFieldDevice(sensor);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
		
		return null;
	}

}
