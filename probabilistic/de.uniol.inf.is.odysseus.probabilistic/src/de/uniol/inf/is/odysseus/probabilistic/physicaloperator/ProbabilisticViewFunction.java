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

package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Implementation of a probabilistic view to generate a distribution for an equi-join
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public class ProbabilisticViewFunction<T extends ITimeInterval> {

	private final SDFSchema schema;
	@SuppressWarnings("unused")
	private Integer[] continuousPos;
	private final Integer[] joinAttributePos;
	private Integer[] viewAttributePos;
	private final DefaultTISweepArea<ProbabilisticTuple<T>> sweepArea = new DefaultTISweepArea<ProbabilisticTuple<T>>();
	private RealMatrix[] sigmas;
	private RealMatrix[] betas;

	public ProbabilisticViewFunction(final Integer[] pos, final SDFSchema schema) {
		this.joinAttributePos = pos;
		this.schema = schema;
		this.init(this.schema);
	}

	public ProbabilisticViewFunction(final ProbabilisticViewFunction<T> probabilisticViewPO) {
		this.joinAttributePos = probabilisticViewPO.joinAttributePos;
		this.viewAttributePos = probabilisticViewPO.viewAttributePos;
		this.schema = probabilisticViewPO.schema.clone();
		this.init(this.schema);
	}

	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	protected void merge(final ProbabilisticTuple<T> left, final ProbabilisticTuple<T> right) {
		this.sweepArea.purgeElements(right, Order.LeftRight);
		this.sweepArea.insert(right);
		final int attributes = right.getAttributes().length;
		if (this.sweepArea.size() > attributes) {
			for (int i = 0; i < this.viewAttributePos.length; i++) {
				this.updateLeastSquareEstimate(this.sweepArea, this.joinAttributePos, this.viewAttributePos, i);
			}
		}

		final IMetadataMergeFunction<T> mergeFunction = new CombinedMergeFunction<T>();

		final ProbabilisticTuple<T> tuple = (ProbabilisticTuple<T>) left.merge(left, right, mergeFunction, Order.LeftRight);
		final int offset = left.getDistributions().length + right.getDistributions().length;
		final NormalDistributionMixture[] distributions = new NormalDistributionMixture[offset + this.viewAttributePos.length];

		tuple.setDistributions(distributions);
		for (int i = 0; i < left.getDistributions().length; i++) {
			tuple.setDistribution(i, left.getDistribution(i));
		}
		for (int i = 0; i < right.getDistributions().length; i++) {
			tuple.setDistribution(left.getDistributions().length + i, right.getDistribution(i));
		}
		for (int i = 0; i < this.viewAttributePos.length; i++) {
			final Map<NormalDistribution, Double> viewMixtures = new HashMap<NormalDistribution, Double>();

			for (final Integer joinAttributePo : this.joinAttributePos) {
				final NormalDistributionMixture distributionMixture = left.getDistribution(joinAttributePo);
				final Map<NormalDistribution, Double> mixtures = distributionMixture.getMixtures();
				for (final Entry<NormalDistribution, Double> mixture : mixtures.entrySet()) {
					final NormalDistribution distribution = mixture.getKey();
					final NormalDistribution viewDistribution = this.updateDistributions(distribution, i);
					viewMixtures.put(viewDistribution, mixture.getValue());
				}
			}
			tuple.setDistribution(offset + i, new NormalDistributionMixture(viewMixtures));
			tuple.setAttribute(i, offset + i);
		}

	}

	@Override
	public ProbabilisticViewFunction<T> clone() {
		return new ProbabilisticViewFunction<T>(this);
	}

	private void init(final SDFSchema schema) {
		final List<Integer> continuousList = new ArrayList<Integer>();
		final List<Integer> viewList = new ArrayList<Integer>();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			final SDFAttribute attr = schema.getAttributes().get(i);
			if (attr.getDatatype() instanceof SDFProbabilisticDatatype) {
				final SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attr.getDatatype();
				if (datatype.isContinuous()) {
					continuousList.add(i);
				}
			}
			if (!Arrays.asList(this.joinAttributePos).contains(i)) {
				viewList.add(i);
			}
		}
		this.continuousPos = continuousList.toArray(new Integer[0]);
		this.viewAttributePos = viewList.toArray(new Integer[0]);
		this.sigmas = new RealMatrix[this.viewAttributePos.length];
		this.betas = new RealMatrix[this.viewAttributePos.length];
	}

	/**
	 * Perform least square estimation of sigma
	 * 
	 * @param sweepArea
	 *            The {@link ITemporalSweepArea sweep area}
	 * @param joinAttributePos
	 *            Position array of all join attributes
	 * @param viewAttributePos
	 *            Position array of all view attributes
	 * @param index
	 *            Index of the current estimate
	 */
	private void updateLeastSquareEstimate(final ITemporalSweepArea<ProbabilisticTuple<T>> sweepArea, final Integer[] joinAttributePos, final Integer[] viewAttributePos, final int index) {

		final Iterator<ProbabilisticTuple<T>> iter = sweepArea.iterator();

		final int attributes = joinAttributePos.length + viewAttributePos.length;
		ProbabilisticTuple<T> element = null;
		final double[][] joinAttributesData = new double[joinAttributePos.length][sweepArea.size()];
		final double[][] viewAttributesData = new double[viewAttributePos.length][sweepArea.size()];

		int dimension = 0;
		while (iter.hasNext()) {
			element = iter.next();
			for (int i = 0; i < element.getAttributes().length; i++) {
				for (int j = 0; j < joinAttributePos.length; j++) {
					joinAttributesData[j][dimension] = element.getAttribute(joinAttributePos[j]);
				}
				for (int j = 0; j < viewAttributePos.length; j++) {
					viewAttributesData[j][dimension] = element.getAttribute(viewAttributePos[j]);
				}
			}
			dimension++;
		}
		System.out.println("Dimension " + dimension);
		final RealMatrix joinAttributes = MatrixUtils.createRealMatrix(joinAttributesData).transpose();
		System.out.println("A " + joinAttributes);

		final RealMatrix viewAttributes = MatrixUtils.createRealMatrix(viewAttributesData).transpose();
		System.out.println("B " + viewAttributes);

		final RealMatrix joinAttributesTranspose = joinAttributes.transpose();
		System.out.println("At " + joinAttributesTranspose);

		final RealMatrix viewAttributesTranspose = viewAttributes.transpose();
		System.out.println("Bt " + viewAttributesTranspose);

		final RealMatrix joinAttributesInverse = new LUDecomposition(joinAttributesTranspose.multiply(joinAttributes)).getSolver().getInverse();
		System.out.println("A^-1 " + joinAttributesInverse);

		final RealMatrix identity = MatrixUtils.createRealIdentityMatrix(dimension);
		System.out.println("I " + identity);

		final RealMatrix beta = joinAttributesInverse.multiply(joinAttributesTranspose).multiply(viewAttributes);
		System.out.println(beta);

		final RealMatrix sigma = (viewAttributesTranspose.multiply(identity.subtract(joinAttributes.multiply(joinAttributesInverse).multiply(joinAttributesTranspose))).multiply(viewAttributes)).scalarMultiply(1 / (dimension - attributes));

		System.out.println(sigma);

		this.betas[index] = beta;
		this.sigmas[index] = sigma;
	}

	private NormalDistribution updateDistributions(final NormalDistribution distribution, final int index) {

		final RealMatrix mean = MatrixUtils.createRealMatrix(1, distribution.getMean().length);
		mean.setColumn(0, distribution.getMean());

		final RealMatrix covarianceMatrix = distribution.getCovarianceMatrix().getMatrix();
		final RealMatrix newMean = MatrixUtils.createRealMatrix(1, this.betas[index].getColumnDimension() + mean.getColumnDimension());
		newMean.setSubMatrix(mean.getData(), 0, 0);
		newMean.setSubMatrix(this.betas[index].getData(), 0, mean.getColumnDimension());

		final RealMatrix newCovarianceMatrix = MatrixUtils.createRealMatrix(covarianceMatrix.getRowDimension() + this.sigmas[index].getRowDimension(), covarianceMatrix.getColumnDimension() + this.sigmas[index].getColumnDimension());

		newCovarianceMatrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);

		newCovarianceMatrix.setSubMatrix(this.betas[index].transpose().multiply(newCovarianceMatrix).getData(), 0, covarianceMatrix.getColumnDimension());

		newCovarianceMatrix.setSubMatrix(newCovarianceMatrix.multiply(this.betas[index]).getData(), covarianceMatrix.getRowDimension(), 0);

		newCovarianceMatrix.setSubMatrix(this.sigmas[index].add(this.betas[index].transpose().multiply(newCovarianceMatrix).multiply(this.betas[index])).getData(), covarianceMatrix.getRowDimension(), covarianceMatrix.getColumnDimension());

		final CovarianceMatrix covariance = CovarianceMatrixUtils.fromMatrix(newCovarianceMatrix);

		final NormalDistribution smoothedDistributions = new NormalDistribution(newMean.getData()[0], covariance);

		return smoothedDistributions;
	}

}
