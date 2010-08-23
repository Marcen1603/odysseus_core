package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;


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
	public void compute(Connection connected, SchemaIndexPath newObjPath, SchemaIndexPath oldObjPath) {
		
		MVRelationalTuple<StreamCarsMetaData<K>> oldTuple = (MVRelationalTuple<StreamCarsMetaData<K>>) connected.getRight();
		MVRelationalTuple<StreamCarsMetaData<K>> newTuple = (MVRelationalTuple<StreamCarsMetaData<K>>) connected.getLeft();
	
		
		//double[] measurementOld = OrAttributeResolver.getMeasurementValues(measurementValuePathsTupleOld, oldTuple);
		
		double[] measurementOld = getMeasurementValues(oldTuple, oldObjPath) ;
		
		//double[] measurementNew = OrAttributeResolver.getMeasurementValues(measurementValuePathsTupleNew, newTuple);
		
		double[] measurementNew = getMeasurementValues(newTuple, newObjPath) ;
		
		double[][] gain = oldTuple.getMetadata().getGain();
		
		double[] result;
		
		RealMatrix measurementOldMatrix = new RealMatrixImpl(measurementOld);
		RealMatrix measurementNewMatrix = new RealMatrixImpl(measurementNew);
		RealMatrix gainMatrix = new RealMatrixImpl(gain);	
		
		RealMatrix temp = new RealMatrixImpl();
		
		temp =  measurementNewMatrix.subtract(measurementOldMatrix);
		temp =  gainMatrix.multiply(temp);
		temp =  measurementOldMatrix.add(temp);
		
		result = temp.getColumn(0);
		
		
		setMeasurementValues(oldTuple, oldObjPath, result);
		
		// TODO richtig machen
		/*for (int y=0; y<= result.length; y++) {
			oldTuple.setAttribute(measurementValuePathsTupleOld.get(i)[y], result[i]);
		}*/
		
	
		
	}

	@Override
	public AbstractDataUpdateFunction clone() {
		
		return new KalmanCorrectStateEstimateFunction<K>(this);
	}

	


	
}
