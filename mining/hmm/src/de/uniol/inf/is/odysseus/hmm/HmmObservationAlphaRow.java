package de.uniol.inf.is.odysseus.hmm;

public class HmmObservationAlphaRow {

	public int timestamp;
	double[] alphas;
	
	public HmmObservationAlphaRow(int numStates) {
		alphas = new double[numStates];
	}
	
	public void initializeAlphas() {
		
	}
	
}
