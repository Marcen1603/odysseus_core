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
package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryNumberInputFunction;

/**
 * Returns Euler's number e raised to the power of a double value
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ExpFunction extends AbstractUnaryNumberInputFunction<Double> {

    private static final long          serialVersionUID = -3546404170264475748L;
    
    public ExpFunction() {
    	super("exp", SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        return Math.exp(getNumericalInputValue(0));
    }

}
