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
package de.offis.chart.charts;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

@SuppressWarnings("unused")
public class ProbabilityMapper implements net.ericaro.surfaceplotter.Mapper {

	private final Map<NormalDistributionFunctionND, Double> funcs = new ConcurrentHashMap<NormalDistributionFunctionND, Double>();

	private double scale = 1.0;
	private Interval[] interval;

	public void setup(final NormalDistributionMixture mixture) {
		this.funcs.clear();
		for (final Entry<NormalDistribution, Double> e : mixture.getMixtures()
				.entrySet()) {
			final double[] means = e.getKey().getMean();
			final CovarianceMatrix m = e.getKey().getCovarianceMatrix();
			this.funcs.put(new NormalDistributionFunctionND(means, m),
					e.getValue());
		}

		this.scale = mixture.getScale();
		this.interval = mixture.getSupport();
	}

	@Override
	public float f1(final float x, final float y) {
		// if ((interval != null) && (interval.length >= 2)) {
		// if ((interval[0] != null) && ((x < interval[0].inf()) || (x >
		// interval[0].sup())))
		// return 0f;
		//
		// if ((interval[1] != null) && ((y < interval[1].inf()) || (y >
		// interval[1].sup())))
		// return 0f;
		// }
		double result = 0.0;

		for (final Entry<NormalDistributionFunctionND, Double> e : this.funcs
				.entrySet()) {
			result += e.getValue() * e.getKey().getValue(new double[] { x, y });
		}

		return (float) (result * this.scale);
	}

	@Override
	public float f2(final float x, final float y) {
		return 0;
	}
}
