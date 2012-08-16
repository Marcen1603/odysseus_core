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
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Polynomial {
    private final double[] coefficients;
    private int            degree;

    public Polynomial(final double coefficient, final int degree) {
        this.coefficients = new double[degree + 1];
        this.coefficients[degree] = coefficient;
        this.degree = this.degree();
    }

    public int degree() {
        int degree = 0;
        for (int i = 0; i < this.coefficients.length; i++) {
            if (this.coefficients[i] != 0.0) {
                degree = i;
            }
        }
        return degree;
    }

    public double coefficient(final int degree) {
        return this.coefficients[degree];
    }

    public Polynomial add(final Polynomial other) {
        final Polynomial result = new Polynomial(0.0, Math.max(this.degree, other.degree));
        for (int i = 0; i <= this.degree; i++) {
            result.coefficients[i] += this.coefficients[i];
        }
        for (int i = 0; i <= other.degree; i++) {
            result.coefficients[i] += other.coefficients[i];
        }
        result.degree = result.degree();
        return result;
    }

    public Polynomial substract(final Polynomial other) {
        final Polynomial result = new Polynomial(0.0, Math.max(this.degree, other.degree));
        for (int i = 0; i <= this.degree; i++) {
            result.coefficients[i] += this.coefficients[i];
        }
        for (int i = 0; i <= other.degree; i++) {
            result.coefficients[i] -= other.coefficients[i];
        }
        result.degree = result.degree();
        return result;
    }

    public Polynomial multiply(final Polynomial other) {
        final Polynomial result = new Polynomial(0.0, this.degree + other.degree);
        for (int i = 0; i <= this.degree; i++) {
            for (int j = 0; j <= other.degree; j++) {
                result.coefficients[i + j] += (this.coefficients[i] * other.coefficients[j]);
            }
        }
        result.degree = result.degree();
        return result;
    }

    public Polynomial compose(final Polynomial other) {
        Polynomial result = new Polynomial(0.0, 0);
        for (int i = this.degree; i >= 0; i--) {
            final Polynomial term = new Polynomial(this.coefficients[i], 0);
            result = term.add(other.multiply(result));
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + Arrays.hashCode(this.coefficients);
        result = (prime * result) + this.degree;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Polynomial other = (Polynomial) obj;
        if (this.degree != other.degree) {
            return false;
        }
        for (int i = this.degree; i >= 0; i--) {
            if (this.coefficients[i] != other.coefficients[i]) {
                return false;
            }
        }
        return true;
    }

    public double evaluate(final double x) {
        double result = 0.0;
        for (int i = this.degree; i >= 0; i--) {
            result += this.coefficients[i] * Math.pow(x, i);
        }
        return result;
    }

    public Polynomial differentiate() {
        if (this.degree == 0) {
            return new Polynomial(0, 0);
        }
        final Polynomial deriv = new Polynomial(0.0, this.degree - 1);
        deriv.degree = this.degree - 1;
        for (int i = 0; i < this.degree; i++) {
            deriv.coefficients[i] = ((i) + 1.0) * this.coefficients[i + 1];
        }
        return deriv;
    }

    public Polynomial integrate() {
        final Polynomial integral = new Polynomial(0.0, this.degree + 1);
        integral.degree = this.degree + 1;
        for (int i = 0; i <= this.degree; i++) {
            integral.coefficients[i + 1] = this.coefficients[i] / ((i) + 1.0);
        }
        return integral;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.degree == 0) {
            return sb.append(this.coefficients[0]).toString();
        }
        if (this.degree == 1) {
            return sb.append(this.coefficients[1]).append("x + ").append(this.coefficients[0]).toString();
        }
        sb.append(this.coefficients[this.degree]).append("x^").append(this.degree);
        for (int i = this.degree - 1; i >= 0; i--) {
            if (this.coefficients[i] == 0.0) {
                continue;
            }
            else if (this.coefficients[i] > 0.0) {
                sb.append(" + ").append(this.coefficients[i]);
            }
            else if (this.coefficients[i] < 0.0) {
                sb.append(" - ").append(-this.coefficients[i]);
            }
            if (i == 1) {
                sb.append("x");
            }
            else if (i > 1) {
                sb.append("x^").append(i);
            }
        }
        return sb.toString();
    }
}
