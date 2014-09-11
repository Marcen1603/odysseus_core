/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.eightterminalelements;

import com.smartdist.modelling.network.FourTerminalElement;
import com.smartdist.util.Complex;

public class LosslessYY implements FourTerminalElement {

	// For details about the used parameters refer to documentation
	public double coreInductivity = 1; // \frac{\mu\cdot A}{s}
	public double magneticCoupeling = 0.95; // ]0..1[ percentage of the magnetic
											// excitation passing the cross
											// section of the coupled winding
	public int primaryWindings = 0;
	public int secondaryWindings = 0;

	public double guessCoreInductivity(double mu_r,
			double lengthOfMagneticCircuit, double widthOfCoreWithinWindings,
			double depthOfCoreWithinWindings) {
		return mu_r * 4 * java.lang.Math.PI * 10E-7 * widthOfCoreWithinWindings
				* depthOfCoreWithinWindings / lengthOfMagneticCircuit;
	}

	@Override
	public Complex[][] generateAdmittanceMatrix(double nominalFrequency) {
		Complex[][] admittanceMatrix = new Complex[8][8];
		Complex[][] singleWinding = new Complex[2][2];
		for (int i = 0; i < singleWinding.length; i++) {
			for (int j = 0; j < singleWinding[0].length; j++) {
				singleWinding[i][j] = new Complex(0, 0);
			}
		}
		double omega = 2 * java.lang.Math.PI * nominalFrequency;
		// I_1 against V_1
		Complex tempComplex = new Complex(1, 0);
		tempComplex.multiply(java.lang.Math.pow(this.primaryWindings, 2));
		tempComplex.multiply(new Complex(0, omega * this.coreInductivity));
		tempComplex
				.multiply((java.lang.Math.pow(this.magneticCoupeling, 2) - 1));
		tempComplex.invert();
		singleWinding[0][0] = tempComplex.clone();
		// I_1 against V_2
		tempComplex = new Complex(0,
				(-omega * this.coreInductivity * this.magneticCoupeling
						* this.primaryWindings * this.secondaryWindings));
		tempComplex.invert();
		singleWinding[0][1] = tempComplex.clone();
		tempComplex = new Complex(0, omega * this.coreInductivity);
		tempComplex
				.multiply((java.lang.Math.pow(this.magneticCoupeling, 2)
						* this.primaryWindings * this.secondaryWindings * (this.magneticCoupeling - (1 / this.magneticCoupeling))));
		tempComplex.invert();
		singleWinding[0][1].sustract(tempComplex);
		// I_2 against V_2
		tempComplex = new Complex(0, omega * this.coreInductivity);
		tempComplex.multiply(this.primaryWindings * this.secondaryWindings
				* (this.magneticCoupeling - (1 / this.magneticCoupeling)));
		tempComplex.invert();
		tempComplex.negate();
		singleWinding[1][0] = tempComplex.clone();
		// I_2 against V_2
		tempComplex = new Complex(0, omega * this.coreInductivity);
		tempComplex
				.multiply((this.magneticCoupeling
						* java.lang.Math.pow(this.secondaryWindings, 2) * (this.magneticCoupeling - (1 / this.magneticCoupeling))));
		tempComplex.invert();
		singleWinding[1][1] = tempComplex.clone();

		// Initialize admittance matrix
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				admittanceMatrix[i][j] = new Complex(0, 0);
			}
		}
		// Phase A
		// ///////////
		// I_primary / V_primary
		admittanceMatrix[0 + 0][0 + 0].add(singleWinding[0][0]);
		admittanceMatrix[0 + 0][0 + 3].sustract(singleWinding[0][0]);
		admittanceMatrix[0 + 3][0 + 0].sustract(singleWinding[0][0]);
		admittanceMatrix[0 + 3][0 + 3].add(singleWinding[0][0]);
		// I_primary / V_primary
		admittanceMatrix[0 + 0][4 + 0].add(singleWinding[0][1]);
		admittanceMatrix[0 + 0][4 + 3].sustract(singleWinding[0][1]);
		admittanceMatrix[0 + 3][4 + 0].sustract(singleWinding[0][1]);
		admittanceMatrix[0 + 3][4 + 3].add(singleWinding[0][1]);
		// I_secondary / V_primary
		admittanceMatrix[4 + 0][0 + 0].add(singleWinding[1][0]);
		admittanceMatrix[4 + 0][0 + 3].sustract(singleWinding[1][0]);
		admittanceMatrix[4 + 3][0 + 0].sustract(singleWinding[1][0]);
		admittanceMatrix[4 + 3][0 + 3].add(singleWinding[1][0]);
		// I_secondary / V_secondary
		admittanceMatrix[4 + 0][4 + 0].add(singleWinding[1][1]);
		admittanceMatrix[4 + 0][4 + 3].sustract(singleWinding[1][1]);
		admittanceMatrix[4 + 3][4 + 0].sustract(singleWinding[1][1]);
		admittanceMatrix[4 + 3][4 + 3].add(singleWinding[1][1]);
		// Phase B
		// ///////////
		// I_primary / V_primary
		admittanceMatrix[0 + 1][0 + 1].add(singleWinding[0][0]);
		admittanceMatrix[0 + 1][0 + 3].sustract(singleWinding[0][0]);
		admittanceMatrix[0 + 3][0 + 1].sustract(singleWinding[0][0]);
		admittanceMatrix[0 + 3][0 + 3].add(singleWinding[0][0]);
		// I_primary / V_primary
		admittanceMatrix[0 + 1][4 + 1].add(singleWinding[0][1]);
		admittanceMatrix[0 + 1][4 + 3].sustract(singleWinding[0][1]);
		admittanceMatrix[0 + 3][4 + 1].sustract(singleWinding[0][1]);
		admittanceMatrix[0 + 3][4 + 3].add(singleWinding[0][1]);
		// I_secondary / V_primary
		admittanceMatrix[4 + 1][0 + 1].add(singleWinding[1][0]);
		admittanceMatrix[4 + 1][0 + 3].sustract(singleWinding[1][0]);
		admittanceMatrix[4 + 3][0 + 1].sustract(singleWinding[1][0]);
		admittanceMatrix[4 + 3][0 + 3].add(singleWinding[1][0]);
		// I_secondary / V_secondary
		admittanceMatrix[4 + 1][4 + 1].add(singleWinding[1][1]);
		admittanceMatrix[4 + 1][4 + 3].sustract(singleWinding[1][1]);
		admittanceMatrix[4 + 3][4 + 1].sustract(singleWinding[1][1]);
		admittanceMatrix[4 + 3][4 + 3].add(singleWinding[1][1]);
		// Phase C
		// ///////////
		// I_primary / V_primary
		admittanceMatrix[0 + 2][0 + 2].add(singleWinding[0][0]);
		admittanceMatrix[0 + 2][0 + 3].sustract(singleWinding[0][0]);
		admittanceMatrix[0 + 3][0 + 2].sustract(singleWinding[0][0]);
		admittanceMatrix[0 + 3][0 + 3].add(singleWinding[0][0]);
		// I_primary / V_primary
		admittanceMatrix[0 + 2][4 + 2].add(singleWinding[0][1]);
		admittanceMatrix[0 + 2][4 + 3].sustract(singleWinding[0][1]);
		admittanceMatrix[0 + 3][4 + 2].sustract(singleWinding[0][1]);
		admittanceMatrix[0 + 3][4 + 3].add(singleWinding[0][1]);
		// I_secondary / V_primary
		admittanceMatrix[4 + 2][0 + 2].add(singleWinding[1][0]);
		admittanceMatrix[4 + 2][0 + 3].sustract(singleWinding[1][0]);
		admittanceMatrix[4 + 3][0 + 2].sustract(singleWinding[1][0]);
		admittanceMatrix[4 + 3][0 + 3].add(singleWinding[1][0]);
		// I_secondary / V_secondary
		admittanceMatrix[4 + 2][4 + 2].add(singleWinding[1][1]);
		admittanceMatrix[4 + 2][4 + 3].sustract(singleWinding[1][1]);
		admittanceMatrix[4 + 3][4 + 2].sustract(singleWinding[1][1]);
		admittanceMatrix[4 + 3][4 + 3].add(singleWinding[1][1]);

		return admittanceMatrix;
	}

}
