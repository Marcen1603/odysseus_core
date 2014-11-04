package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public abstract class Sensor implements FieldDevice, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String rawSourceName;
	private String postfix;
	private String prefix;
	
	private LinkedHashMap<String, String> queriesForRawValues;
	
	private List<String> listPossibleActivityNames;
	//private Map<String, ActivityInterpreter> activityInterpreters = Maps.newHashMap();
	private LinkedHashMap<String, String> viewForActivityInterpreter;
	private HashMap<String, String> activitySourceNameMap;
	
	
	public Sensor(String name, String prefix, String postfix) {
		this.setName(name);
		this.setRawSourceName(name, prefix, postfix);
		this.setPrefix(prefix);
		this.setPostfix(postfix);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRawSourceName() {
		return rawSourceName;
	}
	public void setRawSourceName(String sourceName, String prefix, String postfix) {
		String rawSourceName = "";
		if(prefix!=null && !prefix.equals("")){
			rawSourceName += prefix+"_";
		}
		
		rawSourceName+="rawValues_"+sourceName+"";
		
		if(postfix!=null && !postfix.equals("")){
			rawSourceName += "_"+postfix;
		}
		
		this.rawSourceName = rawSourceName;
	}
	
	protected void addQueryForRawValues(String queryName, String query){
		if(this.queriesForRawValues==null){
			this.queriesForRawValues = new LinkedHashMap<String,String>();
		}
		this.queriesForRawValues.put(queryName, query);
	}
	
	public Map<String,String> getQueriesForRawValues(){
		return this.queriesForRawValues;
	}
	
	@Override
	public List<String> getPossibleActivityNames() {
		if(listPossibleActivityNames==null){
			listPossibleActivityNames = new ArrayList<String>();
		}
		return listPossibleActivityNames;
	}

	protected void addPossibleActivityName(String activityName) {
		if(!getPossibleActivityNames().contains(activityName)){
			getPossibleActivityNames().add(activityName);			
		}
	}
	public void setViewForActivityInterpreterQueries(LinkedHashMap<String, String> interpreters) {
		this.viewForActivityInterpreter = interpreters;
	}
	protected void addViewForActivityInterpreter(String queryName, String query) {
		if(this.viewForActivityInterpreter==null){
			this.viewForActivityInterpreter = new LinkedHashMap<String, String>();
		}
		this.viewForActivityInterpreter.put(queryName, query);
	}
	public LinkedHashMap<String, String> getViewForActivityInterpreterQueries() {
		return this.viewForActivityInterpreter;
	}
	public String getPostfix() {
		return this.postfix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getPrefix() {
		return this.prefix;
	}
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	protected void setActivitySourceName(String activity,
			String activitySourceName) {
		if(this.activitySourceNameMap==null){
			this.activitySourceNameMap = new HashMap<String,String>();
		}
		this.activitySourceNameMap.put(activity, activitySourceName);
	}
	
	public String getActivitySourceName(String possibleActivityName) {
		return this.activitySourceNameMap.get(possibleActivityName);
	}
	
	protected String getNameCombination(String name, String activity) {
		StringBuilder n = new StringBuilder();
		if(getPrefix()!=null && !getPrefix().equals("")){
			n.append(getPrefix()+"_");
		}
		n.append(activity+"_"+name);
		
		if(getPostfix()!=null && !getPostfix().equals("")){
			n.append("_"+getPostfix());
		}
		
		return n.toString();
	}
}
