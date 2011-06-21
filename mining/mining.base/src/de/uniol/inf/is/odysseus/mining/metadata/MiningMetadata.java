package de.uniol.inf.is.odysseus.mining.metadata;

public class MiningMetadata implements IMiningMetadata {
	
	private static final long serialVersionUID = 2490005020065173048L;
	private boolean corrected = false;

	@Override
	public String toString() {
		return ""+corrected;
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

}
