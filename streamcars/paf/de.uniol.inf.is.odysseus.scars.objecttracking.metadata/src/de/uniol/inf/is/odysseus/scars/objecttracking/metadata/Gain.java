package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

public class Gain implements IGain {

	private double[][] gain;
	
	public Gain() {
		
	}
	
	@Override
	public double[][] getGain() {
		return this.gain;
	}

	@Override
	public void setGain(double[][] newGain) {
		this.gain = newGain;
	}

	@Override
	public IGain clone(){
		return new Gain();
	}
}
