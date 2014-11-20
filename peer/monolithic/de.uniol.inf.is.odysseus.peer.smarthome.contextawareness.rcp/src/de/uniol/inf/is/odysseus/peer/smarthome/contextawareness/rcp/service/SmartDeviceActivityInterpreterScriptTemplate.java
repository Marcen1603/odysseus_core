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
		sb.append("#DEFINE sensorname newSensor1\n");
		sb.append("#DEFINE activityinterpretername hot\n");
		sb.append("#DEFINE peername Raspberry\n");
		sb.append("\n");
		sb.append("#SMARTDEVICE_SENSOR_NAME newSensor1\n");
		sb.append("#QNAME ${activityinterpretername}_query\n");
		sb.append("#SMARTDEVICE_ACTIVITY_INTERPRETER_SOURCE_NAME ${activityinterpretername}_source\n");
		sb.append("#SMARTDEVICE_ADD_ACTIVITY_INTERPRETER_NAME ${activityinterpretername}\n");
		sb.append("\n");
		sb.append("\n");
		sb.append("#PARSER PQL\n");
		sb.append("#QNAME ${activityinterpretername}_query\n");
		sb.append("#RUNQUERY\n");
		sb.append("${activityinterpretername}_source := RENAME({\n");
		sb.append("                    aliases = ['EntityName', 'ActivityName']\n");                 
		sb.append("                  },\n");
		sb.append("                  MAP({\n");
		sb.append("                      expressions = ['EntityName','concat(substring(toString(Temperature),0,0),\"${activityinterpretername}\")']\n");                                                                       
		sb.append("                    },SELECT({\n");
		sb.append("                predicate='Temperature > 18'\n");
		sb.append("              },\n");
		sb.append("                ${sensorname}_source\n");
		sb.append("              )))\n");
		sb.append("\n");
		
		return sb.toString();
	}

}
