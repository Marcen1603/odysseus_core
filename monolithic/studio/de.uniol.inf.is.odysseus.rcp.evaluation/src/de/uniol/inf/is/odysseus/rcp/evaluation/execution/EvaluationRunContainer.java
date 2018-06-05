package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.util.ArrayList;
import java.util.List;

public class EvaluationRunContainer {
	
	private List<EvaluationRun> runs = new ArrayList<>();
	private EvaluationRunContext context;
	
	public EvaluationRunContainer(EvaluationRunContext context){
		this.setContext(context);
	}

	public EvaluationRunContext getContext() {
		return context;
	}

	public void setContext(EvaluationRunContext context) {
		this.context = context;
	}

	public List<EvaluationRun> getRuns() {
		return runs;
	}

	public void setRuns(List<EvaluationRun> runs) {
		this.runs = runs;
	}

}
