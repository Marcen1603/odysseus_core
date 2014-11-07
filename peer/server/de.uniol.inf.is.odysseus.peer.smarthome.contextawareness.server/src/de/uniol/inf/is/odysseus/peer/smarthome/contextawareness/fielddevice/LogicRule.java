package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class LogicRule implements Serializable {
	private static final long serialVersionUID = 1L;
	private String activityName;
	private Actor actor;
	private String prefix;
	private String postfix;
	private ArrayList<String> activitySourceNameList;
	private ArrayList<ActivityInterpreter> activityInterpreterList;
	private ArrayList<ActivityInterpreter> runningRules;

	public LogicRule(Actor actor, String activityName, String prefix,
			String postfix) {
		this.setActor(actor);
		this.setActivityName(activityName);
		this.setPrefix(prefix);
		this.setPostfix(postfix);
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityName() {
		return activityName;
	}

	public Actor getActor() {
		return actor;
	}

	private void setActor(Actor actor) {
		this.actor = actor;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public void addActivitySourceName(String activitySourceName) {
		if (!getActivitySourceNameList().contains(activitySourceName)) {
			getActivitySourceNameList().add(activitySourceName);
		}
	}

	public ArrayList<String> getActivitySourceNameList() {
		if (activitySourceNameList == null) {
			activitySourceNameList = new ArrayList<String>();
		}
		return activitySourceNameList;
	}

	public abstract LinkedHashMap<String, String> getLogicRuleQueries(
			ActivityInterpreter activityInterpreter);

	public void addActivityInterpreter(ActivityInterpreter activityInterpreter) {
		addActivitySourceName(activityInterpreter.getActivitySourceName());
		if (!getActivityInterpreterList().contains(activityInterpreter)) {
			getActivityInterpreterList().add(activityInterpreter);
		}
	}

	private ArrayList<ActivityInterpreter> getActivityInterpreterList() {
		if (activityInterpreterList == null) {
			activityInterpreterList = new ArrayList<ActivityInterpreter>();
		}
		return activityInterpreterList;
	}

	protected String getNameCombination(String name, String activity) {
		StringBuilder n = new StringBuilder();
		if (getPrefix() != null && !getPrefix().equals("")) {
			n.append(getPrefix() + "_");
		}
		
		if (getActor().getName() != null && !getActor().getName().equals("")) {
			n.append(getActor().getName() + "_");
		}
		
		//TODO: append ActivityInterpreterName or something to identify the source!?
		
		
		n.append(activity + "_" + name);

		if (getPostfix() != null && !getPostfix().equals("")) {
			n.append("_" + getPostfix());
		}

		return n.toString();
	}

	public void setRunningWith(ActivityInterpreter activityInterpreter) {
		if (!getActivityInterpretersWithRunningRules().contains(activityInterpreter)) {
			getActivityInterpretersWithRunningRules().add(activityInterpreter);
		}
	}
	
	public void removeRunningWith(ActivityInterpreter activityInterpreter) {
		getActivityInterpretersWithRunningRules().remove(activityInterpreter);
	}

	public ArrayList<ActivityInterpreter> getActivityInterpretersWithRunningRules() {
		if (runningRules == null) {
			runningRules = new ArrayList<ActivityInterpreter>();
		}
		return runningRules;
	}

	public boolean isRunningWith(ActivityInterpreter activityInterpreter) {
		return getActivityInterpretersWithRunningRules().contains(activityInterpreter);
	}

	public abstract String getReactionDescription();
}
