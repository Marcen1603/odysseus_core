package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;

/**
 * Probabilistic view as described in Tran, T. T. L., Peng, L., Diao, Y.,
 * McGregor, A., & Liu, A. (2011). CLARO: modeling and processing uncertain data
 * streams. The VLDB Journal. doi:10.1007/s00778-011-0261-7
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticContinuousJoinTISweepArea extends
		JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory
			.getLogger(ProbabilisticContinuousJoinTISweepArea.class);
	private final int[] joinAttributePos;
	private final int[] viewAttributePos;
	private final RealMatrix[] sigmas;
	private final RealMatrix[] betas;

	public ProbabilisticContinuousJoinTISweepArea(int[] joinAttributePos,
			int[] viewAttributePos) {
		this.joinAttributePos = joinAttributePos;
		this.viewAttributePos = viewAttributePos;
		this.sigmas = new RealMatrix[viewAttributePos.length];
		this.betas = new RealMatrix[viewAttributePos.length];
	}

	@Override
	public void insert(ProbabilisticTuple<? extends ITimeInterval> s) {
		super.insert(s);
		updateLeastSquareEstimates();
	}

	@Override
	public void insertAll(
			List<ProbabilisticTuple<? extends ITimeInterval>> toBeInserted) {
		super.insertAll(toBeInserted);
		updateLeastSquareEstimates();
	}

	public RealMatrix getSigma(int viewIndex) {
		return this.sigmas[viewIndex];
	}

	public RealMatrix getBeta(int viewIndex) {
		return betas[viewIndex];
	}

	public NormalDistribution getJoinDistribution(
			NormalDistribution distribution, int viewIndex) {
		RealMatrix mean = MatrixUtils.createRealMatrix(1,
				distribution.getMean().length);
		mean.setColumn(0, distribution.getMean());

		RealMatrix covarianceMatrix = distribution.getCovarianceMatrix()
				.getMatrix();
		RealMatrix newMean = MatrixUtils.createRealMatrix(
				1,
				betas[viewIndex].getColumnDimension()
						+ mean.getColumnDimension());
		newMean.setSubMatrix(mean.getData(), 0, 0);
		newMean.setSubMatrix(betas[viewIndex].getData(), 0,
				mean.getColumnDimension());

		RealMatrix newCovarianceMatrix = MatrixUtils.createRealMatrix(
				covarianceMatrix.getRowDimension()
						+ sigmas[viewIndex].getRowDimension(),
				covarianceMatrix.getColumnDimension()
						+ sigmas[viewIndex].getColumnDimension());

		newCovarianceMatrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);

		newCovarianceMatrix.setSubMatrix(
				betas[viewIndex].transpose().multiply(newCovarianceMatrix)
						.getData(), 0, covarianceMatrix.getColumnDimension());

		newCovarianceMatrix.setSubMatrix(
				newCovarianceMatrix.multiply(betas[viewIndex]).getData(),
				covarianceMatrix.getRowDimension(), 0);

		newCovarianceMatrix.setSubMatrix(
				sigmas[viewIndex].add(
						betas[viewIndex].transpose()
								.multiply(newCovarianceMatrix)
								.multiply(betas[viewIndex])).getData(),
				covarianceMatrix.getRowDimension(),
				covarianceMatrix.getColumnDimension());

		CovarianceMatrix covariance = CovarianceMatrixUtils
				.fromMatrix(newCovarianceMatrix);

		NormalDistribution joinDistribution = new NormalDistribution(
				newMean.getData()[0], covariance);

		return joinDistribution;
	}

	/**
	 * Update the least square estimates for all view attributes
	 */
	private void updateLeastSquareEstimates() {
		for (int i = 0; i < viewAttributePos.length; i++) {
			updateLeastSquareEstimate(joinAttributePos, viewAttributePos, i);
		}
	}

	/**
	 * Perform least square estimation of sigma and beta for the given view
	 * attribute
	 * 
	 * @param joinAttributePos
	 *            Position array of all join attributes
	 * @param viewAttributePos
	 *            Position array of all view attributes
	 * @param viewIndex
	 *            Position of the current estimate
	 */
	private void updateLeastSquareEstimate(int[] joinAttributePos,
			int[] viewAttributePos, int viewIndex) {

		Iterator<ProbabilisticTuple<? extends ITimeInterval>> iter = this
				.iterator();

		int attributes = joinAttributePos.length + viewAttributePos.length;
		ProbabilisticTuple<? extends ITimeInterval> element = null;
		double[][] joinAttributesData = new double[joinAttributePos.length][this
				.size()];
		double[][] viewAttributesData = new double[viewAttributePos.length][this
				.size()];

		int dimension = 0;
		while (iter.hasNext()) {
			element = iter.next();
			for (int i = 0; i < element.getAttributes().length; i++) {
				for (int j = 0; j < joinAttributePos.length; j++) {
					joinAttributesData[j][dimension] = element
							.getAttribute(joinAttributePos[j]);
				}
				for (int j = 0; j < viewAttributePos.length; j++) {
					viewAttributesData[j][dimension] = element
							.getAttribute(viewAttributePos[j]);
				}
			}
			dimension++;
		}
		System.out.println("Dimension " + dimension);
		RealMatrix joinAttributes = MatrixUtils.createRealMatrix(
				joinAttributesData).transpose();
		System.out.println("A " + joinAttributes);

		RealMatrix viewAttributes = MatrixUtils.createRealMatrix(
				viewAttributesData).transpose();
		System.out.println("B " + viewAttributes);

		RealMatrix joinAttributesTranspose = joinAttributes.transpose();
		System.out.println("At " + joinAttributesTranspose);

		RealMatrix viewAttributesTranspose = viewAttributes.transpose();
		System.out.println("Bt " + viewAttributesTranspose);

		RealMatrix joinAttributesInverse = new LUDecomposition(
				joinAttributesTranspose.multiply(joinAttributes)).getSolver()
				.getInverse();
		System.out.println("A^-1 " + joinAttributesInverse);

		RealMatrix identity = MatrixUtils.createRealIdentityMatrix(dimension);
		System.out.println("I " + identity);

		RealMatrix beta = joinAttributesInverse.multiply(
				joinAttributesTranspose).multiply(viewAttributes);
		System.out.println(beta);

		RealMatrix sigma = (viewAttributesTranspose.multiply(identity
				.subtract(joinAttributes.multiply(joinAttributesInverse)
						.multiply(joinAttributesTranspose)))
				.multiply(viewAttributes))
				.scalarMultiply(1 / (dimension - attributes));

		System.out.println(sigma);

		this.betas[viewIndex] = beta;
		this.sigmas[viewIndex] = sigma;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
