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
		sb.append("#QNAME ${activityinterpretername}_filteredRawValues_query\n");
		sb.append("#RUNQUERY\n");
		sb.append("${activityinterpretername}_filteredRawValues := SELECT({\n");
		sb.append("                predicate='Temperature > 18'\n");
		sb.append("              },\n");
		sb.append("                ${sensorname}_source\n");
		sb.append("              )\n");
		sb.append("              \n");
		sb.append("              \n");
		sb.append("#QNAME ${activityinterpretername}_ActivityConfiguration_query\n");
		sb.append("#RUNQUERY\n");
		sb.append("    ${activityinterpretername}_ActivityConfiguration := \n");
		sb.append("	 ACCESS({\n");
		sb.append("      transport = 'activityconfiguration',\n");
		sb.append("      source = '${activityinterpretername}_ActivityConfiguration',\n");
		sb.append("      datahandler = 'Tuple',\n");
		sb.append("      wrapper = 'GenericPull',\n");
		sb.append("      protocol='none',\n");
		sb.append("      options=[\n");
		sb.append("        ['entity', 'temper'],\n");
		sb.append("        ['activity', 'hot']\n");
		sb.append("      ],\n");
		sb.append("      schema=[\n");
		sb.append("        ['EntityName', 'String'],\n");
		sb.append("        ['ActivityName', 'String']\n");
		sb.append("      ]\n");
		sb.append("      }\n");
		sb.append("    )\n");
		sb.append("    \n");
		sb.append("    \n");
		sb.append("#QNAME ${activityinterpretername}_query\n");
		sb.append("#RUNQUERY\n");
		sb.append("${activityinterpretername}_source := PROJECT({\n");
		sb.append("    attributes = ['${activityinterpretername}_filteredRawValues.EntityName', 'ActivityName']},\n");
		sb.append(" JOIN({\n");
		sb.append("                  predicate = '${activityinterpretername}_filteredRawValues.EntityName = ${activityinterpretername}_ActivityConfiguration.EntityName'\n");
		sb.append("                },\n");
		sb.append("                ELEMENTWINDOW({size = 1}, ${activityinterpretername}_filteredRawValues),\n");
		sb.append("                ELEMENTWINDOW({size = 1}, ${activityinterpretername}_ActivityConfiguration)\n");
		sb.append("              )\n");
		sb.append("          )\n");
		sb.append("          \n");
		sb.append("          \n");
		
		return sb.toString();
	}

}
