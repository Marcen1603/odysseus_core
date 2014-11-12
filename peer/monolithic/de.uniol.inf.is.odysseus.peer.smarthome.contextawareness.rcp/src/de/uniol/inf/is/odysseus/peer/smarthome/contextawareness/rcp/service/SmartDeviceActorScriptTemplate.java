package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.service;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class SmartDeviceActorScriptTemplate implements IOdysseusScriptTemplate {

	@Override
	public String getName() {
		return "SmartDevice Actor Script Template";
	}

	@Override
	public String getDescription() {
		return "SmartDevice Actor Script Template description";
	}

	@Override
	public String getText() {
		StringBuilder sb = new StringBuilder();
		sb.append("///SmartDevice Actor Script Template\n");
		sb.append("\n");
		sb.append("#SMARTDEVICE_ACTOR_NAME <actor name>");
		sb.append("\n");
		sb.append("///Query:\n");
		sb.append("\n");
		
		return sb.toString();
	}

}
