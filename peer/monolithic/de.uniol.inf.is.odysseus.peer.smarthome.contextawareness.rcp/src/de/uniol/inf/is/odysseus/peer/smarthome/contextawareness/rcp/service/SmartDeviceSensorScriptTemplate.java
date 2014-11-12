package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.service;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class SmartDeviceSensorScriptTemplate implements IOdysseusScriptTemplate {

	@Override
	public String getName() {
		return "SmartDevice Sensor Script Template";
	}

	@Override
	public String getDescription() {
		return "SmartDevice Sensor Script Template description";
	}

	@Override
	public String getText() {
		StringBuilder sb = new StringBuilder();
		sb.append("///SmartDevice Sensor Script Template\n");
		sb.append("\n");
		sb.append("#SMARTDEVICE_SENSOR_NAME <sensor name>");
		sb.append("\n");
		sb.append("///Query for raw values:\n");
		sb.append("\n");
		
		return sb.toString();
	}

}
