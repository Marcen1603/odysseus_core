package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;


public class KalmanCorrectStateEstimateFunction extends AbstractDataUpdateFunction<StreamCarsMetaData> {
		

		
	public KalmanCorrectStateEstimateFunction() {
		
	}
		
	public KalmanCorrectStateEstimateFunction(KalmanCorrectStateEstimateFunction copy) {
		super(copy);
		this.setParameters(new HashMap<Integer, Object>(copy.getParameters()));	
			
	}
		
	public KalmanCorrectStateEstimateFunction(HashMap<Integer, Object> parameters) {
			
		this.setParameters(parameters);
	}

		
	@Override
	public void compute(Connection connected, MVRelationalTuple<StreamCarsMetaData> tuple, SchemaIndexPath oldObjPath, SchemaIndexPath newObjPath) {
		
		int[] oldTuplePath = connected.getRightPath();
		MVRelationalTuple<StreamCarsMetaData> oldTuple = (MVRelationalTuple<StreamCarsMetaData>)TupleIndexPath.fromIntArray(oldTuplePath, tuple, oldObjPath).getTupleObject();
		
		int[] newTuplePath = connected.getRightPath();
		MVRelationalTuple<StreamCarsMetaData> newTuple = (MVRelationalTuple<StreamCarsMetaData>)TupleIndexPath.fromIntArray(newTuplePath, tuple, newObjPath).getTupleObject();
		
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
		return new KalmanCorrectStateEstimateFunction(this);
	}
}
