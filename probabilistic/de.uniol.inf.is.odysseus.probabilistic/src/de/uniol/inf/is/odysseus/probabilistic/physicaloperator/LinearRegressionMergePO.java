package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public class LinearRegressionMergePO<T extends ITimeInterval> extends
		AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	private int[] explanatoryAttributePos;
	private int[] dependentAttributePos;
	private int regressionCoefficientsPos;

	public LinearRegressionMergePO(SDFSchema inputSchema, int[] dependentList,
			int[] explanatoryList, int regressionCoefficientsPos) {
		this.explanatoryAttributePos = explanatoryList;
		this.dependentAttributePos = dependentList;
		this.regressionCoefficientsPos = regressionCoefficientsPos;
	}

	public LinearRegressionMergePO(
			LinearRegressionMergePO<T> linearRegressionMergePO) {
		super(linearRegressionMergePO);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(ProbabilisticTuple<T> object, int port) {

		int currentMixturePos = ((ProbabilisticContinuousDouble) object
				.getAttribute(dependentAttributePos[0])).getDistribution();
		NormalDistributionMixture currentMixture = object
				.getDistribution(currentMixturePos);

		int residualIndex = ((ProbabilisticContinuousDouble) object
				.getAttribute(explanatoryAttributePos[0])).getDistribution();

		RealMatrix residual = getResidual(object, residualIndex);
		RealMatrix regressionCoefficients = MatrixUtils
				.createRealMatrix((double[][]) object
						.getAttribute(regressionCoefficientsPos));

		Map<NormalDistribution, Double> newMixtureComponents = new HashMap<NormalDistribution, Double>();
		for (Entry<NormalDistribution, Double> mixture : currentMixture
				.getMixtures().entrySet()) {

			RealMatrix mean = MatrixUtils.createColumnRealMatrix(mixture
					.getKey().getMean());
			RealMatrix covarianceMatrix = CovarianceMatrixUtils
					.toMatrix(mixture.getKey().getCovarianceMatrix());

			
			// Create the new \mu = (\mu, \mu \beta)
			double[] newMean = new double[mean.getColumnDimension()
					+ regressionCoefficients.getColumnDimension()];
			System.arraycopy(mean.getData()[0], 0, newMean, 0,
					mean.getColumnDimension());
			mean = mean.multiply(regressionCoefficients);
			System.arraycopy(mean.getData()[0], 0, newMean,
					mean.getColumnDimension(), mean.getColumnDimension());

			RealMatrix newCovarianceMatrix = MatrixUtils.createRealMatrix(
					covarianceMatrix.getRowDimension()
							+ residual.getRowDimension(),
					covarianceMatrix.getColumnDimension()
							+ residual.getColumnDimension());

			//
			// ( \sigma_A | \sigma_A \beta)
			// ( \beta^T \sigma_A | \beta^T \sigma_A \beta + \sigma)
			//
			newCovarianceMatrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);

			newCovarianceMatrix
					.setSubMatrix(
							covarianceMatrix.multiply(regressionCoefficients)
									.getData(), 0, covarianceMatrix
									.getColumnDimension());

			newCovarianceMatrix.setSubMatrix(regressionCoefficients.transpose()
					.multiply(covarianceMatrix).getData(),
					covarianceMatrix.getRowDimension(), 0);

			newCovarianceMatrix.setSubMatrix(regressionCoefficients.transpose()
					.multiply(covarianceMatrix)
					.multiply(regressionCoefficients).add(residual).getData(),
					covarianceMatrix.getRowDimension(),
					covarianceMatrix.getColumnDimension());

			newMixtureComponents.put(new NormalDistribution(newMean,
					CovarianceMatrixUtils.fromMatrix(newCovarianceMatrix)),
					mixture.getValue());
		}

		// Create the new mixture pointing to all attributes (dependent first,
		// then explanatory)
		NormalDistributionMixture newMixture = new NormalDistributionMixture(
				newMixtureComponents);
		System.arraycopy(dependentAttributePos, 0, newMixture.getAttributes(),
				0, dependentAttributePos.length);
		System.arraycopy(explanatoryAttributePos, 0,
				newMixture.getAttributes(), dependentAttributePos.length,
				explanatoryAttributePos.length);

		// Replace the old distribution with the new one
		object.setDistribution(currentMixturePos,
				new NormalDistributionMixture(newMixtureComponents));
		for (int i = 0; i < explanatoryAttributePos.length; i++) {
			object.setAttribute(explanatoryAttributePos[i],
					new ProbabilisticContinuousDouble(currentMixturePos));
		}
	}

	@Override
	public AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
		return new LinearRegressionMergePO<T>(this);
	}

	private RealMatrix getResidual(ProbabilisticTuple<T> object,
			int residualIndex) {
		NormalDistributionMixture mixture = object
				.getDistribution(residualIndex);
		for (Entry<NormalDistribution, Double> entry : mixture.getMixtures()
				.entrySet()) {
			return CovarianceMatrixUtils.toMatrix(entry.getKey()
					.getCovarianceMatrix());
		}
		return null;
	}
}
