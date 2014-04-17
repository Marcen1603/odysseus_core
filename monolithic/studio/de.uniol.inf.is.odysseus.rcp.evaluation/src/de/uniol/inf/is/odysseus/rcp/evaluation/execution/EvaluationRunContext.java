package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.io.File;

import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;

public class EvaluationRunContext {

	
	private String identifier;
	private EvaluationModel model;

	public EvaluationRunContext(EvaluationModel model, String identifier) {
		this.identifier = identifier;
		this.model = model;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public EvaluationModel getModel() {
		return model;
	}

	public void setModel(EvaluationModel model) {
		this.model = model;
	}

	public String getLatencyResultsPath() {
		return this.model.getProcessingResultsPath() + File.separator + identifier + File.separator + "latencies" + File.separator;
	}

	public String getThroughputResultsPath() {
		return this.model.getProcessingResultsPath() + File.separator + identifier + File.separator + "throughput" + File.separator;
	}
	
	public String getLatencyPlotsPath() {
		return this.model.getPlotFilesPath() + File.separator + identifier + File.separator + "latencies" + File.separator;
	}

	public String getThroughputPlotsPath() {
		return this.model.getPlotFilesPath() + File.separator + identifier + File.separator + "throughput" + File.separator;
	}
	
	

}
