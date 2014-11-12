package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.service;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class SmartDeviceSensorScriptTemplate implements IOdysseusScriptTemplate {

	@Override
	public String getName() {
		return "SmartDevice Sensor Template";
	}

	@Override
	public String getDescription() {
		return "SmartDevice Sensor Template description";
	}

	@Override
	public String getText() {
		return "///SmartDevice Sensor Template";
	}

}
