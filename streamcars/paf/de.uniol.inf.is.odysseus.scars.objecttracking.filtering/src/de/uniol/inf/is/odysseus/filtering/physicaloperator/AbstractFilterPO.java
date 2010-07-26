package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.IFilterFunction;
import de.uniol.inf.is.odysseus.filtering.logicaloperator.FilterAO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public abstract class AbstractFilterPO <M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> >
							extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private IFilterFunction filterFunction;

	private SDFAttributeList schema;
	
	// path to new and old objects
	private int[] oldObjListPath;
	private int[] newObjListPath;
	
	public AbstractFilterPO() {
		super();
		}
	
	public AbstractFilterPO(FilterAO filterFunctionAO) {
		this.setFilterFunction(filterFunctionAO.getFilterFunction());
		
	}
	
	public AbstractFilterPO(AbstractFilterPO copy) {
		super(copy);
		}
	
	
	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public void process_next(MVRelationalTuple<M> object, int port) {
		
		
		object = computeAll(object);
		// transfer to broker
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}
	
	public void setFilterFunction(IFilterFunction filterFunction) {
		this.filterFunction = filterFunction;
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
	public void setNewObjListPath(int[] newObjListPath) {
		this.newObjListPath = newObjListPath;
	}
	
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	
	public abstract MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object);
	/**
	 * 
	 * @param connected
	 */
	public abstract void compute(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected);

	/**
	 * @return the filterFunction
	 */
	public IFilterFunction getFilterFunction() {
		return filterFunction;
	}
	

}
