package de.uniol.inf.is.odysseus.hmm;

public class HmmObservationAlphaRow {

	public int timestamp;
	private double[] alphas;
	
	public HmmObservationAlphaRow(int numStates) {
		setAlphas(new double[numStates]);
	}

	public double[] getAlphas() {
		return alphas;
	}

	public void setAlphas(double[] alphas) {
		this.alphas = alphas;
	}
	
}
