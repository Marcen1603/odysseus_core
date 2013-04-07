package de.uniol.inf.is.odysseus.hmm;

public class Gesture {
	//Attributes
	String name;
	double[][] a;
	double[][] b;
	
	//Constructor
	public Gesture(String pName, double[][] pA, double[][] pB){
		this.name = pName;
		this.a = pA;
		this.b = pB;		
	}


	
	//Methods
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
}
