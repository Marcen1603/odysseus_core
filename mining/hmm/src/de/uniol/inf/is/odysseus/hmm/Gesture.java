package de.uniol.inf.is.odysseus.hmm;

public class Gesture {
	//Attributes
	private String name;
	private int numStates;
	private double[] pi;
	private double[][] a;
	private double[][] b;
	
	//Constructor
	public Gesture(String pName, double[] pPi, double[][] pA, double[][] pB){
		this.name = pName;
		this.setPi(pPi);
		this.a = pA;
		this.b = pB;		
	}


	
	//Methods
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
	
}
