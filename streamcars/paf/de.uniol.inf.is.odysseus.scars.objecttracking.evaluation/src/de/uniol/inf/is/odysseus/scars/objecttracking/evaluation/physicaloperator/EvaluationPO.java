package de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.PointInTimeSweepArea;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;

public class EvaluationPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer & ITimeInterval>
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String associationObjListPath;
	private String filteringObjListPath;
	private String brokerObjListPath;

	private PointInTimeSweepArea<M> sweepAssociation;
	private PointInTimeSweepArea<M> sweepFiltering;
	private PointInTimeSweepArea<M> sweepBroker;

	private double threshold;

	public EvaluationPO() {
		super();
		this.sweepAssociation = new PointInTimeSweepArea<M>();
		this.sweepFiltering = new PointInTimeSweepArea<M>();
		this.sweepBroker = new PointInTimeSweepArea<M>();
	}

	public EvaluationPO(EvaluationPO<M> copy) {
		super(copy);
		this.associationObjListPath = copy.associationObjListPath;
		this.filteringObjListPath = copy.filteringObjListPath;
		this.brokerObjListPath = copy.brokerObjListPath;
		this.threshold = copy.getThreshold();
		this.sweepAssociation =  (PointInTimeSweepArea<M>) copy.sweepAssociation.clone();
		this.sweepFiltering =  (PointInTimeSweepArea<M>) copy.sweepFiltering.clone();
		this.sweepBroker =  (PointInTimeSweepArea<M>) copy.sweepBroker.clone();
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
		// Instanciate needed helper classes
		SchemaHelper shAssociationInput = new SchemaHelper(getSubscribedToSource(0).getSchema());
		SchemaHelper shFilteringInput = new SchemaHelper(getSubscribedToSource(1).getSchema());
		SchemaHelper shSecondBrokerInput = new SchemaHelper(getSubscribedToSource(2).getSchema());

		// Create temporary objects that will be filled with "correct" tuples.
		// The temporary objects will be converted to a scan object that can be transfered at the end of process_next.
		Object[] combinedListFather = new Object[1];
		ArrayList<Object> combinedListChildTmp = new ArrayList<Object>();

		// Fill the sweep areas according to the port
		switch(port) {
			case 0: this.sweepAssociation.insert(object); break;
			case 1: this.sweepFiltering.insert(object); break;
			case 2: this.sweepBroker.insert(object); break;
		}

		// Remove invalid objects from the sweep areas
		this.sweepAssociation.purgeElements(object, Order.LeftRight);
		this.sweepFiltering.purgeElements(object, Order.LeftRight);
		this.sweepBroker.purgeElements(object, Order.LeftRight);

		// Get iterators with valid objects according to the timestamp of the current object
		Iterator<MVRelationalTuple<M>> itAssociation = sweepAssociation.query(object, Order.LeftRight);
		Iterator<MVRelationalTuple<M>> itFiltering = sweepFiltering.query(object, Order.LeftRight);
		Iterator<MVRelationalTuple<M>> itBroker = sweepBroker.query(object, Order.LeftRight);

		// Check if there is a complete input -> one list from every port (so that it can be evaluated)
		if(itAssociation.hasNext() && itFiltering.hasNext() && itBroker.hasNext()) {
			// Get the three lists
			MVRelationalTuple<M> associationMainObject = itAssociation.next();
			MVRelationalTuple<M> filteringMainObject = itFiltering.next();
			MVRelationalTuple<M> brokerMainObject = itBroker.next();

			MVRelationalTuple<M> associationListObject = (MVRelationalTuple<M>) shAssociationInput.getSchemaIndexPath(this.associationObjListPath).toTupleIndexPath(associationMainObject).getTupleObject();
			MVRelationalTuple<M> filteringListObject = (MVRelationalTuple<M>) shFilteringInput.getSchemaIndexPath(this.filteringObjListPath).toTupleIndexPath(filteringMainObject).getTupleObject();
			MVRelationalTuple<M> brokerListObject = (MVRelationalTuple<M>) shSecondBrokerInput.getSchemaIndexPath(this.brokerObjListPath).toTupleIndexPath(brokerMainObject).getTupleObject();

			MVRelationalTuple<M>[] associationObjList = (MVRelationalTuple<M>[]) associationListObject.getAttributes();
			MVRelationalTuple<M>[] filteringObjList = (MVRelationalTuple<M>[]) filteringListObject.getAttributes();
			MVRelationalTuple<M>[] brokerObjList = (MVRelationalTuple<M>[]) brokerListObject.getAttributes();

			// Do the evaluation
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

			// Generate the output tuple
			Object[] combinedListChild = combinedListChildTmp.toArray();
			combinedListFather[0] = combinedListChild;

			// Transfer the generated tuple
			transfer(new MVRelationalTuple<M>(combinedListFather));
		}
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

	public void setAssociationObjListPath(String associationObjListPath) {
		this.associationObjListPath = associationObjListPath;
	}

	public void setFilteringObjListPath(String filteringObjListPath) {
		this.filteringObjListPath = filteringObjListPath;
	}

	public void setBrokerObjListPath(String brokerObjListPath) {
		this.brokerObjListPath = brokerObjListPath;
	}

}
