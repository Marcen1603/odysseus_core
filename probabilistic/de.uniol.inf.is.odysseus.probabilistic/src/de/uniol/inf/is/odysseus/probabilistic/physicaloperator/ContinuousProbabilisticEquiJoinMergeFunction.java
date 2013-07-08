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

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.AbstractRelationalMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <M>
 */
public class ContinuousProbabilisticEquiJoinMergeFunction<M extends IMetaAttribute> extends AbstractRelationalMergeFunction<ProbabilisticTuple<M>, M> implements IDataMergeFunction<ProbabilisticTuple<M>, M> {

	private final RealMatrix[][] betas;
	private final RealMatrix[][] sigmas;
	private final int[][] joinAttributePos;
	private final int[][] viewAttributePos;

	public ContinuousProbabilisticEquiJoinMergeFunction(final ContinuousProbabilisticEquiJoinMergeFunction<M> mergeFunction) {
		super(mergeFunction.schemaSize);
		this.betas = mergeFunction.betas.clone();
		this.sigmas = mergeFunction.sigmas.clone();
		this.joinAttributePos = mergeFunction.joinAttributePos.clone();
		this.viewAttributePos = mergeFunction.viewAttributePos.clone();
	}

	@Override
	public ContinuousProbabilisticEquiJoinMergeFunction<M> clone() {
		return new ContinuousProbabilisticEquiJoinMergeFunction<M>(this);
	}

	public ContinuousProbabilisticEquiJoinMergeFunction(final int outputSchemaSize, final int[][] joinAttributePos, final int[][] viewAttributePos) {
		super(outputSchemaSize);
		this.betas = new RealMatrix[2][];
		this.sigmas = new RealMatrix[2][];
		this.joinAttributePos = joinAttributePos;
		this.viewAttributePos = viewAttributePos;
	}

	@Override
	public ProbabilisticTuple<M> merge(final ProbabilisticTuple<M> left, final ProbabilisticTuple<M> right, final IMetadataMergeFunction<M> metamerge, final Order order) {

		// if (order == Order.LeftRight) {
		// for (int i = 0; i < result.getAttributes().length; i++) {
		// result.setAttribute(this.leftViewAttributePos[i],
		// this.areas[port]);
		// }
		// for (int i = 0; i < this.rightViewAttributePos.length; i++) {
		// result.setAttribute(object.getAttributes().length
		// + this.rightViewAttributePos[i], this.areas[otherport]);
		// }
		// } else {
		// for (int i = 0; i < this.leftViewAttributePos.length; i++) {
		// result.setAttribute(this.leftViewAttributePos[i],
		// this.areas[otherport]);
		// }
		// for (int i = 0; i < this.rightViewAttributePos.length; i++) {
		// result.setAttribute(
		// newElement.getAttributes().length
		// - object.getAttributes().length
		// + this.rightViewAttributePos[i],
		// this.areas[port]);
		// }
		// }

		final ProbabilisticTuple<M> result = (ProbabilisticTuple<M>) left.merge(left, right, metamerge, order);

		return result;
	}

	@Override
	public void init() {

	}

	public void setBetas(final RealMatrix[] betas, final int port) {
		this.betas[port] = betas;
	}

	public void setSigmas(final RealMatrix[] sigmas, final int port) {
		this.sigmas[port] = sigmas;
	}

	//
	// private void updateDistribution(ProbabilisticTuple<M> tuple,
	// int[] attributePos) {
	//
	// }
	//
	// private void getDistibution(int joinAttributePos, int port,
	// int viewAttributePos) {
	// int otherport = port ^ 1;
	//
	// }

	public NormalDistribution getJoinDistribution(final NormalDistribution distribution, final int port, final int viewIndex) {
		final RealMatrix mean = MatrixUtils.createRealMatrix(1, distribution.getMean().length);
		mean.setColumn(0, distribution.getMean());

		final RealMatrix covarianceMatrix = distribution.getCovarianceMatrix().getMatrix();
		final RealMatrix newMean = MatrixUtils.createRealMatrix(1, this.betas[port][viewIndex].getColumnDimension() + mean.getColumnDimension());
		newMean.setSubMatrix(mean.getData(), 0, 0);
		newMean.setSubMatrix(this.betas[port][viewIndex].getData(), 0, mean.getColumnDimension());

		final RealMatrix newCovarianceMatrix = MatrixUtils.createRealMatrix(covarianceMatrix.getRowDimension() + this.sigmas[port][viewIndex].getRowDimension(), covarianceMatrix.getColumnDimension() + this.sigmas[port][viewIndex].getColumnDimension());

		newCovarianceMatrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);

		newCovarianceMatrix.setSubMatrix(this.betas[port][viewIndex].transpose().multiply(newCovarianceMatrix).getData(), 0, covarianceMatrix.getColumnDimension());

		newCovarianceMatrix.setSubMatrix(newCovarianceMatrix.multiply(this.betas[port][viewIndex]).getData(), covarianceMatrix.getRowDimension(), 0);

		newCovarianceMatrix.setSubMatrix(this.sigmas[port][viewIndex].add(this.betas[port][viewIndex].transpose().multiply(newCovarianceMatrix).multiply(this.betas[port][viewIndex])).getData(), covarianceMatrix.getRowDimension(), covarianceMatrix.getColumnDimension());

		final CovarianceMatrix covariance = CovarianceMatrixUtils.fromMatrix(newCovarianceMatrix);

		final NormalDistribution joinDistribution = new NormalDistribution(newMean.getData()[0], covariance);

		return joinDistribution;
	}
}
