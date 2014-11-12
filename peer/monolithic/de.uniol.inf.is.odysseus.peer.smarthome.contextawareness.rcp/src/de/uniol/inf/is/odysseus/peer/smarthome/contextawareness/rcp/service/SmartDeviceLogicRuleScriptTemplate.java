package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.service;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class SmartDeviceLogicRuleScriptTemplate implements
		IOdysseusScriptTemplate {

	@Override
	public String getName() {
		return "SmartDevice LogicRule Script Template";
	}

	@Override
	public String getDescription() {
		return "SmartDevice LogicRule Script Template description";
	}

	@Override
	public String getText() {
		StringBuilder sb = new StringBuilder();
		sb.append("///SmartDevice LogicRule Script Template\n");
		sb.append("\n");
		sb.append("#SMARTDEVICE_LOGIC_RULE_NAME <logic rule name>");
		sb.append("\n");
		sb.append("///Query:\n");
		sb.append("\n");
		
		return sb.toString();
	}

}
