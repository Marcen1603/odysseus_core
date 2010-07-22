package de.uniol.inf.is.odysseus.filtering;


import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

public class KalmanCorrectStateCovarianceFunction implements ICorrectStateCovarianceFunction {
	
	private HashMap<Integer, Object> parameters;
	
	public KalmanCorrectStateCovarianceFunction() {
		
	}
	
	public KalmanCorrectStateCovarianceFunction(HashMap<Integer, Object> parameters) {
		/*this.parameters.put("covarianceOld", covarianceOld );
		this.parameters.put("gain", gain);*/
		this.parameters=parameters;
	}
	
	@Override
	public double[][] correctStateCovariance() {
		RealMatrix covarianceOld = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.OLD_COVARIANCE));
		RealMatrix gain = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.GAIN));
		RealMatrix identityMatrixOfGain = new RealMatrixImpl(makeIdentityMatrix(gain.getData()));
		return identityMatrixOfGain.subtract(gain).multiply(covarianceOld).getData();
	}
	

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the parameters
	 */
	public HashMap<Integer, Object> getParameters() {
		
		return parameters;
	} 
	
	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(Integer key, Object value) {
		this.parameters.put(key, value);
	}


	@Override
	public int getFunctionID() {
		return 1;
	}
	
	public static double[][] makeIdentityMatrix(double[][] template) {
		double[][] identityMatrix = new double[template.length][template.length];
		for (int i = 0; i<template.length; i++) {
			for (int j = 0; j<template.length; j++) {
				if (i == j) {
					identityMatrix[i][j] = 1;
				} else if (i != j) {
					identityMatrix[i][j] = 0;
				}
			}
		}
		return identityMatrix;
	}

}
