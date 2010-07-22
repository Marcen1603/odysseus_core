package de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IRating;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Rating;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

public abstract class AbstractEvaluationPO<M extends IRating & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>>
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private int[] associationObjListPath;
	private int[] filteringObjListPath;
	private int[] brokerObjListPath;

	public int[] getAssociationObjListPath() {
		return associationObjListPath;
	}

	public void setAssociationObjListPath(int[] associationObjListPath) {
		this.associationObjListPath = associationObjListPath;
	}

	public int[] getFilteringObjListPath() {
		return filteringObjListPath;
	}

	public void setFilteringObjListPath(int[] filteringObjListPath) {
		this.filteringObjListPath = filteringObjListPath;
	}

	public int[] getBrokerObjListPath() {
		return brokerObjListPath;
	}

	public void setBrokerObjListPath(int[] brokerObjListPath) {
		this.brokerObjListPath = brokerObjListPath;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub

	}

	/*
	 * Port 0: Objekte aus der Assoziation 
	 * Port 1: Objekte aus der Filterung
	 * Port 2: Objekte aus dem temporaeren Broker
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		MVRelationalTuple<M>[] associationObjList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>) OrAttributeResolver
				.resolveTuple(object, this.associationObjListPath))
				.getAttributes();
		MVRelationalTuple<M>[] filteringObjList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>) OrAttributeResolver
				.resolveTuple(object, this.filteringObjListPath))
				.getAttributes();
		MVRelationalTuple<M>[] brokerObjList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>) OrAttributeResolver
				.resolveTuple(object, this.brokerObjListPath)).getAttributes();
		if (associationObjList != null && filteringObjList != null
				&& brokerObjList != null) {
			IRating[] associationObjRatings = new Rating[associationObjList.length];
			IRating[] filteringObjRatings = new Rating[filteringObjList.length];
			IRating[] brokerObjRatings = new Rating[brokerObjList.length];

			for (int i = 0; i < associationObjRatings.length; i++) {
				associationObjRatings[i] = associationObjList[i].getMetadata()
						.getRatingObject();
			}

			for (int i = 0; i < filteringObjRatings.length; i++) {
				filteringObjRatings[i] = filteringObjList[i].getMetadata()
						.getRatingObject();
			}

			for (int i = 0; i < brokerObjRatings.length; i++) {
				brokerObjRatings[i] = brokerObjList[i].getMetadata()
						.getRatingObject();
			}

			for (IRating rating : associationObjRatings) {
				evaluate(rating, 0);
			}

			for (IRating rating : filteringObjRatings) {
				evaluate(rating, 1);
			}

			for (IRating rating : brokerObjRatings) {
				evaluate(rating, 2);
			}
			
			Object[] combinedListChild = new Object[associationObjList.length + filteringObjList.length + brokerObjList.length];
			for (int i = 0; i < associationObjList.length; i++) {
				combinedListChild[i] = associationObjList[i];
			}
			
			for (int i = associationObjList.length; i < associationObjList.length + filteringObjList.length; i++) {
				combinedListChild[i] = filteringObjList[i-associationObjList.length];
			}
			
			for (int i = associationObjList.length + filteringObjList.length; i < associationObjList.length + filteringObjList.length + brokerObjList.length; i++) {
				combinedListChild[i] = associationObjList[i-(associationObjList.length + filteringObjList.length)];
			}
			
			Object[] combinedListFather = new Object[1];
			combinedListFather[0] = combinedListChild;
			transfer(new MVRelationalTuple<M>(combinedListFather));
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public abstract AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone();

	public abstract void evaluate(IRating rating, int port);

}
