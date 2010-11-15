package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;

public class KalmanCorrectStateCovarianceFunction<K extends IProbability & IConnectionContainer & IGain> extends AbstractMetaDataUpdateFunction<K> {

	public KalmanCorrectStateCovarianceFunction() {
		super();
	}

	public KalmanCorrectStateCovarianceFunction(KalmanCorrectStateCovarianceFunction<K> copy) {

		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));

	}

	public KalmanCorrectStateCovarianceFunction(HashMap<Enum<Parameters>, Object> parameters) {
		this.setParameters(parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * This method computes the new state covariance
	 */
	public void compute(IConnection connected, MVRelationalTuple<K> tuple, HashMap<Enum<Parameters>, Object> parameters) {

//		TupleHelper tHelper = new TupleHelper(tuple);
		MVRelationalTuple<K> oldTuple = (MVRelationalTuple<K>) connected.getRightPath().getTupleObject();
		MVRelationalTuple<K> newTuple = (MVRelationalTuple<K>) connected.getLeftPath().getTupleObject();

		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();

		double[][] covarianceNew = newTuple.getMetadata().getCovariance();

		double[][] gain = null;

		// System.out.println(this.toString() + " Gain: " +
		// oldTuple.getMetadata().getGain());

		// check if there is a gain in the parameters
		if (parameters != null && !parameters.containsKey(Parameters.Gain)) {
			gain = (double[][]) parameters.get(Parameters.Gain);
		} else {
			gain = oldTuple.getMetadata().getGain();
		}

		double[][] result;

		RealMatrix covarianceOldMatrix = new RealMatrixImpl(covarianceOld);
		RealMatrix covarianceNewMatrix = new RealMatrixImpl(covarianceNew);
		RealMatrix gainMatrix = new RealMatrixImpl(gain);
		RealMatrix identityMatrixOfGain = new RealMatrixImpl(makeIdentityMatrix(gainMatrix.getData()));

		// (I-K)Pk(I-K)^t + KRK^t
		RealMatrix temp = new RealMatrixImpl();

		// I - K
		RealMatrix term1 = new RealMatrixImpl();
		term1 = identityMatrixOfGain.subtract(gainMatrix);

		temp = term1.multiply(covarianceOldMatrix);

		temp = temp.multiply(term1.transpose());

		RealMatrix term2 = new RealMatrixImpl();

		// KRK^T
		term2 = gainMatrix.multiply(covarianceNewMatrix);

		term2 = term2.multiply(gainMatrix.transpose());

		temp = temp.add(term2);

		result = temp.getData();

		// set new state covariance
		oldTuple.getMetadata().setCovariance(result);

		// System.out.println(this.toString() + " Gain: " +
		// oldTuple.getMetadata().getGain());

	}

	public static double[][] makeIdentityMatrix(double[][] template) {
		double[][] identityMatrix = new double[template.length][template.length];
		for (int i = 0; i < template.length; i++) {
			for (int j = 0; j < template.length; j++) {
				if (i == j) {
					identityMatrix[i][j] = 1;
				} else if (i != j) {
					identityMatrix[i][j] = 0;
				}
			}
		}
		return identityMatrix;
	}

	@Override
	public AbstractMetaDataUpdateFunction<K> clone() {

		return new KalmanCorrectStateCovarianceFunction<K>(this);
	}

}
