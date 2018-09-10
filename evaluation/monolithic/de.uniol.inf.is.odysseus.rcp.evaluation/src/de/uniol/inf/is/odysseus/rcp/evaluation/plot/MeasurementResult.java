package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationType;

public class MeasurementResult {

	
	private List<File> files = new ArrayList<>();
	private String variable = "";
	private String name = "";	
	private EvaluationType type;

	public MeasurementResult(EvaluationType type) {
		this.type = type;
	}

	public void addFile(File file) {
		this.files.add(file);
	}
	
	public void addAllFiles(Collection<File> files){
		this.files.addAll(files);
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {	
		String s = variable+" for "+name;
		for(File f : files){
			s = s+"\n"+f.getAbsolutePath();
		}
		return s;
	}

	public EvaluationType getType() {
		return type;
	}

}
