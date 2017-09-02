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
 * Greater operator for continuous probabilistic values.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ProbabilisticGreaterOperatorInverse extends AbstractProbabilisticCompareOperator {

    /**
     *
     */
    private static final long serialVersionUID = -1919756382879505534L;
    private final boolean leftInclusive;
    private final boolean rightInclusive;

    public ProbabilisticGreaterOperatorInverse() {
        this(">", true, false);
    }

    protected ProbabilisticGreaterOperatorInverse(final String symbol, final boolean leftInclusive, final boolean rightInclusive) {
        super(symbol, ProbabilisticGreaterOperatorInverse.ACC_TYPES);
        this.leftInclusive = leftInclusive;
        this.rightInclusive = rightInclusive;
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
        final double[] upperBound = new double[a.getDimension()];
        Arrays.fill(upperBound, Double.POSITIVE_INFINITY);
        upperBound[a.getDimension(pos)] = b;

        return this.getValueInternal(a, lowerBound, upperBound, this.leftInclusive, this.rightInclusive);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int getPrecedence() {
        return 8;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

}
