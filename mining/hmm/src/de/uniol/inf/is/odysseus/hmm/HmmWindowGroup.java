package de.uniol.inf.is.odysseus.hmm;

import java.util.ArrayList;

public class HmmWindowGroup {
	//Attributes
	int timestamp;
	ArrayList<HmmObservationAlphaRow> alphaRows;
		
	//Constructors
	public HmmWindowGroup(){
		alphaRows = new ArrayList<HmmObservationAlphaRow>();
	}
	
	
	//Methods
	public void addRow(HmmObservationAlphaRow pAlphaRow){
		alphaRows.add(pAlphaRow);		
	}


	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
