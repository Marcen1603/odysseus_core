/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.eightterminalelements;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.util.Complex;

public class SimpleFixedTapTransformer implements
		com.smartdist.modelling.network.EightTerminalElement {
	/**
	 * This Model is only usable in per unit calculations!!!
	 * 
	 */
	
	// MEMBER VARIABLES

	public double shortCircuitL = 0.001;
	public double shortCircuitR = 0.0;
	public Node nominalNode = null;
	public Node tapNode = null;
	public double turnsRatio;
	public FourWireNetwork network = null;

	public SimpleFixedTapTransformer(FourWireNetwork network, Node tapNode,
			Node nominalNode, double shortCircuitL, double shortCircuitR,
			double turnsRatio) {
		super();
		this.network = network;
		this.shortCircuitL = shortCircuitL;
		this.shortCircuitR = shortCircuitR;
		this.turnsRatio = turnsRatio;
		this.tapNode = tapNode;
		this.nominalNode = nominalNode;
	}
	
	public double getTurnsRatio(){
		return this.turnsRatio;
	}
	
	public void setTurnsRatio(double newTurnsRatio){
		this.turnsRatio = newTurnsRatio;
	}

	@Override
	public Complex[][] generateAdmittanceMatrix(double nominalFrequency) {
		// TODO Auto-generated method stub
		this.nominalNode = this.network.getSource(this);
		this.tapNode = this.network.getDest(this);

		Complex tempComplex = null;
		int sourceNode = 0;
		int targetNode = 1;
		double tapSideV = this.tapNode.nominalVoltage;
		double nominalSideV = this.nominalNode.nominalVoltage;
		double tapSideI = 1.0;
		double nominalSideI = (tapSideV * tapSideI) / nominalSideV;
		Complex[][] lineAdmittanceMatrix = new Complex[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				lineAdmittanceMatrix[i][j] = new Complex(0, 0);
			}
		}

		tempComplex = new Complex(this.shortCircuitR, 2 * java.lang.Math.PI
				* nominalFrequency * this.shortCircuitL);
		tempComplex.invert();
		tempComplex.multiply((tapSideI / tapSideV));
		tempComplex.divide(this.turnsRatio * this.turnsRatio); //
		lineAdmittanceMatrix[4 * sourceNode + 0][4 * sourceNode + 0].add(tempComplex);
		lineAdmittanceMatrix[4 * sourceNode + 1][4 * sourceNode + 1].add(tempComplex);
		lineAdmittanceMatrix[4 * sourceNode + 2][4 * sourceNode + 2].add(tempComplex);
		// add artificial grounding to neutral
		lineAdmittanceMatrix[4 * sourceNode + 3][4 * sourceNode + 3].add(new Complex(1000, 0));

		tempComplex = new Complex(this.shortCircuitR, 2 * java.lang.Math.PI
				* nominalFrequency * this.shortCircuitL);
		tempComplex.invert();
		tempComplex.multiply((-tapSideI / nominalSideV));
		// tempComplex.multiply(this.turnsRatio);
		tempComplex.divide(this.turnsRatio);
		lineAdmittanceMatrix[4 * sourceNode + 0][4 * targetNode + 0].add(tempComplex);
		lineAdmittanceMatrix[4 * sourceNode + 1][4 * targetNode + 1].add(tempComplex);
		lineAdmittanceMatrix[4 * sourceNode + 2][4 * targetNode + 2].add(tempComplex);

		tempComplex = new Complex(this.shortCircuitR, 2 * java.lang.Math.PI
				* nominalFrequency * this.shortCircuitL);
		tempComplex.invert();
		tempComplex.multiply((-nominalSideI / tapSideV));
		// tempComplex.multiply(this.turnsRatio);
		tempComplex.divide(this.turnsRatio);
		lineAdmittanceMatrix[4 * targetNode + 0][4 * sourceNode + 0].add(tempComplex);
		lineAdmittanceMatrix[4 * targetNode + 1][4 * sourceNode + 1].add(tempComplex);
		lineAdmittanceMatrix[4 * targetNode + 2][4 * sourceNode + 2].add(tempComplex);

		tempComplex = new Complex(this.shortCircuitR, 2 * java.lang.Math.PI
				* nominalFrequency * this.shortCircuitL);
		tempComplex.invert();
		tempComplex.multiply((nominalSideI / nominalSideV));
		// tempComplex.multiply(this.turnsRatio*this.turnsRatio);
		lineAdmittanceMatrix[4 * targetNode + 0][4 * targetNode + 0].add(tempComplex);
		lineAdmittanceMatrix[4 * targetNode + 1][4 * targetNode + 1].add(tempComplex);
		lineAdmittanceMatrix[4 * targetNode + 2][4 * targetNode + 2].add(tempComplex);
		// add artificial grounding to neutral
		lineAdmittanceMatrix[4 * targetNode + 3][4 * targetNode + 3].add(new Complex(1000, 0));

		return lineAdmittanceMatrix;
	}

}
