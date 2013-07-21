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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * Physical operator for Expectation Maximization (EM) classifier.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class EMPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	/** The sweep area to hold the data. */
	private final DefaultTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> area;
	/** The attribute positions. */
	private final int[] attributes;

	/**
	 * Creates a new EM operator.
	 * 
	 * @param attributes
	 *            The attribute positions
	 * @param mixtures
	 *            The number of mixtures
	 */
	public EMPO(final int[] attributes, final int mixtures) {
		this.attributes = attributes;
		this.area = new BatchEMTISweepArea(attributes, mixtures);
	}

	/**
	 * Clone constructor.
	 * 
	 * @param emPO
	 *            The copy
	 */
	public EMPO(final EMPO<T> emPO) {
		super(emPO);
		this.attributes = emPO.attributes.clone();
		this.area = emPO.area.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#getOutputMode()
	 */
	@Override
	public final OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
		final NormalDistributionMixture[] distributions = object.getDistributions();
		final ProbabilisticTuple<T> outputVal = object.clone();
		// Purge old elements out of the sweep area.
		synchronized (this.area) {
			this.area.purgeElements(object, Order.LeftRight);
		}
		// Insert the new element into the sweep area.
		// Expectation-step and Maximization-step will be done during insert.
		synchronized (this.area) {
			this.area.insert(object);
		}

		// Construct the multivariate distribution
		final List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		final BatchEMTISweepArea emArea = (BatchEMTISweepArea) this.area;
		for (int i = 0; i < emArea.getMixtures(); i++) {
			final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(emArea.getMean(i).getColumn(0), emArea.getCovarianceMatrix(i).getData());
			components.add(new Pair<Double, MultivariateNormalDistribution>(emArea.getWeight(i), distribution));
		}
		final NormalDistributionMixture mixture = new NormalDistributionMixture(components);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone()
	 */
	@Override
	public final AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
		return new EMPO<T>(this);
	}

}
