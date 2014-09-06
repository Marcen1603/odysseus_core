/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network;

import java.util.Iterator;
import java.util.Vector;

import com.smartdist.util.Complex;
import com.smartdist.util.NamedElement;

public class Node extends NamedElement {

	public double nominalVoltage; // phase-to-N voltage
	public Vector<FourTerminalElement> myFourTerminalElements = new Vector<FourTerminalElement>();
	private int modelNumber = 0;

	// Achtung HACK!!
	public double tempValue = 90; // lowest angle of all nodal measurements

	public Node(double nominalVoltage) {
		super();
		this.nominalVoltage = nominalVoltage;
	}

	public void setModelNumber(int assignedModelNumber) {
		this.modelNumber = assignedModelNumber;
	}

	public int getModelNumber() {
		return this.modelNumber;
	}

	public Complex[][] generateAdmittanceMatrix(double nominalFrequency) {

		// initialize admittance matrix
		Complex[][] nodeAdmittanceMatrix = new Complex[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				nodeAdmittanceMatrix[i][j] = new Complex(0, 0);
			}
		}

		// Invoke getAdmittance Matrix of al elements
		Iterator<FourTerminalElement> elementIterator = myFourTerminalElements
				.iterator();
		FourTerminalElement tempElement = null;
		Complex[][] tempFourMatrix = null;
		for (; elementIterator.hasNext();) {
			tempElement = elementIterator.next();
			tempFourMatrix = tempElement
					.generateAdmittanceMatrix(nominalFrequency);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					nodeAdmittanceMatrix[i][j].add(tempFourMatrix[i][j]);
				}
			}
		}

		return nodeAdmittanceMatrix;
	}

	public Complex[] getStartVoltage(double absolute, double angle) {
		double startJitter = 0.0;
		Complex[] voltages = new Complex[4];
		voltages[0] = new Complex(Math.cos(angle / 180. * Math.PI)+Math.random()*startJitter,
				Math.sin(angle / 180. * Math.PI)+Math.random()*startJitter).multiply(absolute);
		voltages[1] = new Complex(Math.cos((angle + 240) / 180. * Math.PI)+Math.random()*startJitter,
				Math.sin((angle + 120) / 180. * Math.PI)+Math.random()*startJitter).multiply(absolute);
		voltages[2] = new Complex(Math.cos((angle + 120) / 180. * Math.PI)+Math.random()*startJitter,
				Math.sin((angle + 240) / 180. * Math.PI)+Math.random()*startJitter).multiply(absolute);
		voltages[3] = new Complex(Math.random()*startJitter, Math.random()*startJitter);

		return voltages;
	}

	public String toString() {
		return modelNumber + "(" + Math.round(tempValue * 100.) / 100. + ")";
	}

}
