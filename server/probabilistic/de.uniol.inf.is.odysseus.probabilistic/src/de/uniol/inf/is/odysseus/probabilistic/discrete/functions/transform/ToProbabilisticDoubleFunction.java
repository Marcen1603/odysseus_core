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
package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.transform;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToProbabilisticDoubleFunction extends AbstractFunction<ProbabilisticDouble> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 820724413847770649L;

    
    public ToProbabilisticDoubleFunction() {
    	super("toProbabilisticDouble",1,ACC_TYPES,SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
    }
   

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final ProbabilisticDouble getValue() {
        final double[][] values = (double[][]) this.getInputValue(0);
        Objects.requireNonNull(values);
        Preconditions.checkArgument(values.length > 0);
        Preconditions.checkArgument(values[0].length == 2);
        final Map<Double, Double> valueMap = new HashMap<Double, Double>();
        for (final double[] value : values) {
            valueMap.put(value[0], value[1]);
        }
        return new ProbabilisticDouble(valueMap);
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE };

}
