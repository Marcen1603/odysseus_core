/*
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public class LinearRegressionMergePO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	private final int[] explanatoryAttributePos;
	private final int[] dependentAttributePos;
	private final int regressionCoefficientsPos;

	/**
	 * 
	 * @param inputSchema
	 * @param dependentList
	 * @param explanatoryList
	 * @param regressionCoefficientsPos
	 */
	public LinearRegressionMergePO(final SDFSchema inputSchema, final int[] dependentList, final int[] explanatoryList, final int regressionCoefficientsPos) {
		super();
		this.explanatoryAttributePos = explanatoryList;
		this.dependentAttributePos = dependentList;
		this.regressionCoefficientsPos = regressionCoefficientsPos;
	}

	/**
	 * 
	 * @param linearRegressionMergePO
	 */
	public LinearRegressionMergePO(final LinearRegressionMergePO<T> linearRegressionMergePO) {
		super(linearRegressionMergePO);
		this.explanatoryAttributePos = linearRegressionMergePO.explanatoryAttributePos.clone();
		this.dependentAttributePos = linearRegressionMergePO.dependentAttributePos.clone();
		this.regressionCoefficientsPos = linearRegressionMergePO.regressionCoefficientsPos;
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(final ProbabilisticTuple<T> object, final int port) {
		final int currentMixturePos = ((ProbabilisticContinuousDouble) object.getAttribute(this.dependentAttributePos[0])).getDistribution();
		final NormalDistributionMixture currentMixture = object.getDistribution(currentMixturePos);

		final int residualIndex = ((ProbabilisticContinuousDouble) object.getAttribute(this.explanatoryAttributePos[0])).getDistribution();
		final RealMatrix residual = this.getResidual(object, residualIndex);
		final RealMatrix regressionCoefficients = MatrixUtils.createRealMatrix((double[][]) object.getAttribute(this.regressionCoefficientsPos));

		final Map<NormalDistribution, Double> newMixtureComponents = new HashMap<NormalDistribution, Double>();
		for (final Entry<NormalDistribution, Double> mixture : currentMixture.getMixtures().entrySet()) {

			RealMatrix mean = MatrixUtils.createColumnRealMatrix(mixture.getKey().getMean());
			final RealMatrix covarianceMatrix = CovarianceMatrixUtils.toMatrix(mixture.getKey().getCovarianceMatrix());

			// Create the new \mu = (\mu, \mu \beta)
			final double[] newMean = new double[mean.getColumnDimension() + regressionCoefficients.getColumnDimension()];
			System.arraycopy(mean.getData()[0], 0, newMean, 0, mean.getColumnDimension());
			mean = mean.multiply(regressionCoefficients);
			System.arraycopy(mean.getData()[0], 0, newMean, mean.getColumnDimension(), mean.getColumnDimension());

			final RealMatrix newCovarianceMatrix = MatrixUtils.createRealMatrix(covarianceMatrix.getRowDimension() + residual.getRowDimension(), covarianceMatrix.getColumnDimension() + residual.getColumnDimension());

			//
			// ( \sigma_A | \sigma_A \beta)
			// ( \beta^T \sigma_A | \beta^T \sigma_A \beta + \sigma)
			//
			newCovarianceMatrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);
			newCovarianceMatrix.setSubMatrix(covarianceMatrix.multiply(regressionCoefficients).getData(), 0, covarianceMatrix.getColumnDimension());
			newCovarianceMatrix.setSubMatrix(regressionCoefficients.transpose().multiply(covarianceMatrix).getData(), covarianceMatrix.getRowDimension(), 0);
			newCovarianceMatrix.setSubMatrix(regressionCoefficients.transpose().multiply(covarianceMatrix).multiply(regressionCoefficients).add(residual).getData(), covarianceMatrix.getRowDimension(), covarianceMatrix.getColumnDimension());
			newMixtureComponents.put(new NormalDistribution(newMean, CovarianceMatrixUtils.fromMatrix(newCovarianceMatrix)), mixture.getValue());
		}

		// Create the new mixture pointing to all attributes (dependent first,
		// then explanatory)
		final NormalDistributionMixture newMixture = new NormalDistributionMixture(newMixtureComponents);
		System.arraycopy(this.dependentAttributePos, 0, newMixture.getAttributes(), 0, this.dependentAttributePos.length);
		System.arraycopy(this.explanatoryAttributePos, 0, newMixture.getAttributes(), this.dependentAttributePos.length, this.explanatoryAttributePos.length);

		// Replace the old distribution with the new one
		final List<NormalDistributionMixture> distributions = new LinkedList<NormalDistributionMixture>(Arrays.asList(object.getDistributions()));
		distributions.set(currentMixturePos, newMixture);

		for (final int explanatoryAttributePo : this.explanatoryAttributePos) {
			object.setAttribute(explanatoryAttributePo, new ProbabilisticContinuousDouble(currentMixturePos));
		}
		distributions.remove(residualIndex);
		for (int i = residualIndex; i < distributions.size(); i++) {
			for (final int j : distributions.get(i).getAttributes()) {
				((ProbabilisticContinuousDouble) object.getAttribute(j)).setDistribution(i);
			}
		}
		object.setDistributions(distributions.toArray(new NormalDistributionMixture[distributions.size()]));
		object.setMetadata((T) object.getMetadata().clone());
		this.transfer(object);
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone()
	 */
	@Override
	public AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
		return new LinearRegressionMergePO<T>(this);
	}

	/**
	 * Gets the residual matrix at the given index from the given probabilistic tuple.
	 * 
	 * @param object
	 *            The probabilistic tuple
	 * @param residualIndex
	 *            The index of the residual
	 * @return The residual matrix
	 */
	private RealMatrix getResidual(final ProbabilisticTuple<T> object, final int residualIndex) {
		final NormalDistributionMixture mixture = object.getDistribution(residualIndex);
		for (final Entry<NormalDistribution, Double> entry : mixture.getMixtures().entrySet()) {
			return CovarianceMatrixUtils.toMatrix(entry.getKey().getCovarianceMatrix());
		}
		return null;
	}
}
