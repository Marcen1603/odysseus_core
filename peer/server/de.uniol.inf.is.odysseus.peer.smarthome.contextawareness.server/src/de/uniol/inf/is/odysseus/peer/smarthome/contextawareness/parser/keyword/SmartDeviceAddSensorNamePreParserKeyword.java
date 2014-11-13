package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.parser.keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServer;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class SmartDeviceAddSensorNamePreParserKeyword extends
		AbstractPreParserKeyword {

	public static final String KEYWORD = "SMARTDEVICE_ADD_SENSOR_NAME";

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("Parameter needed for #"
					+ KEYWORD);

		Object sensorRawSourceName = variables
				.get(SmartDeviceSensorRawSourceName.KEYWORD);
		if (sensorRawSourceName == null
				|| !(sensorRawSourceName instanceof String)
				|| sensorRawSourceName.equals("")) {
			throw new OdysseusScriptException("#"+SmartDeviceSensorRawSourceName.KEYWORD+" needed for the sensor.");
		}
		
		Object sensorQueryName = variables
				.get("QNAME");
		if (sensorQueryName == null
				|| !(sensorQueryName instanceof String)
				|| sensorQueryName.equals("")) {
			throw new OdysseusScriptException("#QNAME needed for the sensor.");
		}

		String[] splitted = parameter.trim().split(" ");
		@SuppressWarnings("unused")
		List<String> parameters = generateParameterList(splitted);

		variables.put(KEYWORD, parameter);
	}

	private List<String> generateParameterList(String[] splitted) {
		ArrayList<String> list = new ArrayList<>();

		for (int i = 0; i < splitted.length; i++) {
			list.add(splitted[i]);
		}

		return list;
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		variables.put(KEYWORD, parameter);

		String sensorName = parameter;
		final String sensorRawSourceName = (String) variables.get(SmartDeviceSensorRawSourceName.KEYWORD);
		String sensorQueryName = (String) variables.get("QNAME");
		final String queryForRawValues = "";

		LOG.debug("SensorName:" + sensorName);
		LOG.debug("SensorRawSourceName:"+sensorRawSourceName);
		LOG.debug("SensorQueryName:"+sensorQueryName);
		
		Sensor sensor = new Sensor(sensorName, "", "") {
			private static final long serialVersionUID = 1L;
			@Override
			public Map<String, String> getQueriesForRawValues() {
				Map<String,String> map = new HashMap<String, String>();
				
				map.put(sensorRawSourceName, queryForRawValues);
				
				return map;
			}
		};
		SmartDeviceServer.getInstance().getLocalSmartDevice().addConnectedFieldDevice(sensor);
		
		return null;
	}
}
