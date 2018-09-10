package de.uniol.inf.is.odysseus.eca.plugin;

import java.util.ArrayList;

public class EcaRuleObj {
	private String type;
	private String ruleName;
	private String rulesource;
	private String ruleCondition;
	private ArrayList<String> systemFunctions;
	private ArrayList<String> queryFunction;
	private ArrayList<String> ruleAction;
	private int windowSize;
	private int timerIntervall;
	
	/**
	 * Konstruktor
	 */
	public EcaRuleObj(String type, String ruleName, String ruleSource, String ruleCondition, ArrayList<String> systemFunctions, ArrayList<String> queryFunctions, ArrayList<String> ruleAction, Integer window, Integer timer) {
		this.type = type;
		this.ruleName = ruleName;
		this.rulesource = ruleSource;
		this.ruleCondition = ruleCondition;
		this.setSystemFunctions(systemFunctions);
		queryFunction = new ArrayList<>();
		this.setQueryFunctions(queryFunctions);
		this.ruleAction = ruleAction;
		this.setWindowSize(window);
		this.setTimerIntervall(timer);
	}

	public String getType() {
		return type;
	}

	public String getRuleName() {
		return ruleName;
	}

	public String getRulesource() {
		return rulesource;
	}

	public String getRuleCondition() {
		return ruleCondition;
	}

	public ArrayList<String> getRuleAction() {
		return ruleAction;
	}

	public int getWindowSize() {
		return windowSize;
	}

	private void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public int getTimerIntervall() {
		return timerIntervall;
	}

	private void setTimerIntervall(int timerIntervall) {
		this.timerIntervall = timerIntervall;
	}

	public ArrayList<String> getSystemFunctions() {
		return systemFunctions;
	}

	public void setSystemFunctions(ArrayList<String> systemFunctions) {
		this.systemFunctions = systemFunctions;
	}

	public ArrayList<String> getQueryFunctions() {
		return queryFunction;
	}

	public void setQueryFunctions(ArrayList<String> queryFunctions) {
		this.queryFunction = queryFunctions;
	}
}
