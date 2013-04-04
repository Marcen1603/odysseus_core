package de.uniol.inf.is.odysseus.core.procedure;

import java.io.Serializable;

public class StoredProcedure implements Serializable{
	
	private static final long serialVersionUID = -3031118218809396056L;
	
	private String name;
	private String text;
	
	public StoredProcedure(String name, String text){
		this.name = name;
		this.text = text;
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

}
