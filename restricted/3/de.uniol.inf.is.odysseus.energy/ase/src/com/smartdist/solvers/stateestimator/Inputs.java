/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.solvers.stateestimator;

import java.util.Vector;

import com.smartdist.modelconnecting.ModelConnector;
import com.smartdist.util.Complex;

public class Inputs {

	Vector<ModelConnector> connectors = new Vector<ModelConnector>();
	public Vector<Double> stdDev = new Vector<Double>();
	Vector<Boolean> largeError = new Vector<Boolean>();

	// Node connectors
	public void addConnector(ModelConnector connector, double abs1stStdDev) {
		if (connector != null) {
			this.connectors.add(connector);
			this.stdDev.add(abs1stStdDev);
			this.largeError.add(false);
		}
	}

	public Vector<ModelConnector> getConnectors() {
		return this.connectors;
	}

	public Vector<Double> getStandardDeviations() {
		return this.stdDev;
	}

	public Vector<Boolean> getLargeErrorDetection() {
		return this.largeError;
	}

	public int numberOfConnectors() {
		return this.connectors.size();
	}

	public ModelConnector getConnector(int i) {
		return this.connectors.elementAt(i);
	}

	protected Vector<ModelConnector> getWeighedInputs() {
		Vector<ModelConnector> weighedInputs = new Vector<ModelConnector>();
		for (int i = 0; i < this.connectors.size(); i++) {
			weighedInputs.add(new WeighingWrapper(this.connectors.elementAt(i),
					this.stdDev.elementAt(i)));
		}
		return weighedInputs;
	}

	public double[][] getJacobian(Complex[] assumedVoltages) {
		double[][] Jacobian = new double[this.connectors.size()][2 * assumedVoltages.length];
		double[] temp = null;
		for (int i = 0; i < Jacobian.length; i++) {
			temp = this.connectors.elementAt(i).myJacobianRow(assumedVoltages);
			for (int j = 0; j < Jacobian[i].length; j++) {
				Jacobian[i][j] = temp[j];
			}
		}
		return Jacobian;
	}

}
