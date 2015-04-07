package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.AbstractActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;

public abstract class AbstractLogicRule implements Serializable {
	private static final long serialVersionUID = 1L;
	private String activityName;
	private AbstractActor actor;
	private String prefix;
	private String postfix;
	private ArrayList<String> activitySourceNameList;
	private ArrayList<AbstractActivityInterpreter> activityInterpreterList;
	private ArrayList<AbstractActivityInterpreter> runningRules;

	public AbstractLogicRule(AbstractActor actor, String activityName, String prefix,
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

	public AbstractActor getActor() {
		return actor;
	}

	private void setActor(AbstractActor actor) {
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

	/**
	 * 
	 * @param activityInterpreter
	 * @return <String,String> <viewName,Query>
	 */
	public abstract LinkedHashMap<String, String> getLogicRuleQueries(
			AbstractActivityInterpreter activityInterpreter);

	public void addActivityInterpreter(AbstractActivityInterpreter activityInterpreter) {
		addActivitySourceName(activityInterpreter.getActivitySourceName());
		if (!getActivityInterpreterList().contains(activityInterpreter)) {
			getActivityInterpreterList().add(activityInterpreter);
		}
	}

	private ArrayList<AbstractActivityInterpreter> getActivityInterpreterList() {
		if (activityInterpreterList == null) {
			activityInterpreterList = new ArrayList<AbstractActivityInterpreter>();
		}
		return activityInterpreterList;
	}

	protected String getNameCombination(String name, String activity, String peerName) {
		StringBuilder n = new StringBuilder();
		if (getPrefix() != null && !getPrefix().equals("")) {
			n.append(getPrefix() + "_");
		}
		
		if (getActor().getName() != null && !getActor().getName().equals("")) {
			n.append(getActor().getName() + "_");
		}
		
		//TODO: append ActivityInterpreterName or something to identify the source!?
		if (peerName!=null && !peerName.equals("")) {
			n.append("source_"+peerName+"_");
		}
		
		n.append(activity + "_" + name);

		if (getPostfix() != null && !getPostfix().equals("")) {
			n.append("_" + getPostfix());
		}

		return n.toString();
	}

	public void addRunningWith(AbstractActivityInterpreter activityInterpreter) {
		if (!getActivityInterpretersWithRunningRules().contains(activityInterpreter)) {
			getActivityInterpretersWithRunningRules().add(activityInterpreter);
		}
	}
	
	public void removeRunningWith(AbstractActivityInterpreter activityInterpreter) {
		getActivityInterpretersWithRunningRules().remove(activityInterpreter);
	}

	public ArrayList<AbstractActivityInterpreter> getActivityInterpretersWithRunningRules() {
		if (runningRules == null) {
			runningRules = new ArrayList<AbstractActivityInterpreter>();
		}
		return runningRules;
	}

	public boolean isRunningWith(AbstractActivityInterpreter activityInterpreter) {
		return getActivityInterpretersWithRunningRules().contains(activityInterpreter);
	}

	public abstract String getReactionDescription();

	public abstract void setActivitySourceName(String activitySourceName);

	public abstract String getActivitySourceName();

	public abstract LinkedHashMap<String, String> getLogicRulesQueriesWithActivitySourceName();

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AbstractLogicRule){
			AbstractLogicRule other = (AbstractLogicRule)obj;
			if(this.getActivityName().equals(other.getActivityName()) &&
					this.getReactionDescription().equals(other.getReactionDescription())){
				//					this.getActivitySourceName().equals(other.getActivitySourceName()) &&
				return true;
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return getActivityName().hashCode() + 31 * getReactionDescription().hashCode();
	}
}
