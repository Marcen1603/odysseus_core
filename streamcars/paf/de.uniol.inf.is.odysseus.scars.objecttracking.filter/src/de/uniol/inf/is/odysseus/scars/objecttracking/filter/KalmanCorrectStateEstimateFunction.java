package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;


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
	public void compute(TupleIndexPath scannedObjectTupleIndex,
			TupleIndexPath predictedObjectTupleIndex) {
	
		MVRelationalTuple<M> oldTuple = (MVRelationalTuple<M>) predictedObjectTupleIndex.getTupleObject();
		double[][] gain = oldTuple.getMetadata().getGain();
		
		MVRelationalTuple<M> newTuple = (MVRelationalTuple<M>) scannedObjectTupleIndex.getTupleObject();
		
		
		
		double[] measurementOld = getMeasurementValues(oldTuple, predictedObjectTupleIndex) ;
		double[] measurementNew = getMeasurementValues(newTuple, scannedObjectTupleIndex) ;
		
		
		
		double[] result;
		
		RealMatrix measurementOldMatrix = new RealMatrixImpl(measurementOld);
		RealMatrix measurementNewMatrix = new RealMatrixImpl(measurementNew);
		RealMatrix gainMatrix = new RealMatrixImpl(gain);	
		
		RealMatrix temp = new RealMatrixImpl();
		
		temp =  measurementNewMatrix.subtract(measurementOldMatrix);
		temp =  gainMatrix.multiply(temp);
		temp =  measurementOldMatrix.add(temp);
		
		result = temp.getColumn(0);
		
		
		setMeasurementValues(oldTuple, predictedObjectTupleIndex, result);
	
	}

	@Override
	public AbstractDataUpdateFunction<M> clone() {
		return new KalmanCorrectStateEstimateFunction<M>(this);
	}
}
