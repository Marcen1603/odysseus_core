/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.solvers.stateestimator;

import com.smartdist.util.Complex;
import com.smartdist.util.SingularValueDecomposer;

public class LinearStateEstimator {

	private Inputs myInputs = null;
	private Observers myOutputs = null;
	private EstimationResultSet myExpansionPoint = null;
	private double[] voltageExpansionPoint = null;
	private double[] inputExpansionPoint = null;
	private double[] outputExpansionPoint = null;
	private double[][] linMap = null;

	public LinearStateEstimator(Inputs inputs, Observers outputs,
			EstimationResultSet expansionPoint) {
		super();
		this.myInputs = inputs;
		this.myOutputs = outputs;
		this.myExpansionPoint = expansionPoint;

		// setting the scene
		double[][] Jm = this.myInputs
				.getJacobian(this.myExpansionPoint.voltages);
		double[][] pJm = SingularValueDecomposer.pinv(Jm);
		double[][] Jo = this.myOutputs
				.getJacobian(this.myExpansionPoint.voltages);
		this.linMap = new double[Jo.length][pJm[0].length];
		this.voltageExpansionPoint = new double[2 * pJm.length];
		this.inputExpansionPoint = new double[this.myInputs
				.numberOfConnectors()];
		this.outputExpansionPoint = new double[this.myOutputs
				.numberOfConnectors()];

		if (pJm.length == Jo[0].length) {
			for (int i = 0; i < pJm.length; i++) {
				for (int j = 0; j < Jo.length; j++) {
					for (int k = 0; k < pJm[0].length; k++) {
						this.linMap[j][k] += pJm[i][k] * Jo[j][i];
					}
				}
			}
			Complex[] tempVoltages = this.myExpansionPoint.voltages;
			for (int i = 0; i < tempVoltages.length; i++) {
				this.voltageExpansionPoint[2 * i] = tempVoltages[i].getReal();
				this.voltageExpansionPoint[2 * i + 1] = tempVoltages[i]
						.getImag();
			}
			for (int i = 0; i < this.myInputs.numberOfConnectors(); i++) {
				this.inputExpansionPoint[i] = this.myInputs.getConnector(i)
						.myTheroreticalValue(this.myExpansionPoint.voltages);
			}
			for (int i = 0; i < this.myOutputs.numberOfConnectors(); i++) {
				this.outputExpansionPoint[i] = this.myOutputs.getConnector(i)
						.myTheroreticalValue(this.myExpansionPoint.voltages);
			}
		} else {
			System.out
					.println("CONSTRUCTOR LinearStateEstimator: Something is wrong with inputs and outputs. number of state variables doesn't match!");
			return;
		}
	}

	public double[] estimate(double[] inputs) {
		if (inputs.length == this.inputExpansionPoint.length) {
			double[] tempOutputs = new double[this.outputExpansionPoint.length];
			for (int i = 0; i < this.outputExpansionPoint.length; i++) {
				tempOutputs[i] = this.outputExpansionPoint[i];
				for (int j = 0; j < this.linMap[0].length; j++) {
					tempOutputs[i] += this.linMap[i][j]
							* (inputs[j] - this.inputExpansionPoint[j]);
				}
			}
			return tempOutputs;
		} else {
			return null;
		}
	}
	
	public double[][] getLinearMap(){
		return this.linMap;
	}
}
