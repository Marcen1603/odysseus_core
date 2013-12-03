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

/**
 * Returns the angle theta from the conversion of rectangular coordinates (x, y)
 * to polar coordinates (r, theta)
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ArcTangens2Function extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long            serialVersionUID = 1824826421970268927L;
    private static final SDFDatatype[][] accTypes         = new SDFDatatype[][] {
            { SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG },
            { SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG } };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): y the ordinate coordinate and x the abscissa coordinate");
        }
        return accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "atan2";
    }

    @Override
    public Double getValue() {
        return Math.atan2(getNumericalInputValue(0), getNumericalInputValue(1));
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DOUBLE;
    }

}
