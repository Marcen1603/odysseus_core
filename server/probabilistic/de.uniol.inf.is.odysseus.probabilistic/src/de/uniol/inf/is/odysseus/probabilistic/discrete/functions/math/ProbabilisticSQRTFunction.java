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
import java.util.Objects;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticSQRTFunction extends AbstractProbabilisticFunction<ProbabilisticDouble> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1757085950523543990L;
    /**
     * Accepted data types.
     */
    private static final SDFDatatype[] ACC_TYPES = SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS;

    public ProbabilisticSQRTFunction() {
    	super("sqrt",1,ACC_TYPES,SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
	}
    
    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final ProbabilisticDouble getValue() {
        final AbstractProbabilisticValue<?> a = this.getInputValue(0);
        Objects.requireNonNull(a);
        return this.getValueInternal(a);
    }

    /**
     * Compute the square root of the given probabilistic value.
     * 
     * @param a
     *            The probabilistic value
     * @return The probabilistic square root
     */
    protected final ProbabilisticDouble getValueInternal(final AbstractProbabilisticValue<?> a) {
        final Map<Double, Double> values = new HashMap<Double, Double>(a.getValues().size());
        for (final Entry<?, Double> aEntry : a.getValues().entrySet()) {
            final double value = FastMath.sqrt(((Number) aEntry.getKey()).doubleValue());
            if (values.containsKey(value)) {
                values.put(value, values.get(value) + aEntry.getValue());
            }
            else {
                values.put(value, aEntry.getValue());
            }
        }
        return new ProbabilisticDouble(values);
    }


}
