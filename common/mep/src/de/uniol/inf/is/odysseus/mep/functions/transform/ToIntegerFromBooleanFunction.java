/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryBooleanInputFunction;

/**
 * Converts a given value to an integer value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @author Marco Grawunder 
 */
public class ToIntegerFromBooleanFunction extends AbstractUnaryBooleanInputFunction<Integer> {

    private static final long serialVersionUID = 2799997996073155068L;

    public ToIntegerFromBooleanFunction() {
        super("toInteger", SDFDatatype.INTEGER);
    }

    @Override
    public Integer getValue() {
        String s = getInputValue(0).toString();
        if (s.equalsIgnoreCase("true")) {
            return new Integer(1);
        }
        return new Integer(0);
    }
}
