package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor;

import java.util.HashMap;
import java.util.Map;

public class ComplexSensor extends AbstractSensor {
	private static final long serialVersionUID = 1L;
	private String sensorRawSourceName;

	public ComplexSensor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);
	}

	public void setRawSourceName(String sensorRawSourceName) {
		this.sensorRawSourceName = sensorRawSourceName;
	}

	@Override
	public Map<String, String> getQueriesForRawValues() {
		Map<String, String> map = new HashMap<String, String>();

		String queryForRawValues = "";

		map.put(sensorRawSourceName, queryForRawValues);

		return map;
	}
}