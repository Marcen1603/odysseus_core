package de.uniol.inf.is.odysseus.mining.metadata;

import java.util.ArrayList;
import java.util.List;

public class MiningMetadata implements IMiningMetadata {
	
	private static final long serialVersionUID = 2490005020065173048L;
	private List<String> corrected = new ArrayList<String>();
	private List<String> detected = new ArrayList<String>(); 

	public MiningMetadata(){		
	}

	public MiningMetadata(MiningMetadata mm) {		
		this.corrected = mm.corrected;
		this.detected =  mm.detected;			
	}
	

	@Override
	public String toString() {
		String dS = listToString(detected);
		String cS = listToString(corrected);
		return "detected: "+dS+" | corrected: "+cS;
	}
	
	
	private String listToString(List<String> list){
		String dS = "[";		
		String del = "";
		for(int i=0;i<list.size();i++){
			dS = dS+del+list.get(i);			
			del = ", ";
		}
		dS = dS+"]";
		return dS;
	}
	
	@Override
	public String csvToString() {
		return toString();
	}

	@Override
	public String getCSVHeader() {
		return "Mining";
	}

	@Override
	public boolean isCorrected() {
		return !this.corrected.isEmpty();
	}

		
	@Override
	public IMiningMetadata clone(){	
		return new MiningMetadata(this);
	}

	@Override
	public String csvToString(boolean withMetadata){
		return this.csvToString();
	}

	@Override
	public boolean isDetected() {
		return !this.detected.isEmpty();
	}
	

	@Override
	public boolean isDetectedAttribute(String attribute) {
		return this.detected.contains(attribute);
	}

	@Override
	public void setDetectedAttribute(String attribute, boolean detected) {
		if(this.detected.contains(attribute)){
			if(!detected){
				this.detected.remove(attribute);
			}
		}else{
			if(detected){
				this.detected.add(attribute);
			}
		}		
	}


	@Override
	public boolean isCorrectedAttribute(String attribute) {
		return this.corrected.contains(attribute);
	}

	@Override
	public void setCorrectedAttribute(String attribute, boolean corrected) {
		if(this.corrected.contains(attribute)){
			if(!corrected){
				this.corrected.remove(attribute);
			}
		}else{
			if(corrected){
				this.corrected.add(attribute);
			}
		}	
	}

}
