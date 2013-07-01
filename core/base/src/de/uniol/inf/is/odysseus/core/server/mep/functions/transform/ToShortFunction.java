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
package de.uniol.inf.is.odysseus.core.server.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * Converts a given value to short
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToShortFunction extends AbstractFunction<Short> {

    /**
     * 
     */
    private static final long serialVersionUID = 2336013385553930997L;

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public String getSymbol() {
        return "toShort";
    }

    @Override
    public Short getValue() {
        String s = getInputValue(0).toString();
        if (s.equalsIgnoreCase("true")) {
            return 1;
        }
        else if (s.equalsIgnoreCase("false")) {
            return 0;
        }
        Double val = Double.parseDouble(getInputValue(0).toString());
        return val.shortValue();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.SHORT;
    }

    private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DOUBLE, SDFDatatype.SHORT,
            SDFDatatype.BYTE, SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.STRING,
            SDFDatatype.BOOLEAN                };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > 0) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return accTypes;
    }
}
