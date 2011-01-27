package windperformancercp.model.query;

import java.util.ArrayList;

public class Stream {
	private String name;
	
	private ArrayList<String> attributeNames;
	
	public Stream(String name, ArrayList<String> attribute){
		this.name = name;
		this.attributeNames = new ArrayList<String>(attribute);
	}
	
	public Stream(){
		this.name = "";
		this.attributeNames = new ArrayList<String>();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	/*
	public ArrayList<Attribute> getAttributes(){
		return attributes;
	}
	*/
	
	public ArrayList<String> getAttributeNames(){
		return attributeNames;
	}
	
	public void setAttributeNames(ArrayList<String> names){
		this.attributeNames = names;
	}
	
	public String getIthAttName(int i){
		return attributeNames.get(i).toString();
	}
	
	public String toString(){
		return this.name+" "+this.attributeNames.toString();
	}

}
