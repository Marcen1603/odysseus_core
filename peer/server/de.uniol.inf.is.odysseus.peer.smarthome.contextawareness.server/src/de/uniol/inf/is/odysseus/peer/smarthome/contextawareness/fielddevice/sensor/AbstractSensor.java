package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.IActivityInterpreterListener;

public abstract class AbstractSensor extends FieldDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	private String rawSourceName;
	private ArrayList<ActivityInterpreter> activityInterpreterList;
	private transient ArrayList<IActivityInterpreterListener> activityInterpreterListeners;

	/**
	 * 
	 * @return Map<String,String> <ViewName,Query>
	 */
	public abstract Map<String, String> getQueriesForRawValues();

	
	public AbstractSensor(String name, String prefix, String postfix) {
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

	public boolean addActivityInterpreter(ActivityInterpreter activityInterpreter) {
		if(activityInterpreterForActivityNameExist(activityInterpreter.getActivityName())){
			return false;
		}
		
		if(getActivityInterpreters().add(activityInterpreter)){
			getSmartDevice().updateActivitiesSources();
			fireActivityInterpreterAdded(activityInterpreter);
			return true;
		}else{
			return false;
		}
	}

	private boolean activityInterpreterForActivityNameExist(String activityName) {
		for(ActivityInterpreter interpreter : getActivityInterpreters()){
			if(interpreter.getActivityName().equals(activityName)){
				return true;
			}
		}
		return false;
	}

	public boolean removeActivityInterpreter(
			ActivityInterpreter activityInterpreter) {
		if(getActivityInterpreters().remove(activityInterpreter)){
			getSmartDevice().updateActivitiesSources();
			fireActivityInterpreterRemoved(activityInterpreter);
			return true;
		}else{
			return false;
		}
	}

	private void fireActivityInterpreterAdded(
			ActivityInterpreter activityInterpreter) {
		for(IActivityInterpreterListener listener : getActivityInterpreterListeners()){
			listener.activityInterpreterAdded(activityInterpreter);
		}
	}
	
	private void fireActivityInterpreterRemoved(
			ActivityInterpreter activityInterpreter) {
		for(IActivityInterpreterListener listener : getActivityInterpreterListeners()){
			listener.activityInterpreterRemoved(activityInterpreter);
		}
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

	public void addActivityInterpreterListener(IActivityInterpreterListener fieldDeviceListener) {
		getActivityInterpreterListeners().add(fieldDeviceListener);
	}
	
	public void removeActivityInterpreterListener(IActivityInterpreterListener fieldDeviceListener) {
		getActivityInterpreterListeners().remove(fieldDeviceListener);
	}

	protected ArrayList<IActivityInterpreterListener> getActivityInterpreterListeners() {
		if(activityInterpreterListeners==null){
			activityInterpreterListeners = new ArrayList<IActivityInterpreterListener>();
		}
		return activityInterpreterListeners;
	}

	public boolean save() throws PeerCommunicationException {
		return getSmartDevice().save();
	}


	public abstract boolean createActivityInterpreterWithCondition(String activityName,
			String activityInterpreterCondition);


	public abstract ArrayList<String> getPossibleAttributes();
}
