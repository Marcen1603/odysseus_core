/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.polynomial.datatype;

import java.io.Serializable;
import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Polynomial implements Serializable, IClone {
    /**
     * 
     */
    private static final long serialVersionUID = 2908275730079940770L;
    /** The polynomial coefficients. */
    private final double[] coefficients;
    /** The degree of the polynomial. */
    private int degree;

    /**
     * Creates a new polynomial with the given coefficient.
     *
     * @param coefficient
     *            The polynomial coefficient
     * @param degree
     *            The degree of the polynomial
     */
    public Polynomial(final double coefficient, final int degree) {
        this.coefficients = new double[degree + 1];
        this.coefficients[degree] = coefficient;
        this.degree = this.degree();
    }

    /**
     * Creates a new polynomial with the given coefficients.
     *
     * @param coefficients
     *            The polynomial coefficients
     */
    public Polynomial(final double[] coefficients) {
        this.coefficients = coefficients.clone();
        this.degree = this.degree();
    }

    /**
     * Class constructor.
     *
     * @param clone
     */
    public Polynomial(Polynomial clone) {
        this.coefficients = clone.coefficients.clone();
        this.degree = clone.degree;
    }
    

    /**
     * Gets the degree of the polynomial.
     *
     * @return The degree of the polynomial
     */
    public final int degree() {
        int polynomialDegree = 0;
        for (int i = 0; i < this.coefficients.length; i++) {
            if (this.coefficients[i] != 0.0) {
                polynomialDegree = i;
            }
        }
        return polynomialDegree;
    }

    /**
     * Gets the coefficient of the given degree.
     *
     * @param coefficientDegree
     *            The degree of the coefficient
     * @return The coefficient of the given degree
     */
    public final double coefficient(final int coefficientDegree) {
        if (coefficientDegree < this.coefficients.length) {
            return this.coefficients[coefficientDegree];
        }
        return 0.0;
    }

    /**
     * Adds the given polynomial to this polynomial and returns the new
     * polynomial.
     *
     * @param other
     *            The other polynomial
     * @return The result of the operation
     */
    public final Polynomial add(final Polynomial other) {
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

    /**
     * Subtracts the given polynomial from this polynomial and returns the new
     * polynomial.
     *
     * @param other
     *            The other polynomial
     * @return The result of the operation
     */
    public final Polynomial substract(final Polynomial other) {
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

    /**
     * Multiplies the given polynomial with this polynomial and returns the new
     * polynomial.
     *
     * @param other
     *            The other polynomial
     * @return The result of the operation
     */
    public final Polynomial multiply(final Polynomial other) {
        final Polynomial result = new Polynomial(0.0, this.degree + other.degree);
        for (int i = 0; i <= this.degree; i++) {
            for (int j = 0; j <= other.degree; j++) {
                result.coefficients[i + j] += (this.coefficients[i] * other.coefficients[j]);
            }
        }
        result.degree = result.degree();
        return result;
    }

    /**
     * Compose the given polynomial with this polynomial and returns the new
     * polynomial.
     *
     * @param other
     *            The other polynomial
     * @return The result of the operation
     */
    public final Polynomial compose(final Polynomial other) {
        Polynomial result = new Polynomial(0.0, 0);
        for (int i = this.degree; i >= 0; i--) {
            final Polynomial term = new Polynomial(this.coefficients[i], 0);
            result = term.add(other.multiply(result));
        }
        return result;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + Arrays.hashCode(this.coefficients);
        result = (prime * result) + this.degree;
        return result;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
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

    /**
     * Evaluates this polynomial with the given value.
     *
     * @param x
     *            The value
     * @return The result of the operation
     */
    public final double evaluate(final double x) {
        double result = 0.0;
        for (int i = this.degree; i >= 0; i--) {
            result += this.coefficients[i] * Math.pow(x, i);
        }
        return result;
    }

    /**
     * Differentiate this polynomial and return the new polynomial.
     *
     * @return The differentiated polynomial
     */
    public final Polynomial differentiate() {
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

    /**
     * Integrate this polynomial and return the new polynomial.
     *
     * @return The integrated polynomial
     */
    public final Polynomial integrate() {
        final Polynomial integral = new Polynomial(0.0, this.degree + 1);
        integral.degree = this.degree + 1;
        for (int i = 0; i <= this.degree; i++) {
            integral.coefficients[i + 1] = this.coefficients[i] / ((i) + 1.0);
        }
        return integral;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Polynomial clone() {
        return new Polynomial(this);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.degree == 0) {
            return sb.append(this.coefficients[0]).toString();
        }
        if (this.degree == 1) {
            if (this.coefficients[0] > 0.0) {
                return sb.append(this.coefficients[1]).append("x + ").append(this.coefficients[0]).toString();
            }
            else if (this.coefficients[0] < 0.0) {
                return sb.append(this.coefficients[1]).append("x - ").append(-this.coefficients[0]).toString();
            }
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
