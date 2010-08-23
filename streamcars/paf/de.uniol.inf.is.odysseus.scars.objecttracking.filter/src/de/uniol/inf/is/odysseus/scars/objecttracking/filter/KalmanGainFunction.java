/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;

/**
 * @author dtwumasi
 * 
 */
public class KalmanGainFunction extends AbstractMetaDataCreationFunction {

	public KalmanGainFunction() {
		super();
	}

	public KalmanGainFunction(KalmanGainFunction copy) {

		this.setParameters(new HashMap<Integer, Object>(copy.getParameters()));

	}

	public KalmanGainFunction(HashMap<Integer, Object> parameters) {
		this.setParameters(parameters);
	}

	public void compute(Connection connected, MVRelationalTuple<StreamCarsMetaData> tuple, SchemaIndexPath pathToOldList, SchemaIndexPath pathToNewList) {

		double gain[][];

		TupleHelper tHelper = new TupleHelper(tuple);
		MVRelationalTuple<StreamCarsMetaData> oldTuple = (MVRelationalTuple<StreamCarsMetaData>)tHelper.getObject(connected.getRightPath());
		MVRelationalTuple<StreamCarsMetaData> newTuple = (MVRelationalTuple<StreamCarsMetaData>)tHelper.getObject(connected.getLeftPath());

		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();

		RealMatrix oldCovariance = new RealMatrixImpl(covarianceOld);
		RealMatrix newCovariance = new RealMatrixImpl(covarianceNew);

		RealMatrix temp = new RealMatrixImpl();

		temp = oldCovariance.add(newCovariance);
		temp = temp.inverse();
		temp = oldCovariance.multiply(temp);

		gain = temp.getData();

		// set gain
		oldTuple.getMetadata().setGain(gain);

	}

	@Override
	public AbstractMetaDataCreationFunction clone() {

		return new KalmanGainFunction(this);
	}
}
