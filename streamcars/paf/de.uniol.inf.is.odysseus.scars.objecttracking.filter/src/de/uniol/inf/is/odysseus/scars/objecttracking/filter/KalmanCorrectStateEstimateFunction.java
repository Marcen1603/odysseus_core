package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;


public class KalmanCorrectStateEstimateFunction<M extends IProbability & IConnectionContainer & IGain> extends AbstractDataUpdateFunction<M> {
		

		
	public KalmanCorrectStateEstimateFunction() {
		
	}
		
	public KalmanCorrectStateEstimateFunction(KalmanCorrectStateEstimateFunction<M> copy) {
		super(copy);
		this.setParameters(new HashMap<Integer, Object>(copy.getParameters()));	
			
	}
		
	public KalmanCorrectStateEstimateFunction(HashMap<Integer, Object> parameters) {
			
		this.setParameters(parameters);
	}

		
	@SuppressWarnings("unchecked")
	@Override
	public void compute(Connection connected, MVRelationalTuple<M> tuple, SchemaIndexPath oldObjPath, SchemaIndexPath newObjPath) {
		
		int[] oldTuplePath = connected.getRightPath();
		MVRelationalTuple<M> oldTuple = (MVRelationalTuple<M>)TupleIndexPath.fromIntArray(oldTuplePath, tuple, oldObjPath).getTupleObject();
		
		int[] newTuplePath = connected.getRightPath();
		MVRelationalTuple<M> newTuple = (MVRelationalTuple<M>)TupleIndexPath.fromIntArray(newTuplePath, tuple, newObjPath).getTupleObject();
		
		double[] measurementOld = getMeasurementValues(oldTuple, oldObjPath) ;
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
	public AbstractDataUpdateFunction<M> clone() {
		return new KalmanCorrectStateEstimateFunction<M>(this);
	}
}
