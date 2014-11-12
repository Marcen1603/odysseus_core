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
		sb.append("///SmartDevice Sensor Template\n");
		sb.append("\n");
		sb.append("#DEFINE sensorname newSensor1\n");
		sb.append("#PARSER PQL\n");
		sb.append("#QNAME ${sensorname}_query\n");
		sb.append("#SMARTDEVICE_SENSOR_NAME ${sensorname}\n");
		sb.append("#SMARTDEVICE_SENSOR_RAW_SOURCE_NAME ${sensorname}_source\n");
		sb.append("#SMARTDEVICE_ADD_SENSOR_NAME ${sensorname}\n");
		sb.append("\n");
		sb.append("#RUNQUERY\n");
		sb.append("${sensorname}_source := TEMPER1ACCESS({SOURCE = 'temper1', TEMPNUMBER = 0})\n");
		sb.append("\n");
		
		
		//sb.append("\n");
		//sb.append("#SMARTDEVICE_SENSOR_NAME <sensor name>");
		//sb.append("\n");
		//sb.append("///Query for raw values:\n");
		//sb.append("\n");
		
		return sb.toString();
	}

}
