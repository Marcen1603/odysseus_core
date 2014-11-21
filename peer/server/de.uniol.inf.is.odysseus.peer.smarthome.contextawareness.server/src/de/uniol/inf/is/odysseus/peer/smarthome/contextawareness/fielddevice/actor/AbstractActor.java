package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.RPiGPIOActor.State;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.ILogicRuleListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;

public abstract class AbstractActor extends FieldDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<AbstractLogicRule> logicRulesList;
	private ArrayList<AbstractActorAction> actorActions;
	private transient ArrayList<ILogicRuleListener> logicRuleListeners;

	public AbstractActor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);
	}

	public boolean addLogicRuleForActivity(AbstractLogicRule rule) {
		if(getLogicRules().add(rule)){
			fireLogicRuleAdded(rule);
			return true;
		}else{
			return false;
		}
	}

	public ArrayList<AbstractLogicRule> getLogicRules() {
		if (this.logicRulesList == null) {
			this.logicRulesList = new ArrayList<AbstractLogicRule>();
		}
		return logicRulesList;
	}

	public boolean deleteLogicRule(AbstractLogicRule rule) {
		if (getLogicRules().remove(rule)) {
			
			fireLogicRuleRemoved(rule);
			return true;
		} else {
			return false;
		}
	}
	
	private void fireLogicRuleAdded(AbstractLogicRule rule) {
		for (ILogicRuleListener listener : getLogicRuleListeners()) {
			listener.logicRuleAdded(rule);
		}
	}

	private void fireLogicRuleRemoved(AbstractLogicRule rule) {
		for (ILogicRuleListener listener : getLogicRuleListeners()) {
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
	public abstract boolean logicRuleExist(AbstractLogicRule newRule);

	public boolean save() throws PeerCommunicationException {
		return getSmartDevice().save();
	}
	
	public void addLogicRuleListener(ILogicRuleListener fieldDeviceListener) {
		getLogicRuleListeners().add(fieldDeviceListener);
	}
	
	public void removeLogicRuleListener(ILogicRuleListener fieldDeviceListener) {
		getLogicRuleListeners().remove(fieldDeviceListener);
	}

	protected ArrayList<ILogicRuleListener> getLogicRuleListeners() {
		if(logicRuleListeners==null){
			logicRuleListeners = new ArrayList<ILogicRuleListener>();
		}
		return logicRuleListeners;
	}
}
