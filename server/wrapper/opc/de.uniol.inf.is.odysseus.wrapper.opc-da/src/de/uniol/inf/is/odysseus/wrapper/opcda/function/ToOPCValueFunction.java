/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.opcda.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.datatype.OPCValue;
import de.uniol.inf.is.odysseus.wrapper.opcda.sdf.schema.SDFOPCDADatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ToOPCValueFunction extends AbstractFunction<OPCValue> {

    /**
     * 
     */
    private static final long serialVersionUID = 5738218883056123862L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public ToOPCValueFunction() {
        super("ToOPCValue", 4, accTypes, SDFOPCDADatatype.OPCVALUE);
    }

    @Override
    public OPCValue getValue() {
        long timestamp = getNumericalInputValue(0).longValue();
        double value = getNumericalInputValue(1).doubleValue();
        short quality = getNumericalInputValue(2).shortValue();
        int error = getNumericalInputValue(3).intValue();
        return new OPCValue(timestamp, value, quality, error);
    }
}
