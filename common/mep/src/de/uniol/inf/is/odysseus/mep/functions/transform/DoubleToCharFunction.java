/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @deprecated Use {@link ToCharFromNumberFunction}
 */
@Deprecated
public class DoubleToCharFunction extends AbstractFunction<Character> {

    /**
     * 
     */
    private static final long serialVersionUID = -4926159873314027627L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.DOUBLE } };

    public DoubleToCharFunction() {
        super("doubleToChar", 1, accTypes, SDFDatatype.CHAR);
    }

    @Override
    public Character getValue() {
        return new Character((char) getNumericalInputValue(0).shortValue());
    }
}
