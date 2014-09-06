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

public class LengthTuneableLine implements
		com.smartdist.modelling.network.TuneableEightTerminalElement {

	// MEMBER VARIABLES

	public boolean phaseA = true;
	public boolean phaseB = true;
	public boolean phaseC = true;
	public boolean phaseN = true;

	public double length = 100; // in meter !!

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

	// line-to-ground conductivity per meter (for the entire line.
	// it will be separated for the Pi-equivalent circuit when stating the
	// admittance matrix)
	public double toGroundConductivityA = .0000000001;
	public double toGroundConductivityB = .0000000001;
	public double toGroundConductivityC = .0000000001;
	public double toGroundConductivityN = .0000000001;

	// mutual conductivities per meter (for the entire line.
	// it will be separated for the Pi-equivalent circuit when stating the
	// admittance matrix)
	public double lineToLineConductivityAB = .0000000001;
	public double lineToLineConductivityAC = .0000000001;
	public double lineToLineConductivityAN = .0000000001;
	public double lineToLineConductivityBC = .0000000001;
	public double lineToLineConductivityBN = .0000000001;
	public double lineToLineConductivityCN = .0000000001;

	// line-to-ground capacitance per meter (for the entire line.
	// it will be separated for the Pi-equivalent circuit when stating the
	// admittance matrix)
	public double toGroundCapacitanceA = 0;//.000000005;
	public double toGroundCapacitanceB = 0;//.000000005;
	public double toGroundCapacitanceC = 0;//.000000005;
	public double toGroundCapacitanceN = 0;//.000000005;

	// mutual capacitances per meter (for the entire line.
	// it will be separated for the Pi-equivalent circuit when stating the
	// admittance matrix)
	public double lineToLineCapacitanceAB = 0;//.0000000001;
	public double lineToLineCapacitanceAC = 0;//.0000000001;
	public double lineToLineCapacitanceAN = 0;//.0000000001;
	public double lineToLineCapacitanceBC = 0;//.0000000001;
	public double lineToLineCapacitanceBN = 0;//.0000000001;
	public double lineToLineCapacitanceCN = 0;//.0000000001;

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
			tempComplex.multiply(new Complex(this.length, 0));
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
			tempComplex.multiply(new Complex(this.length, 0));
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
			tempComplex.multiply(new Complex(this.length, 0));
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
			tempComplex.multiply(new Complex(this.length, 0));
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

		// //////////////////////////////////////////////////////
		//
		// line-to-ground capacitance and conductance
		//
		// //////////////////////////////////////////////////////

		if (this.phaseA) {
			tempComplex = new Complex(this.toGroundConductivityA / 2,
					java.lang.Math.PI * nominalFrequency
							* this.toGroundCapacitanceA);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode][4 * sourceNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * targetNode]
					.add(tempComplex);
		}
		if (this.phaseB) {
			tempComplex = new Complex(this.toGroundConductivityB / 2,
					java.lang.Math.PI * nominalFrequency
							* this.toGroundCapacitanceB);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode + 1]
					.add(tempComplex);
		}
		if (this.phaseC) {
			tempComplex = new Complex(this.toGroundConductivityC / 2,
					java.lang.Math.PI * nominalFrequency
							* this.toGroundCapacitanceC);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode + 2]
					.add(tempComplex);
		}
		if (this.phaseN) {
			tempComplex = new Complex(this.toGroundConductivityN / 2,
					java.lang.Math.PI * nominalFrequency
							* this.toGroundCapacitanceN);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode + 3]
					.add(tempComplex);
		}

		// ///////////////////////////////////////////////////////////////
		//
		// linte-to-line capacitance and conductance
		//
		// ///////////////////////////////////////////////////////////////

		if ((this.phaseA) && (this.phaseB)) {
			tempComplex = new Complex(this.lineToLineConductivityAB / 2,
					java.lang.Math.PI * nominalFrequency
							* this.lineToLineCapacitanceAB);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode][4 * sourceNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * targetNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode + 1]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode][4 * sourceNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * targetNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode]
					.add(tempComplex);
		}
		if ((this.phaseA) && (this.phaseC)) {
			tempComplex = new Complex(this.lineToLineConductivityAC / 2,
					java.lang.Math.PI * nominalFrequency
							* this.lineToLineCapacitanceAC);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode][4 * sourceNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * targetNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode + 2]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode][4 * sourceNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * targetNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode]
					.add(tempComplex);
		}
		if ((this.phaseA) && (this.phaseN)) {
			tempComplex = new Complex(this.lineToLineConductivityAN / 2,
					java.lang.Math.PI * nominalFrequency
							* this.lineToLineCapacitanceAN);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode][4 * sourceNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * targetNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode + 3]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode][4 * sourceNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode][4 * targetNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode]
					.add(tempComplex);
		}
		if ((this.phaseB) && (this.phaseC)) {
			tempComplex = new Complex(this.lineToLineConductivityBC / 2,
					java.lang.Math.PI * nominalFrequency
							* this.lineToLineCapacitanceBC);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode + 2]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode + 1]
					.add(tempComplex);
		}
		if ((this.phaseB) && (this.phaseN)) {
			tempComplex = new Complex(this.lineToLineConductivityBN / 2,
					java.lang.Math.PI * nominalFrequency
							* this.lineToLineCapacitanceBN);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode + 3]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode + 1]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode + 1]
					.add(tempComplex);
		}
		if ((this.phaseC) && (this.phaseN)) {
			tempComplex = new Complex(this.lineToLineConductivityCN / 2,
					java.lang.Math.PI * nominalFrequency
							* this.lineToLineCapacitanceCN);
			tempComplex.multiply(new Complex(this.length, 0));
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode + 3]
					.add(tempComplex);
			tempComplex.negate();
			lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode + 2]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode + 3]
					.add(tempComplex);
			lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode + 2]
					.add(tempComplex);
		}
		return lineAdmittanceMatrix;
	}

	@Override
	public double[] getTuneables() {
		double[] temp = new double[1];
		temp[0] = this.length;
		return temp;
	}

	@Override
	public void setTunables(double[] tuneables) {
//		if ((tuneables[0]>10)){//&&(tuneables[0]<100000)){
//			this.length = tuneables[0];					
//		}
		this.length = tuneables[0];
	}

	@Override
	public double[] getSensitivityAnalysisStepSize() {
		double[] temp = new double[1];
		temp[0] = 0.01*this.length;
		return temp;
	}

	@Override
	public Complex[][] getInfinitisimalAdmittanceChange(FourWireNetwork network, Complex[] expansionPoint, int tuneable) {
		Complex[][] temp = null;
		if (tuneable == 0){
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
			
		

			// //////////////////////////////////////////////////////
			//
			// line-to-ground capacitance and conductance
			//
			// //////////////////////////////////////////////////////

			if (this.phaseA) {
				tempComplex = new Complex(this.toGroundConductivityA / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.toGroundCapacitanceA);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode][4 * sourceNode]
						.add(tempComplex);
				temp[4 * targetNode][4 * targetNode]
						.add(tempComplex);
			}
			if (this.phaseB) {
				tempComplex = new Complex(this.toGroundConductivityB / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.toGroundCapacitanceB);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode + 1][4 * sourceNode + 1]
						.add(tempComplex);
				temp[4 * targetNode + 1][4 * targetNode + 1]
						.add(tempComplex);
			}
			if (this.phaseC) {
				tempComplex = new Complex(this.toGroundConductivityC / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.toGroundCapacitanceC);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode + 2][4 * sourceNode + 2]
						.add(tempComplex);
				temp[4 * targetNode + 2][4 * targetNode + 2]
						.add(tempComplex);
			}
			if (this.phaseN) {
				tempComplex = new Complex(this.toGroundConductivityN / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.toGroundCapacitanceN);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode + 3][4 * sourceNode + 3]
						.add(tempComplex);
				temp[4 * targetNode + 3][4 * targetNode + 3]
						.add(tempComplex);
			}

			// ///////////////////////////////////////////////////////////////
			//
			// linte-to-line capacitance and conductance
			//
			// ///////////////////////////////////////////////////////////////

			if ((this.phaseA) && (this.phaseB)) {
				tempComplex = new Complex(this.lineToLineConductivityAB / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.lineToLineCapacitanceAB);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode][4 * sourceNode]
						.add(tempComplex);
				temp[4 * sourceNode + 1][4 * sourceNode + 1]
						.add(tempComplex);
				temp[4 * targetNode][4 * targetNode]
						.add(tempComplex);
				temp[4 * targetNode + 1][4 * targetNode + 1]
						.add(tempComplex);
				tempComplex.negate();
				temp[4 * sourceNode][4 * sourceNode + 1]
						.add(tempComplex);
				temp[4 * sourceNode + 1][4 * sourceNode]
						.add(tempComplex);
				temp[4 * targetNode][4 * targetNode + 1]
						.add(tempComplex);
				temp[4 * targetNode + 1][4 * targetNode]
						.add(tempComplex);
			}
			if ((this.phaseA) && (this.phaseC)) {
				tempComplex = new Complex(this.lineToLineConductivityAC / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.lineToLineCapacitanceAC);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode][4 * sourceNode]
						.add(tempComplex);
				temp[4 * sourceNode + 2][4 * sourceNode + 2]
						.add(tempComplex);
				temp[4 * targetNode][4 * targetNode]
						.add(tempComplex);
				temp[4 * targetNode + 2][4 * targetNode + 2]
						.add(tempComplex);
				tempComplex.negate();
				temp[4 * sourceNode][4 * sourceNode + 2]
						.add(tempComplex);
				temp[4 * sourceNode + 2][4 * sourceNode]
						.add(tempComplex);
				temp[4 * targetNode][4 * targetNode + 2]
						.add(tempComplex);
				temp[4 * targetNode + 2][4 * targetNode]
						.add(tempComplex);
			}
			if ((this.phaseA) && (this.phaseN)) {
				tempComplex = new Complex(this.lineToLineConductivityAN / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.lineToLineCapacitanceAN);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode][4 * sourceNode]
						.add(tempComplex);
				temp[4 * sourceNode + 3][4 * sourceNode + 3]
						.add(tempComplex);
				temp[4 * targetNode][4 * targetNode]
						.add(tempComplex);
				temp[4 * targetNode + 3][4 * targetNode + 3]
						.add(tempComplex);
				tempComplex.negate();
				temp[4 * sourceNode][4 * sourceNode + 3]
						.add(tempComplex);
				temp[4 * sourceNode + 3][4 * sourceNode]
						.add(tempComplex);
				temp[4 * targetNode][4 * targetNode + 3]
						.add(tempComplex);
				temp[4 * targetNode + 3][4 * targetNode]
						.add(tempComplex);
			}
			if ((this.phaseB) && (this.phaseC)) {
				tempComplex = new Complex(this.lineToLineConductivityBC / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.lineToLineCapacitanceBC);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode + 1][4 * sourceNode + 1]
						.add(tempComplex);
				temp[4 * sourceNode + 2][4 * sourceNode + 2]
						.add(tempComplex);
				temp[4 * targetNode + 1][4 * targetNode + 1]
						.add(tempComplex);
				temp[4 * targetNode + 2][4 * targetNode + 2]
						.add(tempComplex);
				tempComplex.negate();
				temp[4 * sourceNode + 1][4 * sourceNode + 2]
						.add(tempComplex);
				temp[4 * sourceNode + 2][4 * sourceNode + 1]
						.add(tempComplex);
				temp[4 * targetNode + 1][4 * targetNode + 2]
						.add(tempComplex);
				temp[4 * targetNode + 2][4 * targetNode + 1]
						.add(tempComplex);
			}
			if ((this.phaseB) && (this.phaseN)) {
				tempComplex = new Complex(this.lineToLineConductivityBN / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.lineToLineCapacitanceBN);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode + 1][4 * sourceNode + 1]
						.add(tempComplex);
				temp[4 * sourceNode + 3][4 * sourceNode + 3]
						.add(tempComplex);
				temp[4 * targetNode + 1][4 * targetNode + 1]
						.add(tempComplex);
				temp[4 * targetNode + 3][4 * targetNode + 3]
						.add(tempComplex);
				tempComplex.negate();
				temp[4 * sourceNode + 1][4 * sourceNode + 3]
						.add(tempComplex);
				temp[4 * sourceNode + 3][4 * sourceNode + 1]
						.add(tempComplex);
				temp[4 * targetNode + 1][4 * targetNode + 3]
						.add(tempComplex);
				temp[4 * targetNode + 3][4 * targetNode + 1]
						.add(tempComplex);
			}
			if ((this.phaseC) && (this.phaseN)) {
				tempComplex = new Complex(this.lineToLineConductivityCN / 2,
						java.lang.Math.PI * network.nominalFrequency
								* this.lineToLineCapacitanceCN);
				//tempComplex.multiply(new Complex(this.length, 0));
				temp[4 * sourceNode + 2][4 * sourceNode + 2]
						.add(tempComplex);
				temp[4 * sourceNode + 3][4 * sourceNode + 3]
						.add(tempComplex);
				temp[4 * targetNode + 2][4 * targetNode + 2]
						.add(tempComplex);
				temp[4 * targetNode + 3][4 * targetNode + 3]
						.add(tempComplex);
				tempComplex.negate();
				temp[4 * sourceNode + 2][4 * sourceNode + 3]
						.add(tempComplex);
				temp[4 * sourceNode + 3][4 * sourceNode + 2]
						.add(tempComplex);
				temp[4 * targetNode + 2][4 * targetNode + 3]
						.add(tempComplex);
				temp[4 * targetNode + 3][4 * targetNode + 2]
						.add(tempComplex);
			}
			
			////////////////////////////
			// Main diagonal elements should now contain linearly scaling shunt admittances. Need to add serial admittances
			/////////////////////////
			
			if (this.phaseA){
				if (this.length !=0){
					tempComplex = new Complex(serialResistivityA, (serialInductivityA*network.nominalFrequency*2*java.lang.Math.PI));
					tempComplex.invert();
					tempComplex.divide((this.length*this.length));
				}
				temp[4*sourceNode+0][4*sourceNode+0].sustract(tempComplex);
				temp[4*targetNode+0][4*targetNode+0].sustract(tempComplex);
				temp[4*sourceNode+0][4*targetNode+0].add(tempComplex);
				temp[4*targetNode+0][4*sourceNode+0].add(tempComplex);
			}
			if (this.phaseB){
				if (this.length !=0){
					tempComplex = new Complex(serialResistivityB, (serialInductivityB*network.nominalFrequency*2*java.lang.Math.PI));
					tempComplex.invert();
					tempComplex.divide((this.length*this.length));
				}
				temp[4*sourceNode+1][4*sourceNode+1].sustract(tempComplex);
				temp[4*targetNode+1][4*targetNode+1].sustract(tempComplex);
				temp[4*sourceNode+1][4*targetNode+1].add(tempComplex);
				temp[4*targetNode+1][4*sourceNode+1].add(tempComplex);				
			}
			if (this.phaseC){
				if (this.length !=0){
					tempComplex = new Complex(serialResistivityC, (serialInductivityC*network.nominalFrequency*2*java.lang.Math.PI));
					tempComplex.invert();
					tempComplex.divide((this.length*this.length));
				}
				temp[4*sourceNode+2][4*sourceNode+2].sustract(tempComplex);
				temp[4*targetNode+2][4*targetNode+2].sustract(tempComplex);
				temp[4*sourceNode+2][4*targetNode+2].add(tempComplex);
				temp[4*targetNode+2][4*sourceNode+2].add(tempComplex);				
			}
			if (this.phaseN){
				if (this.length !=0){
					tempComplex = new Complex(serialResistivityN, (serialInductivityN*network.nominalFrequency*2*java.lang.Math.PI));
					tempComplex.invert();
					tempComplex.divide((this.length*this.length));
				}
				temp[4*sourceNode+3][4*sourceNode+3].sustract(tempComplex);
				temp[4*targetNode+3][4*targetNode+3].sustract(tempComplex);
				temp[4*sourceNode+3][4*targetNode+3].add(tempComplex);
				temp[4*targetNode+3][4*sourceNode+3].add(tempComplex);				
			}
	
		}
		return temp;
	}

}
