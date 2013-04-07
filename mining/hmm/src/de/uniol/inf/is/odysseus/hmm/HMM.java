package de.uniol.inf.is.odysseus.hmm;

public class HMM {
	int numStates;
	int numObservations;
	double pi[];
	double a[][];
	double b[][];

	public HMM(int numStates, int numObservations) {
		this.numStates = numStates;
		this.numObservations = numObservations;

		pi = new double[numStates];
		a = new double[numStates][numStates];
		b = new double[numStates][numObservations];
	}

	// Methoden
	// Forward-Algorithmus
	public void forward() {

	}

	public void forwardStream() {

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
