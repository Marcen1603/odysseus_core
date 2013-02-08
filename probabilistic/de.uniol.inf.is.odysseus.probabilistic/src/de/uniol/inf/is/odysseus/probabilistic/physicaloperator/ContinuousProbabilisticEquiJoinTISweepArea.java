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

/**
 * Probabilistic view as described in Tran, T. T. L., Peng, L., Diao, Y.,
 * McGregor, A., & Liu, A. (2011). CLARO: modeling and processing uncertain data
 * streams. The VLDB Journal. doi:10.1007/s00778-011-0261-7
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ContinuousProbabilisticEquiJoinTISweepArea extends
		JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory
			.getLogger(ContinuousProbabilisticEquiJoinTISweepArea.class);
	private final int[] joinAttributePos;
	private final int[] viewAttributePos;
	private final RealMatrix[] sigmas;
	private final RealMatrix[] betas;

	public ContinuousProbabilisticEquiJoinTISweepArea(int[] joinAttributePos,
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

	public RealMatrix[] getSigmas() {
		return this.sigmas;
	}

	public RealMatrix getSigma(int viewIndex) {
		return this.sigmas[viewIndex];
	}

	public RealMatrix[] getBetas() {
		return betas;
	}

	public RealMatrix getBeta(int viewIndex) {
		return betas[viewIndex];
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
	 * attribute. More formaly perform the following equation:
	 * \beta = (A^{T} A)^{-1} A^{T} B
	 * \sigma = B^{T} (I - A(A^{T} A)^{-1} A^{T}) B/(n - k)
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
