package de.uniol.inf.is.odysseus.filtering;


import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

public class KalmanCorrectStateCovarianceFunction implements IFilterFunction {
	
	private HashMap<Integer, Object> parameters;
	
	public KalmanCorrectStateCovarianceFunction() {
		this.parameters = new HashMap<Integer, Object>();
	}
	
	public KalmanCorrectStateCovarianceFunction(KalmanCorrectStateCovarianceFunction copy) {

		copy.setParameters(new HashMap<Integer, Object>(this.getParameters()));	
		
	}
	
	public KalmanCorrectStateCovarianceFunction(HashMap<Integer, Object> parameters) {
		this.parameters=parameters;
	}
	
	@Override
	/**
	 * This method computes the new state covariance
	 */
	public double[][] compute() {
		
		double[][] result;
		
		RealMatrix covarianceOld = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.OLD_COVARIANCE));
		RealMatrix covarianceNew = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.NEW_COVARIANCE));
		RealMatrix gain = new RealMatrixImpl((double[][]) this.parameters.get(HashConstants.GAIN));
		RealMatrix identityMatrixOfGain = new RealMatrixImpl(makeIdentityMatrix(gain.getData()));
		
		// (I-K)Pk(I-K)^t + KRK^t
		RealMatrix temp = new RealMatrixImpl();
		
		// I - K
		RealMatrix term1 = new RealMatrixImpl();
		term1 = identityMatrixOfGain.subtract(gain);
		
		temp = term1.multiply(covarianceOld);
		
		temp = temp.multiply(term1.transpose());
		
		RealMatrix term2 = new RealMatrixImpl();
		
		// KRK^T
		term2 = gain.multiply(covarianceNew);
		
		term2 = term2.multiply(gain.transpose());
		
		temp = temp.add(term2);
		
		result = temp.getData();
		
		return result;
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
