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
	
	
	
	//Methoden
	//Forward-Algorithmus
	
	//Backward-Algorithmus
	
	//Productionprobability
	
	//Viterbi
	
	//Baum-Welch
}
