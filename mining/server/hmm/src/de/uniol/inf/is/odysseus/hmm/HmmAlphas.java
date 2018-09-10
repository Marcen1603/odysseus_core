package de.uniol.inf.is.odysseus.hmm;

/**
 * @author Michael Möbes
 * @author Christian Pieper
 * 
 *         Contains forward values alpha for each state, updated with the last
 *         observation
 * 
 */
public class HmmAlphas {

	public int timestamp;
	private double[] alphas;

	public HmmAlphas(int numStates) {
		setAlphas(new double[numStates]);
	}

	public double[] getAlphas() {
		return alphas;
	}

	public void setAlphas(double[] alphas) {
		this.alphas = alphas;
	}

}
