/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;

/**
 * @author dtwumasi
 * 
 */
public class KalmanGainFunction<M extends IProbability & IGain > extends AbstractMetaDataCreationFunction<M> {

	public KalmanGainFunction() {
		super();
	}

	public KalmanGainFunction(KalmanGainFunction<M> copy) {

		this.setParameters(new HashMap<Integer, Object>(copy.getParameters()));

	}

	public KalmanGainFunction(HashMap<Integer, Object> parameters) {
		this.setParameters(parameters);
	}

	@SuppressWarnings("unchecked")
	public void compute(Connection connected, MVRelationalTuple<M> tuple) {

		TupleHelper tHelper = new TupleHelper(tuple);
		MVRelationalTuple<M> oldTuple = (MVRelationalTuple<M>)tHelper.getObject(connected.getRightPath());
		MVRelationalTuple<M> newTuple = (MVRelationalTuple<M>)tHelper.getObject(connected.getLeftPath());

		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();

		RealMatrix oldCovariance = new RealMatrixImpl(covarianceOld);
		RealMatrix newCovariance = new RealMatrixImpl(covarianceNew);

		RealMatrix temp = new RealMatrixImpl();

		temp = oldCovariance.add(newCovariance);
		temp = temp.inverse();
		temp = oldCovariance.multiply(temp);

		// set gain
		oldTuple.getMetadata().setGain(temp.getData());

	}

	@Override
	public KalmanGainFunction<M> clone() {
		return new KalmanGainFunction<M>(this);
	}
}
