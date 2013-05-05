package de.uniol.inf.is.odysseus.hmm;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class HMM {
	//Attributes
	public static int observationLength = 16;
	
	//Constructor

	//Methods
	

	/**
	 * Forward Algorithm
	 * Creating an 2D matrix of forward values for given observation sequence
	 * 
	 * @param gesture Given gesture to compute with
	 * @param observations Given observation sequence
	 * @return
	 */
	public double[][] forward(Gesture gesture, int[] observations) {
		//2D matrix numStats x observationsLength
		double[][] fwd = new double[gesture.getNumStates()][observations.length];
//		System.out.println("fwd: " + gesture.getNumStates() + " " + observations.length);
		
		//initialization (time t=0)
		for(int i=0; i<gesture.getNumStates(); i++){
			fwd[i][0] = gesture.getPi()[i] * gesture.getB()[i][observations[0]];
		}
		
		//induction (time t>0)
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


	/**
	 * Forward Algorithm for streaming data. 
	 * Optimized Forward Algorithm to work efficiently on data streams.
	 *  
	 * @param gesture Given gesture to compute with
	 * @param alphas 
	 * @param observation Given observation sequence
	 * @return
	 */
	public double forwardStream(Gesture gesture, HmmAlphas alphas, int observation) {
		double[] newAlphas = new double[alphas.getAlphas().length];
		for (int j = 0; j < alphas.getAlphas().length; j++) {
			//Calculate new alpha-value for state j
			double sum = 0;
			for (int i = 0; i < alphas.getAlphas().length; i++) {
				sum += alphas.getAlphas()[i] * gesture.getA()[i][j];
			}
			newAlphas[j] = sum * gesture.getB()[j][observation];
		}
		//overwrite alphas with new ones
		alphas.setAlphas(newAlphas);
		
		//return Forward-Probability
		double prob = 0;
		for (int i = 0; i < newAlphas.length; i++) {
			prob += newAlphas[i];
		}
		return prob;
	}
	
	public void forwardInit(Gesture gesture, HmmAlphas alphas, int observation) {
		for (int i = 0; i < alphas.getAlphas().length; i++) {
			alphas.getAlphas()[i] = gesture.getPi()[i] * gesture.getB()[i][observation];
//			System.out.println("Gestenname: " + gesture.getName());
//			System.out.println("pi: " + gesture.getPi()[i] + " b-wert: " + gesture.getPi()[i] * gesture.getB()[i][observation]);
//			System.out.println("gesture.getPi()["+i+"] * gesture.getB()["+i+"]["+observation+"]: " + alphas.getAlphas()[i]);
		}
	}


	/**Implementation of Backward Algorithm
	 * 
	 * @param gesture gesture Given gesture to compute with
	 * @param observations Given observation sequence
	 * @return 2D array of backward values
	 */
	public double[][] backward(Gesture gesture, int[] observations) {
		//2D array numStats x observationsLength
		double[][] bwd = new double[gesture.getNumStates()][observations.length];
		
		//initialization (time t=0)
		for (int i = 0; i < gesture.getNumStates(); i++) {
			bwd[i][observations.length-1] = 1;
		}
		
		//induction (time t>0)
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
	
	
	/**Calculating the production probability of a given model by using the prepared forward values
	 * @param fwd 2D array of calculated forward values
	 * @return probabilty value
	 */
	public double productionProbability(double[][] fwd){
		double sum = 0;
		for (int i = 0; i < fwd.length; i++) {
			sum += fwd[i][fwd[0].length-1]; 
		}
		return sum;
	}
	
	
	public double productionProbabilityBackwards(Gesture gesture, double[][] bwd, int[] observations){
		double sum = 0;
		for (int i = 0; i < gesture.getB().length; i++) {
			sum += 
					gesture.getPi()[i] * 
					gesture.getB()[i][observations[0]]
							* bwd[i][0]; 
		}
		
		return sum;
	}
	
	
	public Gesture baumwelch(Gesture gesture, int[] observations) {
		//calculate forward variables
//		System.out.print("obs:");
//		for (int i = 0; i < observations.length; i++) {
//			System.out.print(observations[i] + ", ");
//		}
//		
//		printPiArray(gesture);
//		printAMatrix(gesture);
//		printBMatrix(gesture);
		
		
		double[][] fwd = forward(gesture, observations);
		//calculate backward variables
		double[][] bwd = backward(gesture, observations);
		//calculate productionprobability
		double prodProb = productionProbability(fwd);
//		double prodProbBackwards = productionProbabilityBackwards(gesture, bwd, observations);
		
//		printForwardArray(gesture, observations);
		
		double[][] improvedA, improvedB;
		double[] improvedPi;
		
//		System.out.println("ProdProb: " + prodProb);
//		System.out.println("ProdProbBackwards: " + prodProbBackwards);
		
		//improve fwd and backward variables with given 
		improvedA = improveA(gesture, observations, fwd, bwd, prodProb);
		improvedB = improveB(gesture, observations, fwd, bwd, prodProb);
		improvedPi = improvePi(gesture, fwd, bwd, observations, prodProb);
		
		Gesture g = new Gesture(improvedPi, improvedA, improvedB);
		return g;
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
					if(t == observations.length -1) {
						nominator += (fwd[i][t] * gesture.getA()[i][j])/prodProb;
					} else {
						nominator += gamma2(fwd[i][t], gesture.getA()[i][j], gesture.getB()[j][observations[t+1]], bwd[j][t+1], prodProb);
					}
					//Calculate denominator
					denominator += gamma1(i, t, fwd, bwd, prodProb); 
				}
				//save
				improvedA[i][j] = nominator/denominator;
			}
		}
		return improvedA;
	}
	
	
	
	public double[][] improveB(Gesture gesture, int[] observations, double[][] fwd, double[][] bwd, double prodProb) {
		double[][] improvedB = new double[gesture.getNumStates()][gesture.getB()[0].length];

		//Run for all B-Values
		for (int j = 0; j < gesture.getNumStates(); j++) {
			for (int o_k = 0; o_k < gesture.getB()[0].length; o_k++) {
				//Zähler
				double nominator = 0;
				//Nenner
				double denominator = 0;
							
				for (int t = 0; t < observations.length; t++) {
					//Calculate nominator
					if(o_k == observations[t]){
						nominator += gamma1(j, t, fwd, bwd, prodProb);
					}
					//Calculate denominator
					denominator += gamma1(j, t, fwd, bwd, prodProb);
					
				}
				improvedB[j][o_k] = (nominator/denominator);
			}
		}
		return improvedB;
	}
	
	
	public double[] improvePi(Gesture gesture, double[][] fwd, double[][] bwd, int[] observations, double prodProb) {
		double[] improvedPi = new double[gesture.getNumStates()];
		for (int i = 0; i < gesture.getNumStates(); i++) {
			improvedPi[i] = gamma1(i, 0, fwd, bwd, prodProb);
		}
		
		return improvedPi;
	}
	
	
	private double gamma1(int S_i, int t, double[][] fwd, double[][] bwd, double prodProb) {
		double nominator;
		nominator = fwd[S_i][t] * bwd[S_i][t];
		return nominator/prodProb;
	}
	
	
	private double gamma2(double alpha_t, double a_ij, double b_jObs, double beta_tPlus1, double prodProb) {
		return (alpha_t*a_ij*b_jObs*beta_tPlus1)/prodProb;
	}
	
	
	public void training(int[][] trainingData, Gesture gesture) {

//		System.out.println("trainingdatages: " + trainingData.length);
		ArrayList<Gesture> trainingResults = new ArrayList<Gesture>();
		for (int i = 0; i < trainingData.length; i++) {
//			System.out.println("trainingdatainhalt: " + trainingData[i].length);
			trainingResults.add(baumwelch(gesture, trainingData[i]));
		}
		
//		System.out.println("RESULTSSIZE: " + trainingResults.size());
//		System.out.println();
		
		//calculate mean from Results
		double[]  pi = new double[gesture.getNumStates()];
		double[][] A = new double[gesture.getNumStates()][gesture.getNumStates()];
		double[][] B = new double[gesture.getNumStates()][gesture.getB()[0].length];
		
//		printAMatrix(trainingResults.get(1));
		
		
		//calculate mean for A
		for (int i = 0; i < gesture.getA().length; i++) {
			for (int j = 0; j < gesture.getA()[i].length; j++) {
				double sumA = 0;
//				System.err.println("i: " + i + "   j: " + j);
				for (int k = 0; k < trainingResults.size(); k++) {
					sumA += trainingResults.get(k)
							.
							getA()[i][j];
				}
				A[i][j] = (sumA/trainingResults.size());
			}
				
		}
		
		//calculate mean for B
		for (int i = 0; i < gesture.getB().length; i++) {
			for (int j = 0; j < gesture.getB()[i].length; j++) {
				double sumB = 0;
				for (int k = 0; k < trainingResults.size(); k++) {
					sumB += trainingResults.get(k).getB()[i][j]; 
				}
				B[i][j] = (sumB/trainingResults.size());
			}
		}
		
		//calculate mean for pi
		for (int i = 0; i < gesture.getPi().length; i++) {
			double sumPi = 0;
			for (int k = 0; k < trainingResults.size(); k++) {
				sumPi += trainingResults.get(k).getPi()[i]; 
			}
			pi[i] = (sumPi/trainingResults.size());
		}
		
		gesture.setA(A);
		gesture.setB(B);
		gesture.setPi(pi);
	}
	
	
	
	/* **************************
	 * ** DEBUGGING TESTOUTPUT **
	 * **************************
	 */
	
	public void printAll(Gesture gesture, int[] obs) {
		System.out.println("Print all HMM-Data:");
		
		printPiArray(gesture);
		printAMatrix(gesture);
		printBMatrix(gesture);
		printForwardArray(gesture, obs);
		printBackwardArray(gesture, obs);
	}
	
	public void printProdViaFwd(Gesture gesture, int[] obs) {
		double[][] fwd = forward(gesture, obs);
		System.out.println("Produktionswahrscheinlichkeit via Fwd: "+ productionProbability(fwd));
		
	}
	
	public void printForwardArray(Gesture gesture, int[] obs) {
		
		System.out.println("Forward-Array");
		double[][] fwd = forward(gesture, obs);
		
		DecimalFormat fmt = new DecimalFormat();
		fmt.setMinimumFractionDigits(8);
		fmt.setMaximumFractionDigits(8);

		for (int i = 0; i < fwd.length; i++) {
			for (int j = 0; j < fwd[i].length; j++) {
				System.out.print(fmt.format(fwd[i][j]) + "\t");
			}
			System.out.println();
		}
		
		//---------- Probability -------------------
		System.out.println("Probability via Forward: " + productionProbability(fwd));
		
		System.out.println("\n");
	}
	
	public void printBackwardArray(Gesture gesture, int[] obs) {
		System.out.println("Backward-Array");
		double[][] bwd = backward(gesture, obs);
		
		DecimalFormat fmt = new DecimalFormat();
		fmt.setMinimumFractionDigits(8);
		fmt.setMaximumFractionDigits(8);

		for (int i = 0; i < bwd.length; i++) {
			for (int j = 0; j < bwd[i].length; j++) {
				System.out.print(fmt.format(bwd[i][j]) + "\t");
			}
			System.out.println();
		}
		
		//---------- Probability -------------------
		System.out.println("Probability via Backward: " + productionProbabilityBackwards(gesture, bwd, obs));
		
		System.out.println("\n");
	}
	
	public void printAMatrix(Gesture gesture) {
		System.out.println("A-Matrix");
		
		DecimalFormat fmt = new DecimalFormat();
		fmt.setMinimumFractionDigits(8);
		fmt.setMaximumFractionDigits(8);

		for (int i = 0; i < gesture.getA().length; i++) {
			for (int j = 0; j < gesture.getA()[i].length; j++) {
				System.out.print(fmt.format(gesture.getA()[i][j]) + "\t");
			}
			System.out.println();
		}
		System.out.println("\n");
	}
	
	public void printBMatrix(Gesture gesture) {
		System.out.println("B-Matrix");
		
		DecimalFormat fmt = new DecimalFormat();
		fmt.setMinimumFractionDigits(8);
		fmt.setMaximumFractionDigits(8);

		for (int i = 0; i < gesture.getB().length; i++) {
			for (int j = 0; j < gesture.getB()[i].length; j++) {
				System.out.print(fmt.format(gesture.getB()[i][j]) + "\t");
			}
			System.out.println();

		}
		System.out.println("\n");
	}
	
	public void printPiArray(Gesture gesture) {
		System.out.println("Pi");
		
		DecimalFormat fmt = new DecimalFormat();
		fmt.setMinimumFractionDigits(8);
		fmt.setMaximumFractionDigits(8);

		
		for (int j = 0; j < gesture.getPi().length; j++) {
			System.out.print(fmt.format(gesture.getPi()[j]) + "\t");
		}
		System.out.println("\n");
	}

}
