/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.eightterminalelements;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.util.Complex;

public class SinglePhaseLengthTuneableLine implements
		com.smartdist.modelling.network.TuneableEightTerminalElement {

	// MEMBER VARIABLES

	public boolean phaseA = true;
	public boolean phaseB = true;
	public boolean phaseC = true;
	public boolean phaseN = true;

	public double lengthA = 500; // in meter !!
	public double lengthB = 500;
	public double lengthC = 500;
	public double lengthN = 500;
	
	// serial resistivity per meter
	public double serialResistivityA = 0.000106;
	public double serialResistivityB = 0.000106;
	public double serialResistivityC = 0.000106;
	public double serialResistivityN = 0.000106;

	// serial inductivity per meter
	public double serialInductivityA = 0.000000277;
	public double serialInductivityB = 0.000000277;
	public double serialInductivityC = 0.000000277;
	public double serialInductivityN = 0.000000277;


	@Override
	public Complex[][] generateAdmittanceMatrix(double nominalFrequency) {
		// TODO Auto-generated method stub

		Complex tempComplex = null;
		int sourceNode = 0;
		int targetNode = 1;
		Complex[][] lineAdmittanceMatrix = new Complex[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				lineAdmittanceMatrix[i][j] = new Complex(0, 0);
			}
		}

		if (this.phaseA) {
			tempComplex = new Complex(this.serialResistivityA, 2
					* java.lang.Math.PI * nominalFrequency
					* this.serialInductivityA);
			tempComplex.multiply(new Complex(this.lengthA, 0));
			tempComplex.invert();
			lineAdmittanceMatrix[4 * sourceNode][4 * sourceNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * targetNode]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode][4 * targetNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * sourceNode]
					.add(tempComplex);
		}
		if (this.phaseB) {
			tempComplex = new Complex(this.serialResistivityB, 2
					* java.lang.Math.PI * nominalFrequency
					* this.serialInductivityB);
			tempComplex.multiply(new Complex(this.lengthB, 0));
			tempComplex.invert();
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode + 1]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * targetNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * sourceNode + 1]
					.add(tempComplex);
		}
		if (this.phaseC) {
			tempComplex = new Complex(this.serialResistivityC, 2
					* java.lang.Math.PI * nominalFrequency
					* this.serialInductivityC);
			tempComplex.multiply(new Complex(this.lengthC, 0));
			tempComplex.invert();
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode + 2]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * targetNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * sourceNode + 2]
					.add(tempComplex);
		}
		if (this.phaseN) {
			tempComplex = new Complex(this.serialResistivityN, 2
					* java.lang.Math.PI * nominalFrequency
					* this.serialInductivityN);
			tempComplex.multiply(new Complex(this.lengthN, 0));
			tempComplex.invert();
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode + 3]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * targetNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * sourceNode + 3]
					.add(tempComplex);
		}
		return lineAdmittanceMatrix;
	}

	@Override
	public double[] getTuneables() {
		double[] temp = new double[4];
		temp[0] = this.lengthA;
		temp[1] = this.lengthB;
		temp[2] = this.lengthC;
		temp[3] = this.lengthN;
		return temp;
	}

	@Override
	public void setTunables(double[] tuneables) {
		if ((tuneables[0]>10)){//&&(tuneables[0]<100000)){
			this.lengthA = tuneables[0];					
		}
//		this.length = tuneables[0];
		if ((tuneables[1]>10)){//&&(tuneables[0]<100000)){
			this.lengthB = tuneables[1];					
		}
//		this.length = tuneables[0];
		if ((tuneables[2]>10)){//&&(tuneables[0]<100000)){
			this.lengthC = tuneables[2];					
		}
//		this.length = tuneables[0];
		if ((tuneables[3]>10)){//&&(tuneables[0]<100000)){
			this.lengthN = tuneables[3];					
		}
//		this.length = tuneables[0];
	}

	@Override
	public double[] getSensitivityAnalysisStepSize() {
		double[] temp = new double[1];
		temp[0] = 0.01*(this.lengthA+this.lengthB+this.lengthC+this.lengthN)/4.;
		return temp;
	}

	@Override
	public Complex[][] getInfinitisimalAdmittanceChange(FourWireNetwork network, Complex[] expansionPoint, int tuneable) {
		Complex[][] temp = null;
		if ((0<=tuneable)&&(tuneable<4)){
			temp = new Complex[8][8];
			Complex tempComplex = null;
			int sourceNode = 0;
			int targetNode = 1;
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					temp[i][j] = new Complex(0, 0);
				}
			}
			///////////////////////////////
			// Diagonal elements behave differently.
			// All else can be populated with per length values
			//////////////////////////////
			
			
			////////////////////////////
			// Main diagonal elements should now contain linearly scaling shunt admittances. Need to add serial admittances
			/////////////////////////
			
			if ((this.phaseA)&&(tuneable==0)){
				if (this.lengthA !=0){
					tempComplex = new Complex(serialResistivityA, (serialInductivityA*network.nominalFrequency*2*java.lang.Math.PI));
					tempComplex.invert();
					tempComplex.divide((this.lengthA*this.lengthA));
				}
				temp[4*sourceNode+0][4*sourceNode+0].sustract(tempComplex);
				temp[4*targetNode+0][4*targetNode+0].sustract(tempComplex);
				temp[4*sourceNode+0][4*targetNode+0].add(tempComplex);
				temp[4*targetNode+0][4*sourceNode+0].add(tempComplex);
			}
			if ((this.phaseB)&&(tuneable==1)){
				if (this.lengthB !=0){
					tempComplex = new Complex(serialResistivityB, (serialInductivityB*network.nominalFrequency*2*java.lang.Math.PI));
					tempComplex.invert();
					tempComplex.divide((this.lengthB*this.lengthB));
				}
				temp[4*sourceNode+1][4*sourceNode+1].sustract(tempComplex);
				temp[4*targetNode+1][4*targetNode+1].sustract(tempComplex);
				temp[4*sourceNode+1][4*targetNode+1].add(tempComplex);
				temp[4*targetNode+1][4*sourceNode+1].add(tempComplex);				
			}
			if ((this.phaseC)&&(tuneable==2)){
				if (this.lengthC !=0){
					tempComplex = new Complex(serialResistivityC, (serialInductivityC*network.nominalFrequency*2*java.lang.Math.PI));
					tempComplex.invert();
					tempComplex.divide((this.lengthC*this.lengthC));
				}
				temp[4*sourceNode+2][4*sourceNode+2].sustract(tempComplex);
				temp[4*targetNode+2][4*targetNode+2].sustract(tempComplex);
				temp[4*sourceNode+2][4*targetNode+2].add(tempComplex);
				temp[4*targetNode+2][4*sourceNode+2].add(tempComplex);				
			}
			if ((this.phaseN)&&(tuneable==3)){
				if (this.lengthN !=0){
					tempComplex = new Complex(serialResistivityN, (serialInductivityN*network.nominalFrequency*2*java.lang.Math.PI));
					tempComplex.invert();
					tempComplex.divide((this.lengthN*this.lengthN));
				}
				temp[4*sourceNode+3][4*sourceNode+3].sustract(tempComplex);
				temp[4*targetNode+3][4*targetNode+3].sustract(tempComplex);
				temp[4*sourceNode+3][4*targetNode+3].add(tempComplex);
				temp[4*targetNode+3][4*sourceNode+3].add(tempComplex);				
			}
	
		}
		return temp;
	}
	
	public void setAllLengths(double length){
		this.lengthA = length;
		this.lengthB = length;
		this.lengthC = length;
		this.lengthN = length;
	}

}
