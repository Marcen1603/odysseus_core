/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
import java.util.Collection;

import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class LinearRegressionPO<T extends ITimeInterval> extends
		AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	private final RegressionTISweepArea area;

	public LinearRegressionPO(SDFSchema inputSchema, int[] dependentList,
			int[] explanatoryList) {
		area = new RegressionTISweepArea(dependentList, explanatoryList);
	}

	public LinearRegressionPO(LinearRegressionPO<T> linearRegressionPO) {
		super(linearRegressionPO);
		this.area = linearRegressionPO.area.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(ProbabilisticTuple<T> object, int port) {
		synchronized (area) {
			area.insert(object);
		}
		if (area.isEstimatable()) {
			RealMatrix regressionCoefficients = area
					.getRegressionCoefficients();
			RealMatrix residual = area.getResidual();

			NormalDistributionMixture mixture = new NormalDistributionMixture(
					new double[residual.getColumnDimension()],
					CovarianceMatrixUtils.fromMatrix(residual));
			mixture.setAttributes(area.getExplanatoryAttributePos());

			NormalDistributionMixture[] distributions = object
					.getDistributions();
			Object[] attributes = object.getAttributes();

			ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(
					new Object[attributes.length + 1],
					new NormalDistributionMixture[distributions.length + 1],
					object.requiresDeepClone());
			outputVal.setDistribution(distributions.length, mixture);

			System.arraycopy(distributions, 0, outputVal.getDistributions(), 0,
					distributions.length);
			System.arraycopy(object.getAttributes(), 0,
					outputVal.getAttributes(), 0, object.getAttributes().length);

			for (int i = 0; i < area.getExplanatoryAttributePos().length; i++) {
				int pos = area.getExplanatoryAttributePos()[i];
				outputVal.setAttribute(pos, new ProbabilisticContinuousDouble(
						distributions.length));
			}
			outputVal.setAttribute(object.getAttributes().length,
					regressionCoefficients.getData());

			outputVal.setMetadata((T) object.getMetadata().clone());
			this.transfer(outputVal);
		}
	}

	@Override
	public AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
		return new LinearRegressionPO<T>(this);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "b", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "c", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "d", SDFDatatype.DOUBLE));

		SDFSchema schema = new SDFSchema("", attr);
		Object[] attributes1 = new Object[] { 1.0, 1.0, 6.0, 11.0 };
		Object[] attributes2 = new Object[] { 1.0, 2.0, 5.0, 12.0 };
		Object[] attributes3 = new Object[] { 1.0, 3.0, 7.0, 13.0 };
		Object[] attributes4 = new Object[] { 1.0, 4.0, 8.0, 14.0 };
		Object[] attributes5 = new Object[] { 1.0, 5.0, 9.0, 15.0 };
		Object[] attributes6 = new Object[] { 1.0, 6.0, 10.0, 16.0 };

		ProbabilisticTuple<ITimeInterval> tuple1 = new ProbabilisticTuple<>(
				attributes1, true);
		tuple1.setMetadata(new TimeIntervalProbabilistic());
		tuple1.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple2 = new ProbabilisticTuple<>(
				attributes2, true);
		tuple2.setMetadata(new TimeIntervalProbabilistic());
		tuple2.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple3 = new ProbabilisticTuple<>(
				attributes3, true);
		tuple3.setMetadata(new TimeIntervalProbabilistic());
		tuple3.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple4 = new ProbabilisticTuple<>(
				attributes4, true);
		tuple4.setMetadata(new TimeIntervalProbabilistic());
		tuple4.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple5 = new ProbabilisticTuple<>(
				attributes5, true);
		tuple5.setMetadata(new TimeIntervalProbabilistic());
		tuple5.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple6 = new ProbabilisticTuple<>(
				attributes6, true);
		tuple6.setMetadata(new TimeIntervalProbabilistic());
		tuple6.getMetadata().setStart(PointInTime.currentPointInTime());

		LinearRegressionPO<ITimeInterval> linearRegression = new LinearRegressionPO<ITimeInterval>(
				schema, new int[] { 0, 3 }, new int[] { 1, 2 });
		linearRegression.process_next(tuple1, 0);
		linearRegression.process_next(tuple2, 0);
		linearRegression.process_next(tuple3, 0);
		linearRegression.process_next(tuple4, 0);
		linearRegression.process_next(tuple5, 0);
		linearRegression.process_next(tuple6, 0);

	}
}
