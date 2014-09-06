/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.eightterminalelements;

import com.smartdist.util.Complex;

public class Line implements
		com.smartdist.modelling.network.EightTerminalElement {

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

	// mutual inductivity per meter
	public double mutualInductivityAB = 0;// -0.0001;
	public double mutualInductivityAC = 0;// -0.0001;
	public double mutualInductivityAN = 0;// -0.0001;
	public double mutualInductivityBC = 0;// -0.0001;
	public double mutualInductivityBN = 0;// -0.0001;
	public double mutualInductivityCN = 0;// -0.0001;

	// line-to-ground conductivity per meter (for the entire line.
	// it will be separated for the Pi-equivalent circuit when stating the
	// admittance matrix)
	public double toGroundConductivityA = 0;// .0000000001;
	public double toGroundConductivityB = 0;// .0000000001;
	public double toGroundConductivityC = 0;// .0000000001;
	public double toGroundConductivityN = 0;// .0000000001;

	// mutual conductivities per meter (for the entire line.
	// it will be separated for the Pi-equivalent circuit when stating the
	// admittance matrix)
	public double lineToLineConductivityAB = 0;// .0000000001;
	public double lineToLineConductivityAC = 0;// .0000000001;
	public double lineToLineConductivityAN = 0;// .0000000001;
	public double lineToLineConductivityBC = 0;// .0000000001;
	public double lineToLineConductivityBN = 0;// .0000000001;
	public double lineToLineConductivityCN = 0;// .0000000001;

	// line-to-ground capacitance per meter (for the entire line.
	// it will be separated for the Pi-equivalent circuit when stating the
	// admittance matrix)
	public double toGroundCapacitanceA = 0.00000000005;
	public double toGroundCapacitanceB = 0.00000000005;
	public double toGroundCapacitanceC = 0.00000000005;
	public double toGroundCapacitanceN = 0.00000000005;

	// mutual capacitances per meter (for the entire line.
	// it will be separated for the Pi-equivalent circuit when stating the
	// admittance matrix)
	public double lineToLineCapacitanceAB = 0;// .0000000001;
	public double lineToLineCapacitanceAC = 0;// .0000000001;
	public double lineToLineCapacitanceAN = 0;// .0000000001;
	public double lineToLineCapacitanceBC = 0;// .0000000001;
	public double lineToLineCapacitanceBN = 0;// .0000000001;
	public double lineToLineCapacitanceCN = 0;// .0000000001;

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

		// //////////////////////////////////////////////////
		//
		// mutual inductance
		// M should have typical values around -10E-4
		//
		// //////////////////////////////////////////////////

		/*
		 * if ((this.phaseA)&&(this.phaseB)){ if ((this.mutualInductivityAB !=
		 * 0)&&(this.length > 0)){ tempComplex = new
		 * Complex(2*java.lang.Math.PI*
		 * nominalFrequency*this.serialInductivityB,-this.serialResistivityB);
		 * tempComplex.multiply(new Complex(this.length,0));
		 * tempComplex.divide(new
		 * Complex(this.mutualInductivityAB*this.length*4*
		 * java.lang.Math.PI*nominalFrequency,0)); tempComplex.sustract(new
		 * Complex(0.5,0)); tempComplex.multiply(new
		 * Complex(this.serialResistivityA
		 * ,2*java.lang.Math.PI*nominalFrequency*this.serialInductivityA));
		 * tempComplex.multiply(new Complex(this.length,0));
		 * tempComplex.invert();
		 * lineAdmittanceMatrix[4*sourceNode][4*sourceNode+1].add(tempComplex);
		 * tempComplex.negate();
		 * lineAdmittanceMatrix[4*sourceNode][4*targetNode+1].add(tempComplex);
		 * tempComplex = new
		 * Complex(2*java.lang.Math.PI*nominalFrequency*this.serialInductivityA
		 * ,-this.serialResistivityA); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityAB
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityB,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityB)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*sourceNode+1][4*sourceNode].add(tempComplex);
		 * tempComplex.negate();
		 * lineAdmittanceMatrix[4*sourceNode+1][4*targetNode].add(tempComplex);
		 * } } if ((this.phaseA)&&(this.phaseC)){ if ((this.mutualInductivityAC
		 * != 0)&&(this.length > 0)){ tempComplex = new
		 * Complex(2*java.lang.Math.
		 * PI*nominalFrequency*this.serialInductivityC,-
		 * this.serialResistivityC); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityAC
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityA,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityA)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*sourceNode][4*sourceNode+2].add(tempComplex);
		 * tempComplex.negate();
		 * lineAdmittanceMatrix[4*sourceNode][4*targetNode+2].add(tempComplex);
		 * tempComplex = new
		 * Complex(2*java.lang.Math.PI*nominalFrequency*this.serialInductivityA
		 * ,-this.serialResistivityA); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityAC
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityC,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityC)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*sourceNode+2][4*sourceNode].add(tempComplex);
		 * tempComplex.negate();
		 * lineAdmittanceMatrix[4*sourceNode+2][4*targetNode].add(tempComplex);
		 * } } if ((this.phaseA)&&(this.phaseN)){ if ((this.mutualInductivityAN
		 * != 0)&&(this.length > 0)){ tempComplex = new
		 * Complex(2*java.lang.Math.
		 * PI*nominalFrequency*this.serialInductivityN,-
		 * this.serialResistivityN); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityAN
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityA,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityA)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*sourceNode][4*sourceNode+3].add(tempComplex);
		 * tempComplex.negate();
		 * lineAdmittanceMatrix[4*sourceNode][4*targetNode+3].add(tempComplex);
		 * tempComplex = new
		 * Complex(2*java.lang.Math.PI*nominalFrequency*this.serialInductivityA
		 * ,-this.serialResistivityA); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityAN
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityN,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityN)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*sourceNode+3][4*sourceNode].add(tempComplex);
		 * tempComplex.negate();
		 * lineAdmittanceMatrix[4*sourceNode+3][4*targetNode].add(tempComplex);
		 * } } if ((this.phaseB)&&(this.phaseC)){ if ((this.mutualInductivityBC
		 * != 0)&&(this.length > 0)){ tempComplex = new
		 * Complex(2*java.lang.Math.
		 * PI*nominalFrequency*this.serialInductivityC,-
		 * this.serialResistivityC); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityBC
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityB,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityB)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*
		 * sourceNode+1][4*sourceNode+2].add(tempComplex); tempComplex.negate();
		 * lineAdmittanceMatrix
		 * [4*sourceNode+1][4*targetNode+2].add(tempComplex); tempComplex = new
		 * Complex
		 * (2*java.lang.Math.PI*nominalFrequency*this.serialInductivityB,-
		 * this.serialResistivityB); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityBC
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityC,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityC)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*
		 * sourceNode+2][4*sourceNode+1].add(tempComplex); tempComplex.negate();
		 * lineAdmittanceMatrix
		 * [4*sourceNode+2][4*targetNode+1].add(tempComplex); } } if
		 * ((this.phaseB)&&(this.phaseN)){ if ((this.mutualInductivityBN !=
		 * 0)&&(this.length > 0)){ tempComplex = new
		 * Complex(2*java.lang.Math.PI*
		 * nominalFrequency*this.serialInductivityN,-this.serialResistivityN);
		 * tempComplex.multiply(new Complex(this.length,0));
		 * tempComplex.divide(new
		 * Complex(this.mutualInductivityBN*this.length*4*
		 * java.lang.Math.PI*nominalFrequency,0)); tempComplex.sustract(new
		 * Complex(0.5,0)); tempComplex.multiply(new
		 * Complex(this.serialResistivityB
		 * ,2*java.lang.Math.PI*nominalFrequency*this.serialInductivityB));
		 * tempComplex.multiply(new Complex(this.length,0));
		 * tempComplex.invert();
		 * lineAdmittanceMatrix[4*sourceNode+1][4*sourceNode
		 * +3].add(tempComplex); tempComplex.negate();
		 * lineAdmittanceMatrix[4*sourceNode
		 * +1][4*targetNode+3].add(tempComplex); tempComplex = new
		 * Complex(2*java
		 * .lang.Math.PI*nominalFrequency*this.serialInductivityB,-
		 * this.serialResistivityB); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityBN
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityN,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityN)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*
		 * sourceNode+3][4*sourceNode+1].add(tempComplex); tempComplex.negate();
		 * lineAdmittanceMatrix
		 * [4*sourceNode+3][4*targetNode+1].add(tempComplex); } } if
		 * ((this.phaseC)&&(this.phaseN)){ if ((this.mutualInductivityCN !=
		 * 0)&&(this.length > 0)){ tempComplex = new
		 * Complex(2*java.lang.Math.PI*
		 * nominalFrequency*this.serialInductivityN,-this.serialResistivityN);
		 * tempComplex.multiply(new Complex(this.length,0));
		 * tempComplex.divide(new
		 * Complex(this.mutualInductivityCN*this.length*4*
		 * java.lang.Math.PI*nominalFrequency,0)); tempComplex.sustract(new
		 * Complex(0.5,0)); tempComplex.multiply(new
		 * Complex(this.serialResistivityC
		 * ,2*java.lang.Math.PI*nominalFrequency*this.serialInductivityC));
		 * tempComplex.multiply(new Complex(this.length,0));
		 * tempComplex.invert();
		 * lineAdmittanceMatrix[4*sourceNode+2][4*sourceNode
		 * +3].add(tempComplex); tempComplex.negate();
		 * lineAdmittanceMatrix[4*sourceNode
		 * +2][4*targetNode+3].add(tempComplex); tempComplex = new
		 * Complex(2*java
		 * .lang.Math.PI*nominalFrequency*this.serialInductivityC,-
		 * this.serialResistivityC); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.divide(new
		 * Complex(this.mutualInductivityCN
		 * *this.length*4*java.lang.Math.PI*nominalFrequency,0));
		 * tempComplex.sustract(new Complex(0.5,0)); tempComplex.multiply(new
		 * Complex
		 * (this.serialResistivityN,2*java.lang.Math.PI*nominalFrequency*this
		 * .serialInductivityN)); tempComplex.multiply(new
		 * Complex(this.length,0)); tempComplex.invert();
		 * lineAdmittanceMatrix[4*
		 * sourceNode+3][4*sourceNode+2].add(tempComplex); tempComplex.negate();
		 * lineAdmittanceMatrix
		 * [4*sourceNode+3][4*targetNode+2].add(tempComplex); } }
		 */
		return lineAdmittanceMatrix;
	}

}
