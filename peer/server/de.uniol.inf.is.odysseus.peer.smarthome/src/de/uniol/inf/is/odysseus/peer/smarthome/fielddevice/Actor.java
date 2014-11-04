package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class Actor implements FieldDevice, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private List<String> listPossibleActivityNames;
	private String activitySourceName;
	private HashMap<String, LinkedHashMap<String, String>> logicRules;
	private String prefix;
	private String postfix;

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
	public LinkedHashMap<String, String> getLogicRuleOfActivity(
			String possibleActivityName) throws Exception {
		if(this.getActivitySourceName()==null || this.getActivitySourceName().equals("")){
			throw new Exception("No ActivitySourceName was set of the actor:"+getName());
		}
		
		initLogicRulesContainerIfNeeded();
		
		initLogicRules();
		
		return this.logicRules.get(possibleActivityName);
	}
	
	/*
	 * 	LinkedHashMap<String, String> logicRuleQueriesCopy = new LinkedHashMap<String, String>();
		
		for (Entry<String, String> entry : getLogicRuleQueries().entrySet()) {
			String activityName = entry.getKey();
			String ruleQuery = entry.getValue();
			logicRuleQueriesCopy.put(activityName, ruleQuery);
		}

		return logicRuleQueriesCopy;(non-Javadoc)

	 */
	
	protected abstract void initLogicRules();

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
	
	public void setActivitySourceName(String name) {
		this.activitySourceName = name;
	}
	
	public String getActivitySourceName(){
		return this.activitySourceName;
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

	public void addLogicRuleForActivity(String activity,
			LinkedHashMap<String, String> logicRuleQueries) {
		initLogicRulesContainerIfNeeded();
		this.logicRules.put(activity, logicRuleQueries);
	}

	private void initLogicRulesContainerIfNeeded() {
		if(this.logicRules==null){
			this.logicRules = new HashMap<String, LinkedHashMap<String, String>>();
		}
	}
}
