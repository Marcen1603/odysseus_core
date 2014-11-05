package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.RpiGPIOActor.RpiGPIOActorLogicRule;

public abstract class Actor implements FieldDevice, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private List<String> listPossibleActivityNames;
	private String prefix;
	private String postfix;
	private ArrayList<LogicRule> logicRulesList;

	public Actor(String name, String prefix, String postfix) {
		this.name = name;
		this.setPrefix(prefix);
		this.setPostfix(postfix);
	}

	public String getName(){
		return this.name;
	}
	
	/**
	 * 
	 * @param possibleActivityName
	 * @return
	 * @throws Exception
	 */
	/*
	public LinkedHashMap<String, String> getLogicRuleOfActivity(
			String possibleActivityName) throws Exception {
		if(this.getActivitySourceName()==null || this.getActivitySourceName().equals("")){
			throw new Exception("No ActivitySourceName was set of the actor:"+getName());
		}
		
		initLogicRulesContainerIfNeeded();
		
		initLogicRules();
		
		return this.logicRules.get(possibleActivityName);
	}
	*/
	
	/*
	 * 	LinkedHashMap<String, String> logicRuleQueriesCopy = new LinkedHashMap<String, String>();
		
		for (Entry<String, String> entry : getLogicRuleQueries().entrySet()) {
			String activityName = entry.getKey();
			String ruleQuery = entry.getValue();
			logicRuleQueriesCopy.put(activityName, ruleQuery);
		}

		return logicRuleQueriesCopy;(non-Javadoc)

	 */
	

	@Override
	public List<String> getPossibleActivityNames() {
		if(listPossibleActivityNames==null){
			listPossibleActivityNames = new ArrayList<String>();
		}
		return listPossibleActivityNames;
	}

	public void addPossibleActivityName(String activityName) {
		if(!getPossibleActivityNames().contains(activityName)){
			getPossibleActivityNames().add(activityName);			
		}
	}
	
	@Override
	public String toString() {
		return getName();
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

	public void addLogicRuleForActivity(RpiGPIOActorLogicRule newRule) {
		getLogicRules().add(newRule);
	}

	public ArrayList<LogicRule> getLogicRules() {
		if(this.logicRulesList==null){
			this.logicRulesList = new ArrayList<LogicRule>();
		}
		return logicRulesList;
	}
}
