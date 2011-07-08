package de.uniol.inf.is.odysseus.mining.metadata;

public class MiningMetadata implements IMiningMetadata {
	
	private static final long serialVersionUID = 2490005020065173048L;
	private boolean corrected = false;
	private boolean detected = false;

	@Override
	public String toString() {
		return "detected: "+detected+" | corrected: "+corrected;
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
		return this.corrected ;
	}

	@Override
	public void setCorrected(boolean corrected) {
		this.corrected = corrected;

	}
	
	@Override
	public IMiningMetadata clone(){	
		return new MiningMetadata();
	}

	@Override
	public String csvToString(boolean withMetadata){
		return this.csvToString();
	}

	@Override
	public boolean isDetected() {
		return this.detected;
	}

	@Override
	public void setDetected(boolean detected) {
		this.detected = detected;		
	}

}
