package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.parser.keyword;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ComplexActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class SmartDeviceAddActivityInterpreterNamePreParserKeyword extends
		AbstractPreParserKeyword implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String KEYWORD = "SMARTDEVICE_ADD_ACTIVITY_INTERPRETER_NAME";

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("Parameter needed for #"
					+ KEYWORD);

		Object sensorQueryName = variables.get("QNAME");
		if (sensorQueryName == null || !(sensorQueryName instanceof String)
				|| sensorQueryName.equals("")) {
			throw new OdysseusScriptException(
					"#QNAME needed for the activity interpreter.");
		}

		Object sensorName = variables
				.get(SmartDeviceSensorNamePreParserKeyword.KEYWORD);
		if (sensorName == null || !(sensorName instanceof String)
				|| sensorName.equals("")) {
			throw new OdysseusScriptException("#"
					+ SmartDeviceSensorNamePreParserKeyword.KEYWORD
					+ " needed for the activity interpreter.");
		}

		Object activityInterpreterSourceName = variables
				.get(SmartDeviceActivityInterpreterSourceNamePreParserKeyword.KEYWORD);
		if (activityInterpreterSourceName == null
				|| !(activityInterpreterSourceName instanceof String)
				|| activityInterpreterSourceName.equals("")) {
			throw new OdysseusScriptException(
					"#"
							+ SmartDeviceActivityInterpreterSourceNamePreParserKeyword.KEYWORD
							+ " needed for the activity interpreter.");
		}

		variables.put(KEYWORD, parameter);
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		variables.put(KEYWORD, parameter);

		final String sensorName = (String) variables
				.get(SmartDeviceSensorNamePreParserKeyword.KEYWORD);
		// String activityInterpreterQueryName = (String)
		// variables.get("QNAME");
		final String activityInterpreterName = parameter;
		final String activityInterpreterSourceName = (String) variables
				.get(SmartDeviceActivityInterpreterSourceNamePreParserKeyword.KEYWORD);
		//final String activityInterpreterQuery = "";

		// TODO: create new activity interpreter and connect it to the sensor
		LOG.debug("create new activity interpreter: " + activityInterpreterName);
		LOG.debug("SourceName:" + activityInterpreterSourceName);
		LOG.debug("sensorName:" + sensorName);

		try {
			AbstractSensor sensor = SmartDevicePublisher.getInstance()
					.getLocalSmartDevice().getSensor(sensorName);

			ComplexActivityInterpreter interpreter = new ComplexActivityInterpreter(
					sensor, activityInterpreterName, "", "");
			interpreter.setActivitySourceName(activityInterpreterSourceName);
			sensor.addActivityInterpreter(interpreter);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}

		return null;
	}
}
