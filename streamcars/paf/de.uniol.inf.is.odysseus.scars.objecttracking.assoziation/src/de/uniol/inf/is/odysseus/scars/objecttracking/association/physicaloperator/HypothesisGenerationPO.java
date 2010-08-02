package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
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
public class HypothesisGenerationPO<M extends IProbability & IConnectionContainer> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private MVRelationalTuple<M> oldList;
	private MVRelationalTuple<M> newList;

	private int[] oldObjListPath;
	private int[] newObjListPath;

	public HypothesisGenerationPO() {

	}

	public HypothesisGenerationPO(HypothesisGenerationPO<M> copy) {
		super(copy);

		this.oldList = copy.getOldList();
		this.newList = copy.getNewList();
		this.oldObjListPath = copy.getOldObjListPath();
		this.newObjListPath = copy.getNewObjListPath();
	}

	public MVRelationalTuple<M> getOldList() {
		return oldList;
	}

	public void setOldList(MVRelationalTuple<M> oldList) {
		this.oldList = oldList;
	}

	public MVRelationalTuple<M> getNewList() {
		return newList;
	}

	public void setNewList(MVRelationalTuple<M> newList) {
		this.newList = newList;
	}

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
				/*
				 *  WICHTIG: Im sekundären Zyklus muss das, was aus dem primären Zyklus
				 *  aus der HypothesisSelection in diesen Operator kommt auf Port 1
				 *  ankommen!!!!!!!!!
				 *
				 */
				case 0: this.oldList = object;
				case 1: this.newList = object;
			}
		} else {
			Object[] oldListAttributes = oldList.getAttributes();
			ArrayList<Object> oldNewListAttributes = new ArrayList<Object>();
			oldNewListAttributes.add(oldListAttributes);
			oldNewListAttributes.add(OrAttributeResolver.resolveTuple(this.newList, this.newObjListPath));
			this.oldList = null;
			this.newList = null;
			transfer(new MVRelationalTuple<M>(oldNewListAttributes.toArray()));
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisGenerationPO<M>(this);
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

	public void testProcessNext(MVRelationalTuple<M> object, int port) {
		process_next(object, port);
	}

}
