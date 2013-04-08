package de.uniol.inf.is.odysseus.hmm;

import java.util.ArrayList;

public class HmmWindowGroup {
	//Attributes
	private long timestamp;
	private ArrayList<HmmObservationAlphaRow> alphaRows;
		
	//Constructors
	public HmmWindowGroup(){
		alphaRows = new ArrayList<HmmObservationAlphaRow>();
	}
	
	//Methods
	public void addRow(HmmObservationAlphaRow pAlphaRow){
		alphaRows.add(pAlphaRow);		
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

}
