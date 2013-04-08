package de.uniol.inf.is.odysseus.hmm;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class HMM<M extends ITimeInterval> {

	// Methoden
	// Forward-Algorithmus
	/**
	 * Initialization of alpha-values for the Forward-Algorithm
	 * @param gesture
	 * @param alphaRow
	 * @param observation
	 */
	public void forwardInit(Gesture gesture, HmmObservationAlphaRow alphaRow, int observation) {
		for (int i = 0; i < alphaRow.getAlphas().length; i++) {
			alphaRow.getAlphas()[i] = gesture.getPi()[i] * gesture.getB()[i][observation];
		}
	}
	
	public void forward() {

	}

	public double forwardStream(Gesture gesture, HmmObservationAlphaRow alphaRow, int observation) {
		double[] newAlphas = new double[alphaRow.getAlphas().length];
		for (int j = 0; j < alphaRow.getAlphas().length; j++) {
			//Calculate new alpha-value for state j
			double sum = 0;
			for (int i = 0; i < alphaRow.getAlphas().length; i++) {
				sum += alphaRow.getAlphas()[i] * gesture.getA()[i][j];
			}
			newAlphas[j] = sum * gesture.getB()[j][observation];
		}
		//overwrite alphas with new ones
		alphaRow.setAlphas(newAlphas);
		
		//return Forward-Probability
		double prob = 0;
		for (int i = 0; i < newAlphas.length; i++) {
			prob += newAlphas[i];
		}
		return prob;
	}

	// Backward-Algorithmus
	public void backward() {

	}

	// Productionprobability
	public void productionProbability(){
		
	}

	// Viterbi
	public void viterbi(){
		
	}

	// Baum-Welch
	public void baumwelch(){
		
	}
}
