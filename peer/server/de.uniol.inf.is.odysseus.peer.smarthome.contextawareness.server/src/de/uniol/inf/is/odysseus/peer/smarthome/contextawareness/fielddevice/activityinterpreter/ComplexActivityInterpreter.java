package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.Sensor;

public class ComplexActivityInterpreter extends ActivityInterpreter {
	private static final long serialVersionUID = 1L;

	public ComplexActivityInterpreter(Sensor _sensor, String _activityName,
			String prefix, String postfix) {
		super(_sensor, _activityName, prefix, postfix);

	}

	@Override
	public HashMap<String, String> getActivityInterpreterQueries(
			String activityName) {
		HashMap<String, String> map = new HashMap<>();

		String query = "";
		
		map.put(getActivitySourceName(), query);

		return map;
	}
	
	@Override
	public String getActivityInterpreterDescription() {
		return "ActivityInterpreter:" + getActivityName();
	}
}
