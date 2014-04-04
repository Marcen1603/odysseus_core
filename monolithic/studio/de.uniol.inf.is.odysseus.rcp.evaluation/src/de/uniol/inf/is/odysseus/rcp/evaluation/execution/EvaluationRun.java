package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;

public class EvaluationRun implements Serializable{
	
	private static final long serialVersionUID = -155264023451987992L;
	
	private final EvaluationModel model;
	private final int run;
	private final String identifier;
	private final Map<String, String> values;
	private final ArrayList<String> sortedNames;

	public EvaluationRun(EvaluationModel model, int run, Map<String, String> currentValues, String identifier){
		this.model = model;
		this.run = run;
		this.identifier = identifier;
		this.values = currentValues;
		this.sortedNames = new ArrayList<>(values.keySet());
		Collections.sort(sortedNames);
	}
	
	public EvaluationModel getModel() {
		return model;
	}

	public String getIdentifier() {
		return identifier;
	}

	
	public int getRun() {
		return run;
	}
	
	public String getLatencyResultsPath() {
		return this.model.getProcessingResultsPath() + File.separator + identifier + File.separator + "latencies" + File.separator;
	}
	
	public String getThroughputResultsPath() {
		return this.model.getProcessingResultsPath() + File.separator + identifier + File.separator + "throughput" + File.separator;
	}
	
	public String createThroughputResultPath(ILogicalOperator op){
		String name = op.getName();
		String base = getThroughputResultsPath();
		
		String var = "";
		String sep = "";
		for(String key : sortedNames){
			String fileSafeName = values.get(key).replaceAll("\\W+", ""); 
			var = var + sep + fileSafeName;
			sep = "_";
		}
		name = name+".csv";
		
		return base+var+File.separator+run+File.separator+name;
	}

	public String createLatencyResultPath(ILogicalOperator op) {
		String name = op.getName();
		String base = getLatencyResultsPath();
		
		String var = "";
		String sep = "";
		for(String key : sortedNames){
			String fileSafeName = values.get(key).replaceAll("\\W+", ""); 
			var = var + sep + fileSafeName;
			sep = "_";
		}
		name = name+".csv";
		
		return base+var+File.separator+run+File.separator+name;
	}

	
}
