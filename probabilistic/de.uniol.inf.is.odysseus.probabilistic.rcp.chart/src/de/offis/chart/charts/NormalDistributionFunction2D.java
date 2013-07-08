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

import org.jfree.data.function.Function2D;

public class NormalDistributionFunction2D implements Function2D {

	private final double sigma;
	private final double mean;

	public NormalDistributionFunction2D(final double sigma, final double mean) {
		this.sigma = sigma;
		this.mean = mean;

	}

	@Override
	public double getValue(final double x) {
		final double exp = -(Math.pow((x - this.mean), 2) / (2 * Math.pow(
				this.sigma, 2)));
		return (1 / (this.sigma * Math.sqrt(2 * Math.PI)))
				* Math.pow(Math.E, exp);
	}
}
