/**
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class KalmanFilterPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	/** The attribute positions. */
	private final int[] attributes;

	private final ProcessModel process;
	private double[][] measurementNoise;
	private KalmanFilter filter;

	/**
	 * Creates a new Kalman filter operator.
	 * 
	 * @param attributes
	 *            The attribute positions
	 */
	public KalmanFilterPO(final int[] attributes, double[][] stateTransition, double[][] control, double[][] processNoise, double[][] measurementNoise) {
		this.attributes = attributes;
		this.measurementNoise = measurementNoise;
		this.process = new DefaultProcessModel(stateTransition, control, processNoise);
	}

	/**
	 * Clone constructor.
	 * 
	 * @param kalmanPO
	 *            The copy
	 */
	public KalmanFilterPO(final KalmanFilterPO<T> kalmanPO) {
		super(kalmanPO);
		this.attributes = kalmanPO.attributes.clone();
		this.process = new DefaultProcessModel(kalmanPO.process.getStateTransitionMatrix().copy(), kalmanPO.process.getControlMatrix().copy(), kalmanPO.process.getProcessNoise().copy(), kalmanPO.process.getInitialStateEstimate().copy(), kalmanPO.process.getInitialErrorCovariance().copy());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void process_next(ProbabilisticTuple<T> object, int port) {
		final NormalDistributionMixture[] distributions = object.getDistributions();
		final ProbabilisticTuple<T> outputVal = object.clone();

		final double[] value = new double[this.attributes.length];
		for (int i = 0; i < this.attributes.length; i++) {
			value[i] = ((Number) object.getAttribute(this.attributes[i])).doubleValue();
		}
		if (filter == null) {
			MeasurementModel measurement = new DefaultMeasurementModel(new double[][] { value }, measurementNoise);
			this.filter = new KalmanFilter(process, measurement);
		}
		this.filter.correct(value);

		double[] state = this.filter.getStateEstimation();
		double[][] covariance = this.filter.getErrorCovariance();

		final List<Pair<Double, MultivariateNormalDistribution>> mvns = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		MultivariateNormalDistribution component = new MultivariateNormalDistribution(state, covariance);
		mvns.add(new Pair<Double, MultivariateNormalDistribution>(1.0, component));

		final NormalDistributionMixture mixture = new NormalDistributionMixture(mvns);
		mixture.setAttributes(this.attributes);

		final NormalDistributionMixture[] outputValDistributions = new NormalDistributionMixture[distributions.length + 1];

		for (final int attribute : this.attributes) {
			outputVal.setAttribute(attribute, new ProbabilisticContinuousDouble(distributions.length));
		}
		// Copy the old distribution to the new tuple
		System.arraycopy(distributions, 0, outputValDistributions, 0, distributions.length);
		// And append the new distribution to the end of the array
		outputValDistributions[distributions.length] = mixture;
		outputVal.setDistributions(outputValDistributions);

		// KTHXBYE
		this.transfer(outputVal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
		return new KalmanFilterPO<>(this);
	}

}
