package de.uniol.inf.is.odysseus.hmm;

public class ObservationAlphas {

	public int timestamp;
	double[] alphas;
	
	public ObservationAlphas(int numStates) {
		alphas = new double[numStates];
	}
	
	public void initializeAlphas() {
		
	}
	
}
