package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;
import java.util.HashMap;

public abstract class ActivityInterpreter implements Serializable {
	private static final long serialVersionUID = 1L;
	private Sensor sensor;
	private String activityName;
	private String prefix;
	private String postfix;
	
	public ActivityInterpreter(Sensor _sensor, String _activityName, String prefix, String postfix) {
		this.setSensor(_sensor);
		this.setActivityName(_activityName);
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
	public String getActivityName(){
		return this.activityName;
	}
	public void setActivityName(String activityName){
		this.activityName = activityName;
	}
	public abstract HashMap<String, String> getActivityInterpreterQueries(
			String activityName);
	public abstract String getActivitySourceName();
	public Sensor getSensor() {
		return sensor;
	}
	private void setSensor(Sensor sensor) {
		this.sensor = sensor;
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
}
