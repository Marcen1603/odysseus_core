package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
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
public class ProbabilisticMergeFunction<T extends ITimeInterval>  {

	private final SDFSchema schema;
	@SuppressWarnings("unused")
	private Integer[] continuousPos;
	private Integer[] joinAttributePos;
	private Integer[] viewAttributePos;
	private DefaultTISweepArea<ProbabilisticTuple<T>> sweepArea = new DefaultTISweepArea<ProbabilisticTuple<T>>();

	public ProbabilisticMergeFunction(final Integer[] pos, final SDFSchema schema) {
		this.joinAttributePos = pos;
		this.schema = schema;
		init(this.schema);
	}

	public ProbabilisticMergeFunction(final ProbabilisticMergeFunction<T> probabilisticViewPO) {
		this.joinAttributePos = probabilisticViewPO.joinAttributePos;
		this.viewAttributePos = probabilisticViewPO.viewAttributePos;
		this.schema = probabilisticViewPO.schema.clone();
		init(this.schema);
	}

	
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}


	protected void process_next(final ProbabilisticTuple<T> object,
			final int port) {
		sweepArea.purgeElements(object, Order.LeftRight);
		sweepArea.insert(object);
		int attributes = object.getAttributes().length;
		if (sweepArea.size() > attributes) {

			NormalDistributionMixture[] distributions = new NormalDistributionMixture[viewAttributePos.length];
			for (int i = 0; i < viewAttributePos.length; i++) {
				distributions[i] = leastSquareEstimate(this.sweepArea,
						joinAttributePos, viewAttributePos);
			}
			object.setDistributions(distributions);
			//transfer(object);
		}
	}

	@Override
	public ProbabilisticMergeFunction<T> clone() {
		return new ProbabilisticMergeFunction<T>(this);
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
	 * @return
	 */
	private NormalDistributionMixture leastSquareEstimate(
			ITemporalSweepArea<ProbabilisticTuple<T>> sweepArea,
			Integer[] joinAttributePos2, Integer[] viewAttributePos2) {

		Iterator<ProbabilisticTuple<T>> iter = sweepArea.iterator();

		int attributes = joinAttributePos2.length + viewAttributePos2.length;
		ProbabilisticTuple<T> element = null;
		double[][] joinAttributesData = new double[joinAttributePos2.length][sweepArea
				.size()];
		double[][] viewAttributesData = new double[viewAttributePos2.length][sweepArea
				.size()];

		int dimension = 0;
		while (iter.hasNext()) {
			element = iter.next();
			for (int i = 0; i < element.getAttributes().length; i++) {
				for (int j = 0; j < joinAttributePos2.length; j++) {
					joinAttributesData[j][dimension] = element
							.getAttribute(joinAttributePos2[j]);
				}
				for (int j = 0; j < viewAttributePos2.length; j++) {
					viewAttributesData[j][dimension] = element
							.getAttribute(viewAttributePos2[j]);
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

		RealMatrix beta = joinAttributesInverse.multiply(joinAttributesTranspose)
				.multiply(viewAttributes);
		System.out.println(beta);

		RealMatrix sigma = (viewAttributesTranspose.multiply(identity
				.subtract(joinAttributes.multiply(joinAttributesInverse)
						.multiply(joinAttributesTranspose)))
				.multiply(viewAttributes))
				.scalarMultiply(1 / (dimension - attributes));

		System.out.println(sigma);

		CovarianceMatrix covariance = CovarianceMatrixUtils.fromMatrix(sigma);
		NormalDistributionMixture smoothedDistributions = new NormalDistributionMixture(
				0, covariance);

		RealMatrix newViewAttributes = joinAttributes.getRowMatrix(0).multiply(beta);
		System.out.println("Bnew "+newViewAttributes);
		
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

		SDFSchema schema = new SDFSchema("", attr);
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

		ProbabilisticMergeFunction<ITimeInterval> probabilisticView = new ProbabilisticMergeFunction<ITimeInterval>(
				new Integer[] { 0 }, schema);
		probabilisticView.process_next(tuple1, 0);
		probabilisticView.process_next(tuple2, 0);
		probabilisticView.process_next(tuple3, 0);
		probabilisticView.process_next(tuple4, 0);

	}
}
