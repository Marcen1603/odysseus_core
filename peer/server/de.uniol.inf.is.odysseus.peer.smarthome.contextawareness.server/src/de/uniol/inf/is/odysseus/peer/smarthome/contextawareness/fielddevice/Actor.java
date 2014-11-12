package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.RPiGPIOActor.State;

public abstract class Actor extends FieldDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<LogicRule> logicRulesList;
	private ArrayList<AbstractActorAction> actorActions;

	public Actor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);
	}

	public void addLogicRuleForActivity(LogicRule newRule) {
		getLogicRules().add(newRule);
	}

	public ArrayList<LogicRule> getLogicRules() {
		if (this.logicRulesList == null) {
			this.logicRulesList = new ArrayList<LogicRule>();
		}
		return logicRulesList;
	}

	public boolean deleteLogicRule(LogicRule rule) {
		if (getLogicRules().remove(rule)) {
			
			fireLogicRuleRemoved(rule);
			return true;
		} else {
			return false;
		}
	}

	private void fireLogicRuleRemoved(LogicRule rule) {
		for (ILogicRuleListener listener : getFieldDeviceListeners()) {
			listener.logicRuleRemoved(rule);
		}
	}

	public ArrayList<AbstractActorAction> getActions() {
		if(actorActions==null){
			actorActions= new ArrayList<AbstractActorAction>();
		}
		return actorActions;
	}
	
	protected void addActorAction(AbstractActorAction action) {
		getActions().add(action);
	}
	
	public abstract void createLogicRuleWithState(String activityName, State state);

	public abstract boolean createLogicRuleWithAction(String activityName,
			AbstractActorAction actorAction);
	public abstract boolean logicRuleExist(LogicRule newRule);

	public boolean save() throws PeerCommunicationException {
		return getSmartDevice().save();
	}
}
