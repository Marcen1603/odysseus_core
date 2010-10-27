package de.uniol.inf.is.odysseus.randomserver;

import java.util.ArrayList;
import java.util.List;

public class BasicSchema {

	public static enum DataType {
		Integer, Long, String, Double, Time
	};

	private List<BasicAttribute> attributes = new ArrayList<BasicAttribute>();

	public BasicSchema() {

	}

	public void addAttribute(DataType type, String min, String max){
		this.attributes.add(new BasicAttribute(type, min, max));
	}
		
	public void addAttribute(DataType type){
		switch (type) {
		case Double:
			this.addAttribute(type, "0.0", "1.0");
			break;
		case Integer:
			this.addAttribute(type, "0", "100");
			break;
		case String:
			this.addAttribute(type, "0", "10");
			break;
		case Long:
			this.addAttribute(type, "0", String.valueOf(Long.MAX_VALUE));
			break;
		case Time:
			this.addAttribute(type, "0", String.valueOf(Long.MAX_VALUE));
			break;
		default:
			break;
		}
		
	}
	
	
	public List<BasicAttribute> getAttributes(){
		return this.attributes;
	}
	
	@Override
	public String toString(){
		String val = "";
		String del = "";
		for(BasicAttribute at : this.attributes){
			val = val + del +at.getDataType().toString().toUpperCase()+"["+at.getMin()+":"+at.getMax()+"]";
			del = ", ";
		}
		return val;
	}

}
