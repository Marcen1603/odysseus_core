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
package cc.kuka.odysseus.interval.function.transform;

import cc.kuka.odysseus.interval.datatype.Interval;
import cc.kuka.odysseus.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ToIntervalFunction<T extends Number> extends AbstractFunction<Interval<T>> {
    /**
     *
     */
    private static final long serialVersionUID = 8339004565543022768L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public ToIntervalFunction() {
        super("toInterval", 2, ToIntervalFunction.accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Interval<T> getValue() {
        final T a = (T) this.getInputValue(0);
        final T b = (T) this.getInputValue(1);
        return new Interval<>(a, b);
    }

    @Override
    public boolean determineTypeFromInput() {
        return true;
    }

    @Override
    public SDFDatatype determineType(final IMepExpression<?>[] args) {
        if (args[0].getReturnType().equals(SDFDatatype.DOUBLE)) {
            return SDFIntervalDatatype.INTERVAL_DOUBLE;
        }
        else if (args[0].getReturnType().equals(SDFDatatype.FLOAT)) {
            return SDFIntervalDatatype.INTERVAL_FLOAT;
        }
        else if (args[0].getReturnType().equals(SDFDatatype.BYTE)) {
            return SDFIntervalDatatype.INTERVAL_BYTE;
        }
        else if (args[0].getReturnType().equals(SDFDatatype.SHORT)) {
            return SDFIntervalDatatype.INTERVAL_SHORT;
        }
        else if (args[0].getReturnType().equals(SDFDatatype.INTEGER)) {
            return SDFIntervalDatatype.INTERVAL_INTEGER;
        }
        else if (args[0].getReturnType().equals(SDFDatatype.LONG)) {
            return SDFIntervalDatatype.INTERVAL_LONG;
        }
        return null;
    }
}
