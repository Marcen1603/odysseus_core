package de.uniol.inf.is.odysseus.rcp.evaluation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EvaluationVariable {
	
	private List<String> values = new ArrayList<>();
	private String name;
	private boolean active = true;
	
	public EvaluationVariable(String name){
		this.name = name;
	}
	
	public EvaluationVariable(String name, List<String> values, boolean active){
		this.name = name;
		this.values = values;
		this.active = active;
	}
	
	public EvaluationVariable(String name, String... variables){
		this.name = name;
		this.values.addAll(Arrays.asList(variables));
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
