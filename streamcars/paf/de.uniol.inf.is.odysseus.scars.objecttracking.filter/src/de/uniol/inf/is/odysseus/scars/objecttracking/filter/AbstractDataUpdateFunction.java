/**
 * Interface for the implementation of a filter function,
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * @author dtwumasi
 *
 */
public abstract class AbstractDataUpdateFunction<M extends IProbability & IConnectionContainer> {
	
	private HashMap<Enum, Object> parameters;
	
	public AbstractDataUpdateFunction() {
		parameters = new HashMap<Enum, Object>();
	}
	
	public AbstractDataUpdateFunction(HashMap<Enum,Object> parameters ) {
		this.setParameters(parameters);
	}
	
	public AbstractDataUpdateFunction(AbstractDataUpdateFunction<M> copy ) {
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
	}
	
	public abstract AbstractDataUpdateFunction<M> clone();
		
	
	/**
	 * this method executes the function
	 * 
	 * @return Object the result of the computation
	 */
	public abstract void compute(TupleIndexPath scannedObjectTupleIndex,
			TupleIndexPath predictedObjectTupleIndex, HashMap<Enum, Object> parameters);

	public double[] getMeasurementValues(MVRelationalTuple<M> tuple, TupleIndexPath tupleIndexPath) {
	    
		ArrayList<Double> values = new ArrayList<Double>();
	    
		TupleHelper tHelper = new TupleHelper(tuple);
	
		
	    for( TupleInfo info :  new TupleIterator(tuple, tupleIndexPath) ) {
	      if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
	        values.add(new Double(info.tupleObject.toString()));
	      }
	    }

	    double[] result = new double[values.size()];
	    for (int i = 0; i < values.size(); i++) {
	      result[i] = values.get(i);
	    }

	    return result;
	  }
	
	public double[] getMeasurementValues(MVRelationalTuple<M> tuple, SchemaIndexPath path) {
		    ArrayList<Double> values = new ArrayList<Double>();
		    for( TupleInfo info :  new TupleIterator(tuple, path) ) {
		      if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
		        values.add(new Double(info.tupleObject.toString()));
		      }
		    }

		    double[] result = new double[values.size()];
		    for (int i = 0; i < values.size(); i++) {
		      result[i] = values.get(i);
		    }

		    return result;
		  }
	
	public void setMeasurementValues(MVRelationalTuple<M> tuple, TupleIndexPath scannedObjectTupleIndex, double[] result) {
		int i=0;
		for( TupleInfo info :  new TupleIterator(tuple, scannedObjectTupleIndex) ) {
			 if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
			   info.tupleIndexPath.setTupleObject(result[i]);
			   i+=1;
			 }
	
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<MVRelationalTuple<M>> getObjectList(MVRelationalTuple<M> mvRelationalTuple) {
	    ArrayList<MVRelationalTuple<M>> objects = new ArrayList<MVRelationalTuple<M>>();
	    for (Object attribute : mvRelationalTuple.getAttributes()) {
	      if (attribute instanceof MVRelationalTuple<?>) {
	        objects.add((MVRelationalTuple<M>) attribute);
	      }
	    }
	    return objects;
	  }
	
	/**
	 * @param parameters the parameters needed for computation
	 */
	public void setParameters(HashMap<Enum, Object> parameters) { 
	this.parameters = parameters;
	}
	
	/**
	 * @return the parameters
	 */
	public HashMap<Enum, Object> getParameters() {
		
		return parameters;
	} 

	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(Enum key, Object value) {
		this.parameters.put(key, value);
	}

	
	
	  

}
