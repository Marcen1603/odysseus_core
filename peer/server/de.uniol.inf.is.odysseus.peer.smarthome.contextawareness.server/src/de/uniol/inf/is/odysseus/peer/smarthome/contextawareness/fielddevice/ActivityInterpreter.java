package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.io.Serializable;
import java.util.HashMap;

public abstract class ActivityInterpreter implements Serializable {
	private static final long serialVersionUID = 1L;
	private Sensor sensor;
	private String activityName;
	private String prefix;
	private String postfix;
	private String activitySourceName;
	
	public ActivityInterpreter(Sensor _sensor, String _activityName, String prefix, String postfix) {
		this.setSensor(_sensor);
		this.setActivityName(_activityName);
		this.setPrefix(prefix);
		this.setPostfix(postfix);
		
		String activitySourceName = getNameCombination("Activity",
				getActivityName());
		setActivitySourceName(activitySourceName);
	}
	
	private void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	private String getPrefix() {
		return this.prefix;
	}

	private void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	private String getPostfix() {
		return this.postfix;
	}
	public String getActivityName(){
		return this.activityName;
	}
	private void setActivityName(String activityName){
		this.activityName = activityName;
	}
	public abstract HashMap<String, String> getActivityInterpreterQueries(
			String activityName);
	public Sensor getSensor() {
		return sensor;
	}
	private void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
	public void setActivitySourceName(String _activitySourceName) {
		this.activitySourceName = _activitySourceName;
	}
	
	public String getActivitySourceName() {
		return this.activitySourceName;
	}
	protected String getNameCombination(String name, String activity) {
		StringBuilder n = new StringBuilder();
		if (getPrefix() != null && !getPrefix().equals("")) {
			n.append(getPrefix() + "_");
		}
		n.append(activity + "_" + name);

		if (getPostfix() != null && !getPostfix().equals("")) {
			n.append("_" + getPostfix());
		}

		return n.toString();
	}

	public abstract String getActivityInterpreterDescription();

	
}
