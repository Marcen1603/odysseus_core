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

/**
 * This is just a simple example which doesn't take the nominalFrequency into
 * account. You may wish to implement more sophisticated models
 * 
 * @author Dr. Olav Krause
 * 
 */
public class StaticCompensator implements FourTerminalElement {

	// MEMBER VARIABLES

	int phaseA = 0;
	int phaseB = 1;
	int phaseC = 2;
	int phaseN = 3;

	// Special elements are thought to be admittances directly attached between
	// phases
	// or phases and ground, directly within the node. This could be e.g. a
	// Neutral-to-Ground admittance
	public boolean hasSpecialElements = true;
	public Complex elementAtoB = new Complex(1E-6, 0);
	public Complex elementAtoC = new Complex(2E-6, 0);
	public Complex elementAtoN = new Complex(4E-6, 0);
	public Complex elementBtoC = new Complex(8E-6, 0);
	public Complex elementBtoN = new Complex(16E-6, 0);
	public Complex elementCtoN = new Complex(32E-6, 0);

	public Complex elementAtoGround = new Complex(0, 1E-6);
	public Complex elementBtoGround = new Complex(0, 2E-6);
	public Complex elementCtoGround = new Complex(0, 4E-6);
	public Complex elementNtoGround = new Complex(0, 8E-6);

	@Override
	public Complex[][] generateAdmittanceMatrix(double nominalFrequency) {
		// TODO Auto-generated method stub

		Complex[][] compensatorAdmittanceMatrix = new Complex[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				compensatorAdmittanceMatrix[i][j] = new Complex(0, 0);
			}
		}

		compensatorAdmittanceMatrix[phaseA][phaseA].add(this.elementAtoB);
		compensatorAdmittanceMatrix[phaseA][phaseB].sustract(this.elementAtoB);
		compensatorAdmittanceMatrix[phaseB][phaseA].sustract(this.elementAtoB);
		compensatorAdmittanceMatrix[phaseB][phaseB].add(this.elementAtoB);

		compensatorAdmittanceMatrix[phaseA][phaseA].add(this.elementAtoC);
		compensatorAdmittanceMatrix[phaseA][phaseC].sustract(this.elementAtoC);
		compensatorAdmittanceMatrix[phaseC][phaseA].sustract(this.elementAtoC);
		compensatorAdmittanceMatrix[phaseC][phaseC].add(this.elementAtoC);

		compensatorAdmittanceMatrix[phaseA][phaseA].add(this.elementAtoN);
		compensatorAdmittanceMatrix[phaseA][phaseN].sustract(this.elementAtoN);
		compensatorAdmittanceMatrix[phaseN][phaseA].sustract(this.elementAtoN);
		compensatorAdmittanceMatrix[phaseN][phaseN].add(this.elementAtoN);

		compensatorAdmittanceMatrix[phaseB][phaseB].add(this.elementBtoC);
		compensatorAdmittanceMatrix[phaseB][phaseC].sustract(this.elementBtoC);
		compensatorAdmittanceMatrix[phaseC][phaseB].sustract(this.elementBtoC);
		compensatorAdmittanceMatrix[phaseC][phaseC].add(this.elementBtoC);

		compensatorAdmittanceMatrix[phaseB][phaseB].add(this.elementBtoN);
		compensatorAdmittanceMatrix[phaseB][phaseN].sustract(this.elementBtoN);
		compensatorAdmittanceMatrix[phaseN][phaseB].sustract(this.elementBtoN);
		compensatorAdmittanceMatrix[phaseN][phaseN].add(this.elementBtoN);

		compensatorAdmittanceMatrix[phaseC][phaseC].add(this.elementCtoN);
		compensatorAdmittanceMatrix[phaseC][phaseN].sustract(this.elementCtoN);
		compensatorAdmittanceMatrix[phaseN][phaseC].sustract(this.elementCtoN);
		compensatorAdmittanceMatrix[phaseN][phaseN].add(this.elementCtoN);

		compensatorAdmittanceMatrix[phaseA][phaseA].add(this.elementAtoGround);
		compensatorAdmittanceMatrix[phaseB][phaseB].add(this.elementBtoGround);
		compensatorAdmittanceMatrix[phaseC][phaseC].add(this.elementCtoGround);
		compensatorAdmittanceMatrix[phaseN][phaseN].add(this.elementNtoGround);

		return compensatorAdmittanceMatrix;
	}

}
