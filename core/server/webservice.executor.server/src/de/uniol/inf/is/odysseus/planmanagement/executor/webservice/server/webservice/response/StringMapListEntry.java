package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.List;

public class StringMapListEntry {

	
	private String key;
	private List<String> value;
	
	public StringMapListEntry(){
		
	}
	
	public StringMapListEntry(String key, List<String> value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
