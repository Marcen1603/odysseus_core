package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;

import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;

public class KalmanCorrectStateCovarianceFunction<K extends IProbability & IConnectionContainer & IGain> extends AbstractMetaDataUpdateFunction<K> {
	
	
	
	public KalmanCorrectStateCovarianceFunction() {
	super();
	}
	
	public KalmanCorrectStateCovarianceFunction(KalmanCorrectStateCovarianceFunction<K> copy) {

		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
		
	}
	
	public KalmanCorrectStateCovarianceFunction(HashMap<Enum, Object> parameters) {
		this.setParameters(parameters);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * This method computes the new state covariance
	 */
	public void compute(Connection connected, MVRelationalTuple<K> tuple, HashMap<Enum, Object> parameters) {
		
	
		TupleHelper tHelper = new TupleHelper(tuple);
		MVRelationalTuple<K> oldTuple = (MVRelationalTuple<K>)tHelper.getObject(connected.getRightPath());
		MVRelationalTuple<K> newTuple = (MVRelationalTuple<K>)tHelper.getObject(connected.getLeftPath());
			
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
			
		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		
		double[][] gain = null;
		
		// check if there is a gain in the parameters
		if (parameters != null) {
			if (parameters.containsKey(Parameters.Gain)) {
				gain = (double[][]) parameters.get(Parameters.Gain);
			}
		}  else {
			gain = oldTuple.getMetadata().getGain();
		}
		
		
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
		oldTuple.getMetadata().setCovariance(result);
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
	public AbstractMetaDataUpdateFunction<K> clone() {
		
		return new KalmanCorrectStateCovarianceFunction<K>(this);
	}

}
