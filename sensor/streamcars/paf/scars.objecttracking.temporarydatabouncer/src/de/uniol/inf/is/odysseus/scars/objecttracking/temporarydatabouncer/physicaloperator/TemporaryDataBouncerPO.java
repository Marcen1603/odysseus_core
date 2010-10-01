package de.uniol.inf.is.odysseus.scars.objecttracking.temporarydatabouncer.physicaloperator;


import java.util.ArrayList;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;

public class TemporaryDataBouncerPO<M extends IProbability & ITimeInterval> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String objListPath;
	private double threshold;

	private static final String LESS = "LESS";
	private static final String LESS_EQUAL = "LESSEQUAL";
	private static final String GREATER = "GREATER";
	private static final String GREATER_EQUAL = "GREATEREQUAL";
	private static final String EQUAL = "EQUAL";

	private String operator = LESS;

	public TemporaryDataBouncerPO() {
		super();
	}

	public TemporaryDataBouncerPO(TemporaryDataBouncerPO<M> clone) {
		super(clone);
		this.objListPath = clone.objListPath;
		this.threshold = clone.threshold;
		this.operator = clone.operator;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		if( !this.operator.toUpperCase().equals(LESS))
			sendPunctuation(timestamp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> obj, int port) {
		MVRelationalTuple<M> object = obj.clone();

		SchemaHelper sh = new SchemaHelper(getSubscribedToSource(0).getSchema());
		// Get the list of cars
		MVRelationalTuple<M> carListTuple = (MVRelationalTuple<M>) sh.getSchemaIndexPath(this.objListPath).toTupleIndexPath(object).getTupleObject();
		// Init an arraylist for the elements that should be transfered
		ArrayList<MVRelationalTuple<M>> transferCarListArrayList = new ArrayList<MVRelationalTuple<M>>();

		PointInTime timestamp = object.getMetadata().getStart();

		double val = 0;
		for(Object carObject : carListTuple.getAttributes()) {
			MVRelationalTuple<M> car = (MVRelationalTuple<M>) carObject;
			val = 0;
			double[][] cov = car.getMetadata().getCovariance();
			for(int i = 0; i < cov.length; i++) {
				val += cov[i][i];
			}
			if (this.operator.toUpperCase().equals(LESS)) {
				if(val < this.threshold) {
					transferCarListArrayList.add(car);
				}
			} else if (this.operator.toUpperCase().equals(LESS_EQUAL)) {
				if(val <= this.threshold) {
					transferCarListArrayList.add(car);
				}
			} else if (this.operator.toUpperCase().equals(GREATER)) {
				if(val > this.threshold) {
					transferCarListArrayList.add(car);
				}
			} else if (this.operator.toUpperCase().equals(GREATER_EQUAL)) {
				if(val >= this.threshold) {
					transferCarListArrayList.add(car);
				}
			} else if (this.operator.toUpperCase().equals(EQUAL)) {
				if(val == this.threshold) {
					transferCarListArrayList.add(car);
				}
			}
		}

		MVRelationalTuple<M> tuples = new MVRelationalTuple<M>(transferCarListArrayList.size());
		int counter = 0;
		for (MVRelationalTuple<M> mvRelationalTuple : transferCarListArrayList) {
			tuples.setAttribute(counter++, mvRelationalTuple);
		}

		SchemaIndexPath schemaPath = sh.getSchemaIndexPath(this.objListPath);
		TupleIndexPath tuplePath = schemaPath.toTupleIndexPath(object);
		tuplePath.setTupleObject(tuples);

		// Falls NICHTS weitergeleitet wird -> punctuation senden
		if(transferCarListArrayList.size() == 0) {
			sendPunctuation(timestamp);
		} else {
			transfer(object);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new TemporaryDataBouncerPO<M>(this);
	}

	public String getObjListPath() {
		return objListPath;
	}

	public void setObjListPath(String objListPath) {
		this.objListPath = objListPath;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
