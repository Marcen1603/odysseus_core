package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Sensor implements FieldDevice, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String sourceName;
	private String queryForRawValues;
	private List<String> listPossibleActivityNames;
	
	public Sensor(String name, String rawSourceName) {
		this.setName(name);
		this.setRawSourceName(rawSourceName);
	}
	public abstract List<Object> possibleValueArea();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRawSourceName() {
		return sourceName;
	}
	public void setRawSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public void setQueryForRawValues(String query){
		this.queryForRawValues = query;
	}
	
	public String getQueryForRawValues(){
		return this.queryForRawValues;
	}
	
	public String getActivitySourceName(){
		return getName()+"Activities";
	}
	
	@Override
	public List<String> getPossibleActivityNames() {
		if(listPossibleActivityNames==null){
			listPossibleActivityNames = new ArrayList<String>();
		}
		return listPossibleActivityNames;
	}

	@Override
	public void addPossibleActivityName(String activityName) {
		getPossibleActivityNames().add(activityName);
	}
}
