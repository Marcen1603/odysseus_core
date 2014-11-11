package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;

public abstract class Sensor extends FieldDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	private String rawSourceName;
	private ArrayList<ActivityInterpreter> activityInterpreterList;

	/**
	 * 
	 * @return Map<String,String> <ViewName,Query>
	 */
	public abstract Map<String, String> getQueriesForRawValues();

	
	public Sensor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);

		this.setRawSourceName(name, prefix, postfix);
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

	public void addActivityInterpreter(ActivityInterpreter activityInterpreter) {
		getActivityInterpreters().add(activityInterpreter);
	}

	public boolean removeActivityInterpreter(
			ActivityInterpreter activityInterpreter) {
		return getActivityInterpreters().remove(activityInterpreter);
	}

	public ArrayList<ActivityInterpreter> getActivityInterpreters() {
		if (activityInterpreterList == null) {
			activityInterpreterList = new ArrayList<ActivityInterpreter>();
		}
		return activityInterpreterList;
	}

	public String getActivitySourceName(String activityName) {
		for (ActivityInterpreter inter : getActivityInterpreters()) {
			if (inter.getActivityName().equals(activityName)) {
				return inter.getActivitySourceName();
			}
		}
		return null;
	}


	public boolean save() throws PeerCommunicationException {
		return getSmartDevice().save();
	}
}
