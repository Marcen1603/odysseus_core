package de.uniol.inf.is.odysseus.core.procedure;

import java.io.Serializable;
import java.util.List;

public class StoredProcedure implements Serializable{
	
	private static final long serialVersionUID = -3031118218809396056L;
	
	private String name;
	private String text;

	private List<String> variables;
	
	public StoredProcedure(){
		
	}
	
	public StoredProcedure(String name, String text, List<String> procVars){
		this.name = name;
		this.text = text;
		this.variables = procVars;
	}
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
	}

}
