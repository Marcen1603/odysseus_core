package de.uniol.inf.is.odysseus.hmm;


import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.hmm.Gesture;
import de.uniol.inf.is.odysseus.hmm.HmmObservationAlphaRow;

public class HMM<M extends ITimeInterval> {

	// Methoden
	
	//+++++++++Learning++++++++++
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
	
	public double[][] forward(Gesture gesture, int[] observations) {
		//Matrix numStats x numObservations
		double[][] fwd = new double[gesture.getNumStates()][observations.length];
		
		//initialization (time0)
		for(int i=0; i<gesture.getNumStates(); i++){
			fwd[i][0] = gesture.getPi()[i] * gesture.getB()[i][observations[0]];
		}
		
		//induction
		for (int t=0; t<=observations.length-2; t++) {
			for(int j=0; j<gesture.getNumStates(); j++){
				fwd[j][t+1] = 0;
				
				for(int i=0; i<gesture.getNumStates(); i++){
					fwd[j][t+1] += (fwd[i][t] * gesture.getA()[i][j]);
				}
				
				fwd[j][t+1] *= gesture.getB()[j][observations[t+1]];
				
			}
		}
		return fwd;
	}

	// Backward-Algorithmus
	public double[][] backward(Gesture gesture, int[] observations) {
		double[][] bwd = new double[gesture.getNumStates()][observations.length];
		
		//initialization
		for (int i = 0; i < gesture.getNumStates(); i++) {
			bwd[i][observations.length-1] = 1;
		}
		
		//induction
		for(int t = observations.length-2; t >= 0; t--) {
			for (int i = 0; i < gesture.getNumStates(); i++) {
				bwd[i][t] = 0;
				for (int j = 0; j < gesture.getNumStates(); j++) {
					bwd[i][t] += gesture.getA()[i][j] * gesture.getB()[j][observations[t+1]] * bwd[j][t+1];
				}
				
			}
		}
		
		return bwd;
	}
	
	// Productionprobability
	public double productionProbability(double[][] fwd){
		double sum = 0;
		for (int i = 0; i < fwd.length; i++) {
			sum += fwd[i][fwd[0].length-1]; 
		}
		
		return sum;
	}
	
	public double[][] improveA(Gesture gesture, int[] observations, double[][] fwd, double[][] bwd, double prodProb) {
		double[][] improvedA = new double[gesture.getNumStates()][gesture.getNumStates()];
		
		for (int j = 0; j < gesture.getNumStates(); j++) {
			for (int i = 0; i < gesture.getNumStates(); i++) {
				improvedA[i][j] = 0;
				//Zähler
				double nominator = 0;
				//Nenner
				double denominator = 0;

				//Fraction calculation
				for (int t = 0; t < observations.length; t++) {
					//Calculate nominator
					nominator += gamma2(fwd[i][t], gesture.getA()[i][j], gesture.getB()[j][observations[t+1]], bwd[j][t+1], prodProb);
					//Calculate denominator
					denominator += gamma1(gesture, i, fwd[i][t], bwd, observations, t, prodProb);
				}
				
				//save 
				improvedA[i][j] = nominator/denominator;
			}
		}
		return improvedA;
	}
	
	public double[][] improveB(Gesture gesture, int[] observations, double[][] fwd, double[][] bwd, double prodProb) {
		double[][] improvedB = new double[gesture.getNumStates()][gesture.getB()[0].length];
		//Zähler
		double nominator = 0;
		//Nenner
		double denominator = 0;
		
		//Run for all B-Values
		for (int j = 0; j < gesture.getNumStates(); j++) {
			for (int o_k = 0; o_k < gesture.getB()[0].length; o_k++) {
							
				for (int t = 0; t < observations.length; t++) {
					//Calculate nominator
					if(o_k == observations[t]){
						nominator += gamma1(gesture, j, fwd[j][t], bwd, observations, t, prodProb);
					}
					//Calculate denominator
					denominator += gamma1(gesture, j, fwd[j][t], bwd, observations, t, prodProb);
				}
				improvedB[j][o_k] = nominator/denominator;
			}
		}
		return improvedB;
	}
	
	public double[] improvePi(Gesture gesture, double[][] fwd, double[][] bwd, int[] observations, double prodProb) {
		double[] improvedPi = new double[gesture.getNumStates()];
		for (int i = 0; i < gesture.getNumStates(); i++) {
			improvedPi[i] = gamma1(gesture, i, fwd[i][0], bwd, observations, 0, prodProb);
		}
		
		return improvedPi;
	}
	
	private double gamma2(double alpha_t, double a_ij, double b_jObs, double beta_tPlus1, double prodProb) {
		return (alpha_t*a_ij*b_jObs*beta_tPlus1)/prodProb;
	}

	private double gamma1(Gesture gesture, int S_i, double alpha_t, double[][] bwd, int[] observations, int t, double prodProb) {
		double sum = 0;
		for (int j = 0; j < gesture.getNumStates(); j++) {
			sum += gamma2(alpha_t, gesture.getA()[S_i][j], gesture.getB()[j][observations[t+1]], bwd[j][t+1], prodProb);
		}
		return sum;
	}
	
	// Baum-Welch
	public void baumwelch(Gesture gesture, int[] observations) {
		//calculate forward variables
		double[][] fwd = forward(gesture, observations);
		//calculate backward variables
		double[][] bwd = backward(gesture, observations);
		//calculate productionprobability
		double prodProb = productionProbability(fwd);
		
		double[][] improvedA, improvedB;
		double[] improvedPi;
		
		//improve fwd and backward variables with given 
		improvedA = improveA(gesture, observations, fwd, bwd, prodProb);
		improvedB = improveB(gesture, observations, bwd, bwd, prodProb);
		improvedPi = improvePi(gesture, fwd, bwd, observations, prodProb);
		
		gesture.setA(improvedA);
		gesture.setB(improvedB);
		gesture.setPi(improvedPi);
		
	}
		
	
	//++++++++++Recognition+++++++++++++
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



	// Viterbi
	public void viterbi(){
		
	}

}
