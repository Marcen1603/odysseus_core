/**
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
package de.uniol.inf.is.odysseus.probabilistic.functions.compare;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Smaller operator for continuous probabilistic values.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ProbabilisticSmallerOperatorInverse extends AbstractProbabilisticCompareOperator {

    /**
     *
     */
    private static final long serialVersionUID = 69720551268314998L;
    private final boolean inclusive;

    public ProbabilisticSmallerOperatorInverse() {
        this("<", false);
    }

    public ProbabilisticSmallerOperatorInverse(final String symbol, final boolean inclusive) {
        super(symbol, ProbabilisticSmallerOperatorInverse.ACC_TYPES);
        this.inclusive = inclusive;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final int getPrecedence() {
        return 8;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final ProbabilisticBooleanResult getValue() {
        final MultivariateMixtureDistribution a = ((MultivariateMixtureDistribution) this.getInputValue(1)).clone();
        final int pos = getInputPosition(1);
        final Double b = getNumericalInputValue(0);
        final double[] lowerBound = new double[a.getDimension()];
        Arrays.fill(lowerBound, Double.NEGATIVE_INFINITY);
        if (!inclusive) {
            lowerBound[a.getDimension(pos)] = b + Double.MIN_VALUE;

        } else {
            lowerBound[a.getDimension(pos)] = b;
        }
        final double[] upperBound = new double[a.getDimension()];
        Arrays.fill(upperBound, Double.POSITIVE_INFINITY);

        return this.getValueInternal(a, lowerBound, upperBound);
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

}
