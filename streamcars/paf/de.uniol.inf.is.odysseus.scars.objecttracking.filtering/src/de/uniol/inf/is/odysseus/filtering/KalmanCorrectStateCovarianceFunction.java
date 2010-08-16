package de.uniol.inf.is.odysseus.filtering;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

public class KalmanCorrectStateCovarianceFunction extends AbstractMetaDataUpdateFunction {
	
	
	
	public KalmanCorrectStateCovarianceFunction() {
	super();
	}
	
	public KalmanCorrectStateCovarianceFunction(KalmanCorrectStateCovarianceFunction copy) {

		copy.setParameters(new HashMap<Integer, Object>(this.getParameters()));	
		
	}
	
	public KalmanCorrectStateCovarianceFunction(HashMap<Integer, Object> parameters) {
		this.setParameters(parameters);
	}
	
	@Override
	/**
	 * This method computes the new state covariance
	 */
	public void compute(Connection connected) {
		
	
			
		MVRelationalTuple<StreamCarsMetaData> oldTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getRight();
		MVRelationalTuple<StreamCarsMetaData> newTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getLeft();
			
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
			
		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
			
		double[][] gain = oldTuple.getMetadata().getGain();		
		
		double[][] result;
		
		RealMatrix covarianceOldMatrix = new RealMatrixImpl(covarianceOld);
		RealMatrix covarianceNewMatrix = new RealMatrixImpl(covarianceNew);
		RealMatrix gainMatrix = new RealMatrixImpl(gain);
		RealMatrix identityMatrixOfGain = new RealMatrixImpl(makeIdentityMatrix(gainMatrix.getData()));
		
		// (I-K)Pk(I-K)^t + KRK^t
		RealMatrix temp = new RealMatrixImpl();
		
		// I - K
		RealMatrix term1 = new RealMatrixImpl();
		term1 = identityMatrixOfGain.subtract(gainMatrix);
		
		temp = term1.multiply(covarianceOldMatrix);
		
		temp = temp.multiply(term1.transpose());
		
		RealMatrix term2 = new RealMatrixImpl();
		
		// KRK^T
		term2 = gainMatrix.multiply(covarianceNewMatrix);
		
		term2 = term2.multiply(gainMatrix.transpose());
		
		temp = temp.add(term2);
		
		result = temp.getData();
		
		//set new state covariance
		((MetaAttributeContainer<StreamCarsMetaData>) connected.getRight()).getMetadata().setCovariance(result);
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

	@Override
	public AbstractMetaDataUpdateFunction clone() {
		
		return new KalmanCorrectStateCovarianceFunction(this);
	}

}
