package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.FilterAO;
import de.uniol.inf.is.odysseus.filtering.FilterPO;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.IGainFunction;
import de.uniol.inf.is.odysseus.filtering.logicaloperator.GainFunctionAO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class GainFunctionPO <M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<N>, MVRelationalTuple<N>, Double> , 
						N extends IProbability>
							extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private IGainFunction gainFunction;

	private SDFAttributeList schema;
	
	// path to new and old objects
	private int[] oldObjListPath;
	private int[] newObjListPath;
	
	public GainFunctionPO() {
		super();
		}
	
	public GainFunctionPO(GainFunctionAO gainfunctionAO) {
		this.gainFunction=gainfunctionAO.getGainFunction();
		
	}
	
	public GainFunctionPO(GainFunctionPO copy) {
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
		
		// list of connections
		Connection<MVRelationalTuple<N>, MVRelationalTuple<N>, Double>[] objConList = (Connection<MVRelationalTuple<N>, MVRelationalTuple<N>, Double>[]) object.getMetadata().getConnectionList().toArray();
		
		double[][] gain = null;
		
		// traverse connection list and filter
		for(Connection<MVRelationalTuple<N>, MVRelationalTuple<N>, Double> connected : objConList ) {
			
			MVRelationalTuple<N> oldTuple = connected.getRight();
			MVRelationalTuple<N> newTuple = connected.getLeft();
			
			double[][] covarianceNew = newTuple.getMetadata().getCovariance();
			double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
			
			// compute gain
			gainFunction.addParameter(HashConstants.OLD_COVARIANCE, covarianceOld);
			gainFunction.addParameter(HashConstants.NEW_COVARIANCE, covarianceNew);
			
			gain = gainFunction.computeGain();
			
			//set gain
			//connected.setGain(gain);
		}
			
		// transfer to broker
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}
	
	public void setGainFunction(IGainFunction gainFunction) {
		this.gainFunction = gainFunction;
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




}
