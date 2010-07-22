package de.uniol.inf.is.odysseus.assoziation.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

/**
 * The Hypothesis Generation has two inputstreams:
 * 1 - predicted objects from the prediction operator
 * 2 - the new detected objects
 * Both list now have the same timestamp. The Hypothesis Generation Operator initiates the connection list
 * in the metadata and changes the schema so that the next operator gets both lists (new and old) as input.
 * 
 * @author Volker Janz
 *
 */
public class HypothesisGenerationPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {
	
	private MVRelationalTuple<M> oldList;
	private MVRelationalTuple<M> newList;
	
	private int[] oldObjListPath;
	private int[] newObjListPath;
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * port 0 = old;
	 * port 1 = new;
	 */
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		if(this.oldList == null || this.newList == null) {
			switch(port) {
				case 0: this.oldList = object;
				case 1: this.newList = object;
			}
		} else {
			Object[] oldNewListArray = new Object[2];
			oldNewListArray[0] = OrAttributeResolver.resolveTuple(this.oldList, this.oldObjListPath);
			oldNewListArray[1] = OrAttributeResolver.resolveTuple(this.newList, this.newObjListPath);
			MVRelationalTuple<M> oldNewList = new MVRelationalTuple<M>(oldNewListArray);
			this.oldList = null;
			this.newList = null;
			transfer(oldNewList);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int[] getOldObjListPath() {
		return this.oldObjListPath;
	}

	public int[] getNewObjListPath() {
		return this.newObjListPath;
	}
	
	public void setOldObjListPath(int[] oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public void setNewObjListPath(int[] newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

}
