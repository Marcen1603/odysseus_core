package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.filtering.IFilterFunction;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public abstract class AbstractFilterPO<M extends IProbability & IConnectionContainer>
							extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private IFilterFunction filterFunction;

	private SDFAttributeList schema;
	
	

	// path to new and old objects
	private int[] oldObjListPath;
	private int[] newObjListPath;
	
	public AbstractFilterPO() {
		
	}

	
	
	
	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	protected void process_next(MVRelationalTuple<M> object, int port) {
		
		
		object = computeAll(object);
		// transfer to broker
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return the schema
	 */
	public SDFAttributeList getSchema() {
		return schema;
	}

	public void setSchema(SDFAttributeList schema) {
		this.schema = schema;
	}

	/**
	 * @param oldObjListPath the oldObjListPath to set
	 */
	public void setOldObjListPath(int[] oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	/**
	 * @param newObjListPath the newObjListPath to set
	 */
	public int[] getNewObjListPath() {
		return this.newObjListPath;
	}
	
	/**
	 * @param newObjListPath the newObjListPath to set
	 */
	public void setNewObjListPath(int[] newObjListPath) {
		this.newObjListPath = newObjListPath;
	}
	
	/**
	 * @param oldObjListPath the oldObjListPath to set
	 */
	public int[] getOldObjListPath() {
		return this.oldObjListPath;
	}


	
	
	public abstract MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object);
	
	/**
	 * 
	 * @param connected
	 */
	public abstract void compute(Connection connected, ArrayList<int[]> mesurementValuePathsTupleNew, ArrayList<int[]> mesurementValuePathsTupleOld);


	/**
	 * @return the filterFunction
	 */
	public IFilterFunction getFilterFunction() {
		return filterFunction;
	}
	
	public void setFilterFunction(IFilterFunction filterFunction) {
		this.filterFunction = filterFunction;
	}

}
