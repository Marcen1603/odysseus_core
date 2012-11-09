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
package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * Returns the value of the specified number as a <code>short</code>.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class DoubleToShortFunction extends AbstractFunction<Short> {

    /**
     * 
     */
    private static final long serialVersionUID = 6684569578991865699L;

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public String getSymbol() {
        return "doubleToShort";
    }

    @Override
    public Short getValue() {
        return getNumericalInputValue(0).shortValue();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.SHORT;
    }

    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DOUBLE };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > 0) {
            throw new IllegalArgumentException("doubleToShort has only 1 argument.");
        }
        return accTypes;
    }
}
