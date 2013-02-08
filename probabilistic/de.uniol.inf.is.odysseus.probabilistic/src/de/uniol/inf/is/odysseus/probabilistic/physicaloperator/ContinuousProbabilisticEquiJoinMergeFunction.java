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
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <M>
 */
public class ContinuousProbabilisticEquiJoinMergeFunction<M extends IMetaAttribute>
		extends AbstractRelationalMergeFunction<ProbabilisticTuple<M>, M>
		implements IDataMergeFunction<ProbabilisticTuple<M>, M> {

	private final RealMatrix[][] betas;
	private final RealMatrix[][] sigmas;
	private final int[][] joinAttributePos;
	private final int[][] viewAttributePos;

	public ContinuousProbabilisticEquiJoinMergeFunction(
			ContinuousProbabilisticEquiJoinMergeFunction<M> mergeFunction) {
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

	public ContinuousProbabilisticEquiJoinMergeFunction(int outputSchemaSize,
			int[][] joinAttributePos, int[][] viewAttributePos) {
		super(outputSchemaSize);
		this.betas = new RealMatrix[2][];
		this.sigmas = new RealMatrix[2][];
		this.joinAttributePos = joinAttributePos;
		this.viewAttributePos = viewAttributePos;
	}

	@Override
	public ProbabilisticTuple<M> merge(ProbabilisticTuple<M> left,
			ProbabilisticTuple<M> right, IMetadataMergeFunction<M> metamerge,
			Order order) {

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

		ProbabilisticTuple<M> result = (ProbabilisticTuple<M>) left.merge(left,
				right, metamerge, order);

		return result;
	}

	@Override
	public void init() {

	}

	public void setBetas(RealMatrix[] betas, int port) {
		this.betas[port] = betas;
	}

	public void setSigmas(RealMatrix[] sigmas, int port) {
		this.sigmas[port] = sigmas;
	}

	private void updateDistribution(ProbabilisticTuple<M> tuple,
			int[] attributePos) {

	}

	private void getDistibution(int joinAttributePos, int port,
			int viewAttributePos) {
		int otherport = port ^ 1;

	}

	public NormalDistribution getJoinDistribution(
			NormalDistribution distribution, int port, int viewIndex) {
		RealMatrix mean = MatrixUtils.createRealMatrix(1,
				distribution.getMean().length);
		mean.setColumn(0, distribution.getMean());

		RealMatrix covarianceMatrix = distribution.getCovarianceMatrix()
				.getMatrix();
		RealMatrix newMean = MatrixUtils.createRealMatrix(
				1,
				betas[port][viewIndex].getColumnDimension()
						+ mean.getColumnDimension());
		newMean.setSubMatrix(mean.getData(), 0, 0);
		newMean.setSubMatrix(betas[port][viewIndex].getData(), 0,
				mean.getColumnDimension());

		RealMatrix newCovarianceMatrix = MatrixUtils.createRealMatrix(
				covarianceMatrix.getRowDimension()
						+ sigmas[port][viewIndex].getRowDimension(),
				covarianceMatrix.getColumnDimension()
						+ sigmas[port][viewIndex].getColumnDimension());

		newCovarianceMatrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);

		newCovarianceMatrix.setSubMatrix(betas[port][viewIndex].transpose()
				.multiply(newCovarianceMatrix).getData(), 0,
				covarianceMatrix.getColumnDimension());

		newCovarianceMatrix.setSubMatrix(
				newCovarianceMatrix.multiply(betas[port][viewIndex]).getData(),
				covarianceMatrix.getRowDimension(), 0);

		newCovarianceMatrix.setSubMatrix(
				sigmas[port][viewIndex].add(
						betas[port][viewIndex].transpose()
								.multiply(newCovarianceMatrix)
								.multiply(betas[port][viewIndex])).getData(),
				covarianceMatrix.getRowDimension(),
				covarianceMatrix.getColumnDimension());

		CovarianceMatrix covariance = CovarianceMatrixUtils
				.fromMatrix(newCovarianceMatrix);

		NormalDistribution joinDistribution = new NormalDistribution(
				newMean.getData()[0], covariance);

		return joinDistribution;
	}
}
