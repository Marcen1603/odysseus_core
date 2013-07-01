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
package de.uniol.inf.is.odysseus.core.server.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * Returns a new string that is a substring of the value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SubStringFunction extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long            serialVersionUID = 2270358376473789092L;
    private static final SDFDatatype[][] accTypes         = new SDFDatatype[][] { { SDFDatatype.STRING },
            { SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG },
            { SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG } };

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): a string and the begin and end index");
        }
        return accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "substring";
    }

    @Override
    public String getValue() {
        return ((String) getInputValue(0)).substring(getNumericalInputValue(1).intValue(), getNumericalInputValue(2)
                .intValue());
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.STRING;
    }
}
