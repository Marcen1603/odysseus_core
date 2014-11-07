package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Actor extends FieldDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<LogicRule> logicRulesList;
	private ArrayList<IActorAction> actorActions;

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
		for (IFieldDeviceListener listener : getFieldDeviceListeners()) {
			listener.logicRuleRemoved(rule);
		}
	}

	public ArrayList<IActorAction> getActions() {
		if(actorActions==null){
			actorActions= new ArrayList<IActorAction>();
		}
		return actorActions;
	}
	
	protected void addActorAction(IActorAction action) {
		getActions().add(action);
	}
}
