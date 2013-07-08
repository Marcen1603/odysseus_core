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

package de.uniol.inf.is.odysseus.probabilistic.math;

public class LinCDF implements CumulativeDistributionFunction {
	private double[][] points;

	@Override
	public double evaluate(final double x) {
		if (x < this.points[0][0]) {
			return 0.0;
		}
		if (x >= this.points[this.points.length - 1][0]) {
			return 1.0;
		}

		for (int i = 0; i < (this.points.length - 1); i++) {
			if ((this.points[i][0] <= x) && (x < this.points[i + 1][0])) {
				return this.points[i][1] + (((x - this.points[i][0]) / (this.points[i + 1][0] - this.points[i][0])) * (this.points[i + 1][1] - this.points[i][1]));
			}
		}
		throw new IllegalArgumentException("No data points found");
	}

	@Override
	public CumulativeDistributionFunction add() {
		// TODO Auto-generated method stub
		return null;
	}

}
