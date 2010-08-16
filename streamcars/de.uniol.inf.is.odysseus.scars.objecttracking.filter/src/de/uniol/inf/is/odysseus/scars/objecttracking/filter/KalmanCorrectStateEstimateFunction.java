package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;


public class KalmanCorrectStateEstimateFunction extends AbstractDataUpdateFunction {
		

		
	public KalmanCorrectStateEstimateFunction() {
		
	}
		
	public KalmanCorrectStateEstimateFunction(KalmanCorrectStateEstimateFunction copy) {
		
		copy.setParameters(new HashMap<Integer, Object>(this.getParameters()));	
			
	}
		
	public KalmanCorrectStateEstimateFunction(HashMap<Integer, Object> parameters) {
			
		this.setParameters(parameters);
	}
		
	@Override
	public void compute(Connection connected, ArrayList<int[]> measurementValuePathsTupleNew, ArrayList<int[]> measurementValuePathsTupleOld, int i) {
		
		MVRelationalTuple<StreamCarsMetaData> oldTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getRight();
		MVRelationalTuple<StreamCarsMetaData> newTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getLeft();
	
		double[] measurementOld = OrAttributeResolver.getMeasurementValues(measurementValuePathsTupleOld, oldTuple);
		
		double[] measurementNew = OrAttributeResolver.getMeasurementValues(measurementValuePathsTupleNew, newTuple);
		
		double[][] gain = oldTuple.getMetadata().getGain();
		
		double[][] result;
		
		RealMatrix measurementOldMatrix = new RealMatrixImpl(measurementOld);
		RealMatrix measurementNewMatrix = new RealMatrixImpl(measurementNew);
		RealMatrix gainMatrix = new RealMatrixImpl(gain);	
		
		RealMatrix temp = new RealMatrixImpl();
		
		temp =  measurementNewMatrix.subtract(measurementOldMatrix);
		temp =  gainMatrix.multiply(temp);
		temp =  measurementOldMatrix.add(temp);
		
		result = temp.getData();
		
		// TODO richtig machen
	/*	for (int i=0; i<= connected.; i++) {
			oldTuple.setAttribute(measurementValuePathsTupleOld.get(i)[0], result);
		}*/
		
	
		
	}

	@Override
	public AbstractDataUpdateFunction clone() {
		
		return new KalmanCorrectStateEstimateFunction(this);
	}


	
}
