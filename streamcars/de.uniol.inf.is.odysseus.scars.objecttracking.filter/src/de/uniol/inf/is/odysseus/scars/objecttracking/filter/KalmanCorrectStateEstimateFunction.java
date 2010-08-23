package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;


public class KalmanCorrectStateEstimateFunction<K> extends AbstractDataUpdateFunction {
		

		
	public KalmanCorrectStateEstimateFunction() {
		
	}
		
	public KalmanCorrectStateEstimateFunction(KalmanCorrectStateEstimateFunction<K> copy) {
		super(copy);
		this.setParameters(new HashMap<Integer, Object>(copy.getParameters()));	
			
	}
		
	public KalmanCorrectStateEstimateFunction(HashMap<Integer, Object> parameters) {
			
		this.setParameters(parameters);
	}
		
	@Override
	public void compute(Connection connected, ArrayList<int[]> measurementValuePathsTupleNew, ArrayList<int[]> measurementValuePathsTupleOld, int i) {
		
		MVRelationalTuple<StreamCarsMetaData<K>> oldTuple = (MVRelationalTuple<StreamCarsMetaData<K>>) connected.getRight();
		MVRelationalTuple<StreamCarsMetaData<K>> newTuple = (MVRelationalTuple<StreamCarsMetaData<K>>) connected.getLeft();
	
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
		for (int y=0; y<= result.length; y++) {
			oldTuple.setAttribute(measurementValuePathsTupleOld.get(i)[y], result[i]);
		}
		
	
		
	}

	@Override
	public AbstractDataUpdateFunction clone() {
		
		return new KalmanCorrectStateEstimateFunction<K>(this);
	}

	


	
}
