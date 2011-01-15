package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.sources.Attribute;

public class Stream {
	private String name;
	//private ArrayList<Attribute> attributes;
	private ArrayList<String> attributes;
	
	public Stream(String name, ArrayList<String> attribute){
		this.name = name;
		//this.attributes = new ArrayList<Attribute>(attribute);
		this.attributes = new ArrayList<String>(attribute);
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<String> getAttributes(){
		return attributes;
	}
	
	public String getIthAttName(int i){
		return attributes.get(i);
	}
	
	public String toString(){
		return this.name+" "+this.attributes.toString();
	}

}
