package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationType;

public class MeasurementResultContainer {
	
	private List<MeasurementResult> results = new ArrayList<>();
	private EvaluationType type;

	public MeasurementResultContainer(EvaluationType type) {
		this.type = type;
	}

	public void addResult(MeasurementResult mr){
		this.results.add(mr);
	}
	
	public boolean contains(MeasurementResult mr){
		if(containsEqualResult(mr)!=null){
			return true;
		}
		return false;
	}
	
	public MeasurementResult containsEqualResult(MeasurementResult mr){
		for(MeasurementResult result : results){
			if(mr.getName().equals(result.getName()) && mr.getVariable().equals(result.getVariable())){
				return result;
			}
		}
		return null;
	}
	
	public void addOrMergeResult(String name, String variable, File file){
		MeasurementResult mr = new MeasurementResult(this.type);
		mr.setName(name);
		mr.setVariable(variable);
		mr.addFile(file);
		addOrMergeResult(mr);
	}
	
	public void addOrMergeResult(MeasurementResult mr){
		MeasurementResult result = containsEqualResult(mr);
		if(result==null){
			results.add(mr);
		}else{
			result.addAllFiles(mr.getFiles());
		}
	}
	
	public List<MeasurementResult> getResults() {
		return results;
	}

	public void setResults(List<MeasurementResult> results) {
		this.results = results;
	}
	
	

}
