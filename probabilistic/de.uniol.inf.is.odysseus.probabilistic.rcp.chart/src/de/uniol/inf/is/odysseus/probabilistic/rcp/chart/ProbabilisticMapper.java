/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.rcp.chart;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticMapper implements net.ericaro.surfaceplotter.Mapper {

	/** The mixtures. */
	private final Map<NormalDistributionFunctionND, Double> mixtures = new ConcurrentHashMap<NormalDistributionFunctionND, Double>();
	/** The scale. */
	private double scale = 1.0;
	/** The interval. */
	private Interval[] interval;

	/**
	 * 
	 * @param mixture
	 *            The normal distribution mixture
	 */
	public final void setup(final NormalDistributionMixture mixture) {
		this.mixtures.clear();
		for (final Pair<Double, MultivariateNormalDistribution> entry : mixture.getMixtures().getComponents()) {
			final MultivariateNormalDistribution normalDistribution = entry.getValue();
			final Double weight = entry.getKey();
			final double[] means = normalDistribution.getMeans();
			final RealMatrix sigma = normalDistribution.getCovariances();
			this.mixtures.put(new NormalDistributionFunctionND(means, sigma),
					weight);
		}

		this.scale = mixture.getScale();
		this.interval = mixture.getSupport();
	}

	/*
	 * 
	 * @see net.ericaro.surfaceplotter.Mapper#f1(float, float)
	 */
	@Override
	public final float f1(final float x, final float y) {
		double result = 0.0;
		for (final Entry<NormalDistributionFunctionND, Double> e : this.mixtures
				.entrySet()) {
			if ((this.interval[0].contains(x))
					&& (this.interval[1].contains(y))) {
				result += e.getValue()
						* e.getKey().getValue(new double[] { x, y });
			}
		}

		return (float) (result * this.scale);
	}

	/*
	 * 
	 * @see net.ericaro.surfaceplotter.Mapper#f2(float, float)
	 */
	@Override
	public final float f2(final float x, final float y) {
		return 0;
	}
}
