package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.service;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class SmartDeviceActivityInterpreterScriptTemplate implements
		IOdysseusScriptTemplate {

	@Override
	public String getName() {
		return "SmartDevice ActivityInterpreter Script Template";
	}

	@Override
	public String getDescription() {
		return "SmartDevice ActivityInterpreter Script Template description.";
	}

	@Override
	public String getText() {
		StringBuilder sb = new StringBuilder();
		sb.append("///SmartDevice ActivityInterpreter Script Template\n");
		sb.append("\n");
		sb.append("#SMARTDEVICE_ACTIVITY_INTERPRETER_NAME <activity interpreter name>");
		sb.append("\n");
		sb.append("///Query:\n");
		sb.append("\n");
		
		return sb.toString();
	}

}
