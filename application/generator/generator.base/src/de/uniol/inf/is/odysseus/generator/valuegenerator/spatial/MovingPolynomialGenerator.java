/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.generator.valuegenerator.spatial;

import java.util.Objects;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractMultiValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MovingPolynomialGenerator extends AbstractMultiValueGenerator {
    private final double start;
    private final double increase;
    private double current;
    private final Polynomial polynomial;

    /**
     * @param errorModel
     */
    public MovingPolynomialGenerator(final IErrorModel errorModel, final Double[] coefficients) {
        this(errorModel, coefficients, 0.0, 1.0);
    }

    public MovingPolynomialGenerator(final IErrorModel errorModel, final Double[] coefficients, final double increase) {
        this(errorModel, coefficients, 0.0, increase);
    }

    public MovingPolynomialGenerator(final IErrorModel errorModel, final Double[] coefficients, final double start, final double increase) {
        super(errorModel);
        Objects.requireNonNull(coefficients);
        this.start = start;
        this.increase = increase;
        this.polynomial = new Polynomial(coefficients);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int dimension() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] generateValue() {
        final double[] value = new double[] { this.current, this.polynomial.evaluate(this.current) };
        this.current += this.increase;
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initGenerator() {
        this.current = this.start;
    }

    class Polynomial {
        /** The polynomial coefficients. */
        private final Double[] coefficients;
        /** The degree of the polynomial. */
        private final int degree;

        public Polynomial(final Double coefficient, final int degree) {
            Objects.requireNonNull(coefficient);
            this.coefficients = new Double[degree + 1];
            this.coefficients[degree] = coefficient;
            this.degree = this.degree();
        }

        public Polynomial(final Double[] coefficients) {
            Objects.requireNonNull(coefficients);
            this.coefficients = coefficients;
            this.degree = this.degree();
        }

        public final int degree() {
            int polynomialDegree = 0;
            for (int i = 0; i < this.coefficients.length; i++) {
                if (this.coefficients[i] != 0.0) {
                    polynomialDegree = i;
                }
            }
            return polynomialDegree;
        }

        public final double evaluate(final double x) {
            double result = 0.0;
            for (int i = this.degree; i >= 0; i--) {
                result += this.coefficients[i] * Math.pow(x, i);
            }
            return result;
        }
    }
}
