package de.uniol.inf.is.odysseus.filtering;


import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>>  {

	private IGainFunction gainFunction;

	private ICorrectStateEstimateFunction correctStateEstimateFunction;

	private ICorrectStateCovarianceFunction correctStateCovarianceFunction;

	private MVRelationalTuple<M> oldTuple;

	private MVRelationalTuple<M> newTuple;

	private int[] oldObjListPath;
	private int[] newObjListPath;

	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;


	public FilterPO() {
	super();
	}

	public FilterPO(FilterAO<M> filterAO) {
		this.gainFunction=filterAO.getGainFunction();
		this.correctStateEstimateFunction=filterAO.getCorrectStateEstimateFunction();
		this.correctStateCovarianceFunction=filterAO.getCorrectStateCovarianceFunction();
	}
	public FilterPO(FilterPO<M> copy) {
	super(copy);
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// TODO Auto-generated method stub

	// list of new objects
	MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.newObjListPath)).getAttributes();
	// list of old objects
	MVRelationalTuple<M>[] oldList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.oldObjListPath)).getAttributes();
	// list of connections
	Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[] objConList = (Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[]) object.getMetadata().getConnectionList().toArray();


	double[][] gain = null;

	// traverse connection list and filter
	for(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected : objConList ) {

		MVRelationalTuple<M> oldTuple = connected.getRight();
		MVRelationalTuple<M> newTuple = connected.getLeft();

		double[] measurementOld = newTuple.getMeasurementValues();
		double[] measurementNew = oldTuple.getMeasurementValues();

		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();

		double[] correctedMeasurement;
		double[][] correctedCovariance;


		// compute gain
		gainFunction.addParameter("oldCovariance", covarianceOld);
		gainFunction.addParameter("newCovariance", covarianceNew);

		gain = gainFunction.computeGain();

		// update state covariance
		correctStateCovarianceFunction.addParameter("gain",gain);
		correctStateCovarianceFunction.addParameter("covarianceOld", covarianceOld);

		correctedCovariance = correctStateCovarianceFunction.correctStateCovariance();

		oldTuple.getMetadata().setCovariance(correctedCovariance);

		// update state estimate
		correctStateEstimateFunction.addParameter("measurementOld", measurementOld);
		correctStateEstimateFunction.addParameter("measurementNew", measurementNew);
		correctStateEstimateFunction.addParameter("gain", gain);


		correctedMeasurement = correctStateEstimateFunction.correctStateEstimate();



	}

	// transfer to broker
	transfer(object);


	}



	public void setOldObjListPath(int[] oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public void setNewObjListPath(int[] newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public void setLeftSchema(SDFAttributeList leftSchema) {
		this.leftSchema = leftSchema;
	}

	public void setRightSchema(SDFAttributeList rightSchema) {
		this.rightSchema = rightSchema;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub

	}

	public int hashcode() {
		return 0;
	}

	public boolean equals(Object obj) {
		return false;

	}
}
