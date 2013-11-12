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
package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticPowerOperator extends AbstractProbabilisticBinaryOperator<ProbabilisticDouble> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4646698914179052402L;

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getPrecedence()
     */
    @Override
    public final int getPrecedence() {
        return 1;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
     */
    @Override
    public final String getSymbol() {
        return "^";
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final ProbabilisticDouble getValue() {
        final AbstractProbabilisticValue<?> a = this.getInputValue(0);
        final double b = this.getNumericalInputValue(1);
        return this.getValueInternal(a, b);
    }

    /**
     * Calculates the probabilistic of the first argument raised to the power of
     * the second argument.
     * 
     * @param a
     *            The probabilistic value
     * @param b
     *            The value
     * @return The probabilistic value a^b
     */
    protected final ProbabilisticDouble getValueInternal(final AbstractProbabilisticValue<?> a, final double b) {
        final Map<Double, Double> values = new HashMap<Double, Double>(a.getValues().size());
        for (final Entry<?, Double> aEntry : a.getValues().entrySet()) {
            final double value = FastMath.pow(((Number) aEntry.getKey()).doubleValue(), b);
            // Does the value already exists in the map, i.e., a^0
            if (values.containsKey(value)) {
                values.put(value, values.get(value) + aEntry.getValue());
            }
            else {
                values.put(value, aEntry.getValue());
            }
        }
        return new ProbabilisticDouble(values);
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
     */
    @Override
    public final SDFDatatype getReturnType() {
        return SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isCommutative()
     */
    @Override
    public final boolean isCommutative() {
        return false;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isAssociative()
     */
    @Override
    public final boolean isAssociative() {
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#
     * isLeftDistributiveWith
     * (de.uniol.inf.is.odysseus.core.server.mep.IOperator)
     */
    @Override
    public final boolean isLeftDistributiveWith(final IOperator<ProbabilisticDouble> operator) {
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#
     * isRightDistributiveWith
     * (de.uniol.inf.is.odysseus.core.server.mep.IOperator)
     */
    @Override
    public final boolean isRightDistributiveWith(final IOperator<ProbabilisticDouble> operator) {
        return false;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.mep.IOperator#getAssociativity()
     */
    @Override
    public final de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return null;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
            { SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
                    SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG }, SDFDatatype.NUMBERS };

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
     */
    @Override
    public final SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > (this.getArity() - 1)) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return ProbabilisticPowerOperator.ACC_TYPES[argPos];
    }

}
