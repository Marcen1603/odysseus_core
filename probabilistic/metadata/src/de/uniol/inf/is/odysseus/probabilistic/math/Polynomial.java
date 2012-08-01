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
package de.uniol.inf.is.odysseus.probabilistic.math;

import java.util.Arrays;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class Polynomial {
	private double[] coefficients;
	private int degree;

	public Polynomial(double coefficient, int degree) {
		this.coefficients = new double[degree + 1];
		this.coefficients[degree] = coefficient;
		this.degree = degree();
	}

	public int degree() {
		int degree = 0;
		for (int i = 0; i < coefficients.length; i++) {
			if (coefficients[i] != 0) {
				degree = i;
			}
		}
		return degree;
	}

	public double coefficient(int degree) {
		return coefficients[degree];
	}

	public Polynomial plus(Polynomial other) {
		Polynomial result = new Polynomial(0.0, Math.max(this.degree,
				other.degree));
		for (int i = 0; i <= this.degree; i++) {
			result.coefficients[i] += this.coefficients[i];
		}
		for (int i = 0; i <= other.degree; i++) {
			result.coefficients[i] += other.coefficients[i];
		}
		result.degree = result.degree();
		return result;
	}

	public Polynomial minus(Polynomial other) {
		Polynomial result = new Polynomial(0, Math.max(this.degree,
				other.degree));
		for (int i = 0; i <= this.degree; i++) {
			result.coefficients[i] += this.coefficients[i];
		}
		for (int i = 0; i <= other.degree; i++) {
			result.coefficients[i] -= other.coefficients[i];
		}
		result.degree = result.degree();
		return result;
	}

	public Polynomial times(Polynomial other) {
		Polynomial result = new Polynomial(0, this.degree + other.degree);
		for (int i = 0; i <= this.degree; i++) {
			for (int j = 0; j <= other.degree; j++) {
				result.coefficients[i + j] += (this.coefficients[i] * other.coefficients[j]);
			}
		}
		result.degree = result.degree();
		return result;
	}

	public Polynomial times(double value) {
		Polynomial result = new Polynomial(0, this.degree);
		for (int i = 0; i <= this.degree; i++) {
			result.coefficients[i] += (this.coefficients[i] * value);
		}
		result.degree = result.degree();
		return result;
	}

	public Polynomial compose(Polynomial other) {
		Polynomial result = new Polynomial(0, 0);
		for (int i = this.degree; i >= 0; i--) {
			Polynomial term = new Polynomial(this.coefficients[i], 0);
			result = term.plus(other.times(result));
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(coefficients);
		result = prime * result + degree;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Polynomial other = (Polynomial) obj;
		if (this.degree != other.degree) {
			return false;
		}
		for (int i = this.degree; i >= 0; i--) {
			if (this.coefficients[i] != other.coefficients[i])
				return false;
		}
		return true;
	}

	public double evaluate(double x) {
		double result = 0.0;
		for (int i = this.degree; i >= 0; i--) {
			result = this.coefficients[i] + (x * result);
		}
		return result;
	}

	public Polynomial differentiate() {
		if (degree == 0) {
			return new Polynomial(0, 0);
		}
		Polynomial deriv = new Polynomial(0, degree - 1);
		deriv.degree = degree - 1;
		for (int i = 0; i < degree; i++) {
			deriv.coefficients[i] = (i + 1) * coefficients[i + 1];
		}
		return deriv;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (degree == 0) {
			return sb.append(coefficients[0]).toString();
		}
		if (degree == 1) {
			return sb.append(coefficients[1]).append("x + ")
					.append(coefficients[0]).toString();
		}
		sb.append(coefficients[degree]).append("x^").append(degree);
		for (int i = degree - 1; i >= 0; i--) {
			if (coefficients[i] == 0) {
				continue;
			} else if (coefficients[i] > 0) {
				sb.append(" + ").append(coefficients[i]);
			} else if (coefficients[i] < 0) {
				sb.append(" - ").append(-coefficients[i]);
			}
			if (i == 1) {
				sb.append("x");
			} else if (i > 1) {
				sb.append("x^").append(i);
			}
		}
		return sb.toString();
	}
}
