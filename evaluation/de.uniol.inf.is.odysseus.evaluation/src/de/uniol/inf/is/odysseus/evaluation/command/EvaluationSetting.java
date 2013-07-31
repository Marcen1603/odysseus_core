package de.uniol.inf.is.odysseus.evaluation.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class EvaluationSetting implements IQueryBuildSetting<EvaluationSetting>{
	
	private Map<String, List<String>> variables = new HashMap<String, List<String>>();
	private int numberOfTimes = 1;
	private String storingPath;
	
	private boolean activateThroughput = true;
	private boolean addThroughputOperators = true;
	
	
	private boolean activateLatency = true;
	private boolean addLatencyCalculationOperators = true;
	private boolean addFileSinkOperators = true;
	private boolean useTimestamps = true;
	
	public Map<String, List<String>> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, List<String>> variables) {
		this.variables = variables;
	}
	public int getNumberOfTimes() {
		return numberOfTimes;
	}
	public void setNumberOfTimes(int numberOfTimes) {
		this.numberOfTimes = numberOfTimes;
	}
	public String getStoringPath() {
		return storingPath;
	}
	public void setStoringPath(String storingPath) {
		this.storingPath = storingPath;
	}
	public boolean isActivateThroughput() {
		return activateThroughput;
	}
	public void setActivateThroughput(boolean activateThroughput) {
		this.activateThroughput = activateThroughput;
	}
	public boolean isAddThroughputOperators() {
		return addThroughputOperators;
	}
	public void setAddThroughputOperators(boolean addThroughputOperators) {
		this.addThroughputOperators = addThroughputOperators;
	}
	public boolean isActivateLatency() {
		return activateLatency;
	}
	public void setActivateLatency(boolean activateLatency) {
		this.activateLatency = activateLatency;
	}
	public boolean isAddLatencyCalculationOperators() {
		return addLatencyCalculationOperators;
	}
	public void setAddLatencyCalculationOperators(boolean addLatencyCalculationOperators) {
		this.addLatencyCalculationOperators = addLatencyCalculationOperators;
	}
	public boolean isAddFileSinkOperators() {
		return addFileSinkOperators;
	}
	public void setAddFileSinkOperators(boolean addFileSinkOperators) {
		this.addFileSinkOperators = addFileSinkOperators;
	}
	public boolean isUseTimestamps() {
		return useTimestamps;
	}
	public void setUseTimestamps(boolean useTimestamps) {
		this.useTimestamps = useTimestamps;
	}
	@Override
	public EvaluationSetting getValue() {
		return this;
	}
	
	
	

}
