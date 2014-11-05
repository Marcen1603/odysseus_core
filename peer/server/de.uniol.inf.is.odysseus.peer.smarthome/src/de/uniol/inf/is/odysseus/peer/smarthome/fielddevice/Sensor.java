package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Sensor implements FieldDevice, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String rawSourceName;
	private String postfix;
	private String prefix;
	
	private ArrayList<ActivityInterpreter> activityInterpreterList;
	private SmartDevice smartDevice;

	public Sensor(String name, String prefix, String postfix) {
		this.setName(name);
		this.setRawSourceName(name, prefix, postfix);
		this.setPrefix(prefix);
		this.setPostfix(postfix);
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	public String getPostfix() {
		return this.postfix;
	}
	public void setSmartDevice(SmartDevice smartDevice) {
		this.smartDevice = smartDevice;
	}

	public SmartDevice getSmartDevice(){
		return this.smartDevice;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRawSourceName() {
		return rawSourceName;
	}

	private void setRawSourceName(String sourceName, String prefix,
			String postfix) {
		String rawSourceName = "";
		if (prefix != null && !prefix.equals("")) {
			rawSourceName += prefix + "_";
		}

		rawSourceName += "rawValues_" + sourceName + "";

		if (postfix != null && !postfix.equals("")) {
			rawSourceName += "_" + postfix;
		}

		this.rawSourceName = rawSourceName;
	}

	public abstract Map<String, String> getQueriesForRawValues();

	@Override
	public List<String> getPossibleActivityNames() {
		ArrayList<String> list = new ArrayList<String>();
		
		for(ActivityInterpreter interpreter : getActivityInterpreters()){
			if(!list.contains(interpreter.getActivityName())){
				list.add(interpreter.getActivityName());
			}
		}
		
		return list;
	}

	@Override
	public String toString() {
		return getName();
	}

	public abstract String getActivitySourceName(String possibleActivityName);

	public void addActivityInterpreter(ActivityInterpreter activityInterpreter) {
		getActivityInterpreters().add(activityInterpreter);
	}

	public void removeActivityInterpreter(ActivityInterpreter activityInterpreter) {
		getActivityInterpreters().remove(activityInterpreter);
	}

	public ArrayList<ActivityInterpreter> getActivityInterpreters() {
		if (activityInterpreterList == null) {
			activityInterpreterList = new ArrayList<ActivityInterpreter>();
		}
		return activityInterpreterList;
	}
}
