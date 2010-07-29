package de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

public class EvaluationPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer>
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private int[] associationObjListPath;
	private int[] filteringObjListPath;
	private int[] brokerObjListPath;

	private double threshold;
	
	public EvaluationPO() {
		
	}
	
	public EvaluationPO(EvaluationPO<M> copy) {
		this.associationObjListPath = copy.getAssociationObjListPath();
		this.filteringObjListPath = copy.getFilteringObjListPath();
		this.brokerObjListPath = copy.getBrokerObjListPath();
		this.threshold = copy.getThreshold();
	}

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
		Object[] combinedListFather = new Object[1];
		ArrayList<Object> combinedListChildTmp = new ArrayList<Object>();

		MVRelationalTuple<M>[] associationObjList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>) OrAttributeResolver
				.resolveTuple(object, this.associationObjListPath))
				.getAttributes();
		MVRelationalTuple<M>[] filteringObjList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>) OrAttributeResolver
				.resolveTuple(object, this.filteringObjListPath))
				.getAttributes();
		MVRelationalTuple<M>[] brokerObjList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>) OrAttributeResolver
				.resolveTuple(object, this.brokerObjListPath)).getAttributes();

		double val = 0;

		if(associationObjList != null) {
			for(MVRelationalTuple<M> tuple : associationObjList) {
				val = 0;
				double[][] cov = tuple.getMetadata().getCovariance();
				for(int i = 0; i < cov.length; i++) {
					val += cov[i][i];
				}
				if(val < this.threshold) {
					combinedListChildTmp.add(tuple);
				}
			}
		}

		if(filteringObjList != null) {
			for(MVRelationalTuple<M> tuple : filteringObjList) {
				val = 0;
				double[][] cov = tuple.getMetadata().getCovariance();
				for(int i = 0; i < cov.length; i++) {
					val += cov[i][i];
				}
				if(val < this.threshold) {
					combinedListChildTmp.add(tuple);
				}
			}
		}

		if(brokerObjList != null) {
			for(MVRelationalTuple<M> tuple : brokerObjList) {
				val = 0;
				double[][] cov = tuple.getMetadata().getCovariance();
				for(int i = 0; i < cov.length; i++) {
					val += cov[i][i];
				}
				if(val < this.threshold) {
					combinedListChildTmp.add(tuple);
				}
			}
		}

		Object[] combinedListChild = combinedListChildTmp.toArray();
		combinedListFather[0] = combinedListChild;
		transfer(new MVRelationalTuple<M>(combinedListFather));
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getThreshold() {
		return threshold;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
