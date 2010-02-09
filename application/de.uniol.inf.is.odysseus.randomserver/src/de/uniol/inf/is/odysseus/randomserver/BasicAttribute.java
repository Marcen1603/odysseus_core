package de.uniol.inf.is.odysseus.randomserver;

import de.uniol.inf.is.odysseus.randomserver.BasicSchema.DataType;

public class BasicAttribute {
	
	private DataType type = null;
	private String min;
	private String max;


	public BasicAttribute(DataType type) {
		this.type = type;
	}
	
	public BasicAttribute(DataType type, String min, String max){	
		this.type = type;
		this.max = max;
		this.min = min;
	}	

	public DataType getDataType() {
		return this.type;
	}

	public String getMin(){
		return this.min;
	}
	
	public String getMax(){
		return this.max;
	}
	
}
