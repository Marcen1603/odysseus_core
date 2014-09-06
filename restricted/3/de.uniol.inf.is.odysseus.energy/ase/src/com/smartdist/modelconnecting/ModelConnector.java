/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelconnecting;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.TuneableNetworkElement;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.util.Complex;

public abstract class ModelConnector {

	public abstract double myTrueValue();

	public abstract double myTheroreticalValue(Complex[] assumedVoltages);

	public abstract double[] myJacobianRow(Complex[] assumedVoltages);

	public abstract void setMyTrueValue(double trueValue);
	
	public abstract boolean wantsFadeIn();
	
	public abstract boolean wantsDelayedStart();
	
	public abstract double getModelSensitivity(FourWireNetwork network, Complex[] expansionPoint, TuneableNetworkElement element, int tuneable);
	
	public double getModelCoverages(EstimationResultSet results) {
		double[][] rightSingularVectors = results.rightSingularVectors;
		int rank = results.rank;

		double[] tempJacobian = null;
		double[] tempProjection = new double[rank];
		double[] tempProjected = null;
		double tempNormJacobian = 0;
		double tempNormProjected = 0;
		
		double modelCoverage = 0;
		
		for (int j = 0; j < rank; j++) {
			tempProjection[j] = 0;
		}
		tempNormJacobian = 0;
		tempJacobian = this.myJacobianRow(
				results.voltages);
		for (int j = 0; j < tempJacobian.length; j++) {
			tempNormJacobian += tempJacobian[j] * tempJacobian[j];
			for (int k = 0; k < rank; k++) {
				tempProjection[k] += tempJacobian[j]
						* rightSingularVectors[j][k];
			}
		}
		tempNormJacobian = java.lang.Math.sqrt(tempNormJacobian);
		tempProjected = new double[tempJacobian.length];
		for (int j = 0; j < tempJacobian.length; j++) {
			for (int k = 0; k < rank; k++) {
				tempProjected[j] += rightSingularVectors[j][k]
						* tempProjection[k];
			}
		}
		tempNormProjected = 0;
		for (int j = 0; j < tempProjected.length; j++) {
			tempNormProjected += tempProjected[j] * tempProjected[j];
		}
		tempNormProjected = java.lang.Math.sqrt(tempNormProjected);
		if (tempNormJacobian != 0) {
			if ((tempNormProjected / tempNormJacobian) > 1.) {
				modelCoverage = 0.;
			} else {
				modelCoverage = java.lang.Math.acos((tempNormProjected / tempNormJacobian))/ java.lang.Math.PI * 180.;
			}
		} else {
			modelCoverage=90.;
		}
		return modelCoverage;
	}

	
}
