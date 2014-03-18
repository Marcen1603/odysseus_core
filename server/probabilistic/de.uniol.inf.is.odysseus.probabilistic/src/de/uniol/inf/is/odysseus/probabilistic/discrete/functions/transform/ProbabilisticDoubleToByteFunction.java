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
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticByte;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticDoubleToByteFunction extends AbstractProbabilisticFunction<ProbabilisticByte> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2574577913106674914L;
    /**
     * Accepted data types.
     */
    private static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE };

    public ProbabilisticDoubleToByteFunction() {
    	super("doubleToByte", 1, ACC_TYPES, SDFProbabilisticDatatype.PROBABILISTIC_BYTE);
    }
    
    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final ProbabilisticByte getValue() {
        final Map<Byte, Double> values = new HashMap<Byte, Double>();
        for (final Entry<?, Double> value : ((AbstractProbabilisticValue<?>) this.getInputValue(0)).getValues().entrySet()) {
            values.put(((Number) value.getKey()).byteValue(), value.getValue());
        }
        return new ProbabilisticByte(values);
    }

  
  

  
}
