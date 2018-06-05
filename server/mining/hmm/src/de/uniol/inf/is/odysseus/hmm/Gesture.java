package de.uniol.inf.is.odysseus.hmm;

/**
 * @author Michael Möbes, mmo
 * @author Christian Pieper, cpi
 * 
 */
public class Gesture {
	// Attributes
	private String name;
	private int numStates;
	private int numMinObs;
	private int numMaxObs;
	private double[] pi;
	private double[][] a;
	private double[][] b;

	/**
	 * Create and initialize Gesture with standard-values
	 * 
	 * @param numStates
	 * @param sizeObservationAlphabet
	 * @param observationLength
	 * @param numMaxObs
	 * @param observationLength
	 */
	public Gesture(int numStates, int sizeObservationAlphabet, int numMinObs, int numMaxObs, int observationLength) {
		this.numStates = numStates;
		this.setNumMinObs(numMinObs);
		this.setNumMaxObs(numMaxObs);
		// Create Pi-Array
		// Initialize to start in first state
		this.pi = new double[numStates];
		for (int i = 0; i < pi.length; i++) {
			if (i == 0) {
				pi[i] = 1;
			} else {
				pi[i] = 0;
			}
		}

		// Create A-matix
		// Initialize with Left-Right-Banded Approach
		this.a = new double[numStates][numStates];
		for (int i = 0; i < this.a.length; i++) {

			if (i < this.a.length - 1) {
				System.out.print("a[" + i + "][" + i + "] = (1 - (1/( "
						+ observationLength + "/" + numStates + ")))");
				a[i][i] = (1 - (1 / (observationLength / numStates)));
				System.out.println(a[i][i]);
				a[i][i + 1] = 1 - a[i][i];
			} else {
				a[i][i] = 1;
			}

		}

		// Create B-matrix
		// Initialize evenly with 1/M values
		this.b = new double[numStates][sizeObservationAlphabet];
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				b[i][j] = 1.0 / sizeObservationAlphabet;
			}
		}
	}

	/**
	 * Create Gesture with existing data
	 * 
	 * @param pi
	 *            Starting distribution for states
	 * @param a
	 *            2D array of state transition probabilities
	 * @param b
	 *            2D array of state emission probabilities
	 */
	public Gesture(double[] pi, double[][] a, double[][] b) {
		this.numStates = a.length;
		this.pi = pi;
		this.a = a;
		this.b = b;
	}

	// methods
	// getter and setter
	public double[] getPi() {
		return pi;
	}

	public void setPi(double[] pi) {
		this.pi = pi;
	}

	public double[][] getA() {
		return a;
	}

	public void setA(double[][] a) {
		this.a = a;
	}

	public double[][] getB() {
		return b;
	}

	public void setB(double[][] b) {
		this.b = b;
	}

	public String getName() {
		return name;
	}

	public int getNumStates() {
		return numStates;
	}

	public void setName(String gestureName) {
		this.name = gestureName;
	}

	public int getNumMinObs() {
		return numMinObs;
	}

	public void setNumMinObs(int numMinObs) {
		this.numMinObs = numMinObs;
	}

	public int getNumMaxObs() {
		return numMaxObs;
	}

	public void setNumMaxObs(int numMaxObs) {
		this.numMaxObs = numMaxObs;
	}

}
