package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Implementation of a probabilistic view to generate a distribution for an
 * equi-join
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public class ProbabilisticViewFunction<T extends ITimeInterval> {

	private final SDFSchema schema;
	@SuppressWarnings("unused")
	private Integer[] continuousPos;
	private Integer[] joinAttributePos;
	private Integer[] viewAttributePos;
	private DefaultTISweepArea<ProbabilisticTuple<T>> sweepArea = new DefaultTISweepArea<ProbabilisticTuple<T>>();
	private RealMatrix[] sigmas;
	private RealMatrix[] betas;

	public ProbabilisticViewFunction(final Integer[] pos, final SDFSchema schema) {
		this.joinAttributePos = pos;
		this.schema = schema;
		init(this.schema);
	}

	public ProbabilisticViewFunction(
			final ProbabilisticViewFunction<T> probabilisticViewPO) {
		this.joinAttributePos = probabilisticViewPO.joinAttributePos;
		this.viewAttributePos = probabilisticViewPO.viewAttributePos;
		this.schema = probabilisticViewPO.schema.clone();
		init(this.schema);
	}

	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	protected void merge(final ProbabilisticTuple<T> left,
			final ProbabilisticTuple<T> right) {
		sweepArea.purgeElements(right, Order.LeftRight);
		sweepArea.insert(right);
		int attributes = right.getAttributes().length;
		if (sweepArea.size() > attributes) {
			for (int i = 0; i < viewAttributePos.length; i++) {
				updateLeastSquareEstimate(this.sweepArea, joinAttributePos,
						viewAttributePos, i);
			}
		}

		IMetadataMergeFunction<T> mergeFunction = new CombinedMergeFunction<T>();

		ProbabilisticTuple<T> tuple = (ProbabilisticTuple<T>) left.merge(left,
				right, mergeFunction, Order.LeftRight);
		int offset = left.getDistributions().length
				+ right.getDistributions().length;
		NormalDistributionMixture[] distributions = new NormalDistributionMixture[offset
				+ viewAttributePos.length];

		tuple.setDistributions(distributions);
		for (int i = 0; i < left.getDistributions().length; i++) {
			tuple.setDistribution(i, left.getDistribution(i));
		}
		for (int i = 0; i < right.getDistributions().length; i++) {
			tuple.setDistribution(left.getDistributions().length + i,
					right.getDistribution(i));
		}
		for (int i = 0; i < viewAttributePos.length; i++) {
			Map<NormalDistribution, Double> viewMixtures = new HashMap<NormalDistribution, Double>();

			for (int j = 0; j < joinAttributePos.length; j++) {
				NormalDistributionMixture distributionMixture = left
						.getDistribution(joinAttributePos[j]);
				Map<NormalDistribution, Double> mixtures = distributionMixture
						.getMixtures();
				for (Entry<NormalDistribution, Double> mixture : mixtures
						.entrySet()) {
					NormalDistribution distribution = mixture.getKey();
					NormalDistribution viewDistribution = updateDistributions(
							distribution, i);
					viewMixtures.put(viewDistribution, mixture.getValue());
				}
			}
			tuple.setDistribution(offset + i, new NormalDistributionMixture(
					viewMixtures));
			tuple.setAttribute(i, offset + i);
		}

	}

	@Override
	public ProbabilisticViewFunction<T> clone() {
		return new ProbabilisticViewFunction<T>(this);
	}

	private void init(SDFSchema schema) {
		List<Integer> continuousList = new ArrayList<Integer>();
		List<Integer> viewList = new ArrayList<Integer>();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			SDFAttribute attr = schema.getAttributes().get(i);
			if (attr.getDatatype() instanceof SDFProbabilisticDatatype) {
				SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attr
						.getDatatype();
				if (datatype.isContinuous()) {
					continuousList.add(i);
				}
			}
			if (!Arrays.asList(joinAttributePos).contains(i)) {
				viewList.add(i);
			}
		}
		this.continuousPos = continuousList.toArray(new Integer[0]);
		this.viewAttributePos = viewList.toArray(new Integer[0]);
		this.sigmas = new RealMatrix[viewAttributePos.length];
		this.betas = new RealMatrix[viewAttributePos.length];
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
	private void updateLeastSquareEstimate(
			ITemporalSweepArea<ProbabilisticTuple<T>> sweepArea,
			Integer[] joinAttributePos, Integer[] viewAttributePos, int index) {

		Iterator<ProbabilisticTuple<T>> iter = sweepArea.iterator();

		int attributes = joinAttributePos.length + viewAttributePos.length;
		ProbabilisticTuple<T> element = null;
		double[][] joinAttributesData = new double[joinAttributePos.length][sweepArea
				.size()];
		double[][] viewAttributesData = new double[viewAttributePos.length][sweepArea
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

		this.betas[index] = beta;
		this.sigmas[index] = sigma;
	}

	private NormalDistribution updateDistributions(
			NormalDistribution distribution, int index) {

		RealMatrix mean = MatrixUtils.createRealMatrix(1,
				distribution.getMean().length);
		mean.setColumn(0, distribution.getMean());

		RealMatrix covarianceMatrix = distribution.getCovarianceMatrix()
				.getMatrix();
		RealMatrix newMean = MatrixUtils.createRealMatrix(1,
				betas[index].getColumnDimension() + mean.getColumnDimension());
		newMean.setSubMatrix(mean.getData(), 0, 0);
		newMean.setSubMatrix(betas[index].getData(), 0,
				mean.getColumnDimension());

		RealMatrix newCovarianceMatrix = MatrixUtils.createRealMatrix(
				covarianceMatrix.getRowDimension()
						+ sigmas[index].getRowDimension(),
				covarianceMatrix.getColumnDimension()
						+ sigmas[index].getColumnDimension());

		newCovarianceMatrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);

		newCovarianceMatrix.setSubMatrix(
				betas[index].transpose().multiply(newCovarianceMatrix)
						.getData(), 0, covarianceMatrix.getColumnDimension());

		newCovarianceMatrix.setSubMatrix(
				newCovarianceMatrix.multiply(betas[index]).getData(),
				covarianceMatrix.getRowDimension(), 0);

		newCovarianceMatrix.setSubMatrix(
				sigmas[index].add(
						betas[index].transpose().multiply(newCovarianceMatrix)
								.multiply(betas[index])).getData(),
				covarianceMatrix.getRowDimension(),
				covarianceMatrix.getColumnDimension());

		CovarianceMatrix covariance = CovarianceMatrixUtils
				.fromMatrix(newCovarianceMatrix);

		NormalDistribution smoothedDistributions = new NormalDistribution(
				newMean.getData()[0], covariance);

		return smoothedDistributions;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "b", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "c", SDFDatatype.DOUBLE));

		// SDFSchema schema = new SDFSchema("", attr);
		Object[] attributes1 = new Object[] { 1.0, 1.0, 6.0 };
		Object[] attributes2 = new Object[] { 1.0, 2.0, 5.0 };
		Object[] attributes3 = new Object[] { 1.0, 3.0, 7.0 };
		Object[] attributes4 = new Object[] { 1.0, 4.0, 10.0 };

		ProbabilisticTuple<ITimeInterval> tuple1 = new ProbabilisticTuple<>(
				attributes1, true);
		tuple1.setMetadata(new TimeInterval());
		tuple1.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple2 = new ProbabilisticTuple<>(
				attributes2, true);
		tuple2.setMetadata(new TimeInterval());
		tuple2.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple3 = new ProbabilisticTuple<>(
				attributes3, true);
		tuple3.setMetadata(new TimeInterval());
		tuple3.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple4 = new ProbabilisticTuple<>(
				attributes4, true);
		tuple4.setMetadata(new TimeInterval());
		tuple4.getMetadata().setStart(PointInTime.currentPointInTime());

		// ProbabilisticViewFunction<ITimeInterval> probabilisticView = new
		// ProbabilisticViewFunction<ITimeInterval>(
		// new Integer[] { 0 }, schema);
		// probabilisticView.process_next(tuple1, 0);
		// probabilisticView.process_next(tuple2, 0);
		// probabilisticView.process_next(tuple3, 0);
		// probabilisticView.process_next(tuple4, 0);

	}
}
