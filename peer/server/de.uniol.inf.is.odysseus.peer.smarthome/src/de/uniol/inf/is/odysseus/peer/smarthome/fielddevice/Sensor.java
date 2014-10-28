package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public abstract class Sensor implements FieldDevice, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String sourceName;
	private String activitySourceName;
	private String queryForRawValues;
	private List<String> listPossibleActivityNames;
	private Map<String, ActivityInterpreter> activityInterpreters = Maps.newHashMap();
	private String queryForActivityInterpreter;
	private String sourceNamePostfix;
	
	public Sensor(String name, String sourceName, String sourceNamePostfix) {
		this.setName(name);
		this.setRawSourceName(sourceName+"_"+sourceNamePostfix);
		this.setActivitySourceName(sourceName+"Activity_"+sourceNamePostfix);
		this.setSourceNamePostfix(sourceNamePostfix);
	}
	protected void setActivitySourceName(String name) {
		this.activitySourceName = name;
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
	
	protected void setQueryForRawValues(String query){
		this.queryForRawValues = query;
	}
	
	public String getQueryForRawValues(){
		return this.queryForRawValues;
	}
	
	public String getActivitySourceName(){
		return this.activitySourceName;
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
	public Map<String, ActivityInterpreter> getActivityInterpreters() {
		return activityInterpreters;
	}
	public void setActivityInterpreters(Map<String, ActivityInterpreter> activityInterpreters) {
		this.activityInterpreters = activityInterpreters;
	}
	public void addActivityInterpreter(String activity,ActivityInterpreter activityInterpreter){
		this.activityInterpreters.put(activity, activityInterpreter);
	}
	
	protected void setQueryForActivityInterpreter(String query) {
		this.queryForActivityInterpreter = query;
	}
	
	public String getQueryForActivityInterpreter() {
		return this.queryForActivityInterpreter;
	}
	public String getSourceNamePostfix() {
		return sourceNamePostfix;
	}
	public void setSourceNamePostfix(String sourceNamePostfix) {
		this.sourceNamePostfix = sourceNamePostfix;
	}
}
