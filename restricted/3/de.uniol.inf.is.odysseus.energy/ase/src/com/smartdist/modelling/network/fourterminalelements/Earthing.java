/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.fourterminalelements;

import com.smartdist.modelling.network.FourTerminalElement;
import com.smartdist.util.Complex;

public class Earthing implements FourTerminalElement {

	public Earthing(Complex Y) {
		super();
		this.earthingAdmittance = Y;
	}

	// To make things work, we assume a standard earthing resistance of 1 Ohm
	public Complex earthingAdmittance = new Complex(1, 0);

	@Override
	public Complex[][] generateAdmittanceMatrix(double nominalFrequency) {
		// TODO Auto-generated method stub
		Complex[][] earthingAdmittanceMatrix = new Complex[4][4];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				earthingAdmittanceMatrix[i][j] = new Complex(0, 0);
			}
		}

		earthingAdmittanceMatrix[3][3] = this.earthingAdmittance.clone();

		return earthingAdmittanceMatrix;
	}

}
