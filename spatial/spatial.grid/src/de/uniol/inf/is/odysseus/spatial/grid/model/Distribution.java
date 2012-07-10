/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.spatial.grid.model;

public class Distribution {
	private double mean;
	private double deviation;

	public Distribution(double mean, double deviation) {
		this.mean = mean;
		this.deviation = deviation;
	}

	public double getMean() {
		return mean;
	}

	public double getDeviation() {
		return deviation;
	}

	public double getVariance() {
		return Math.pow(deviation, 2);
	}

	public Distribution add(Distribution other) {
		return new Distribution(this.mean + other.mean, Math.sqrt(Math.pow(
				this.deviation, 2) + Math.pow(other.deviation, 2)));
	}

	public Distribution sub(Distribution other) {
		return new Distribution(this.mean - other.mean, Math.sqrt(Math.pow(
				this.deviation, 2) - Math.pow(other.deviation, 2)));
	}

	public double getValue(double value) {
		return phi((value - this.mean) / getDeviation()) / getDeviation();
	}

	private double phi(double value) {
		return Math.exp(-1 * Math.pow(value, 2) / 2) / Math.sqrt(2 * Math.PI);
	}

	public double getArea(double value) {
		double x = (value - this.mean) / getDeviation();
		if (x < -8.0) {
			return 0.0;
		}
		if (x > 8.0) {
			return 1.0;
		}
		double sum = 0.0, term = x;
		for (int i = 3; sum + term != sum; i += 2) {
			sum = sum + term;
			term = term * x * x / i;
		}
		return 0.5 + sum * phi(x);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("mu: ").append(getMean()).append(" sigma: ")
				.append(getDeviation());
		return sb.toString();
	}
}
