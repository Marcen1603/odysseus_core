package de.uniol.inf.is.odysseus.rcp.viewer.editors.impl;

import java.util.ArrayList;
import java.util.List;

public class NamedList {
	
	private Object key;
	private List<Object> values = new ArrayList<>(); 

	public NamedList(Object name){
		this.key = name;
	}
	
	public List<Object> getValues() {
		return values;
	}

	public void addValue(Object val){
		this.values.add(val);
	}
	
	public void setValues(List<Object> values) {
		this.values = values;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

}
