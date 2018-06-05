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
package cc.kuka.odysseus.interval.function.interval;

import cc.kuka.odysseus.interval.datatype.Interval;
import cc.kuka.odysseus.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class IntervalUnionFunction<T extends Number> extends AbstractFunction<Interval<T>> {
    /**
     *
     */
    private static final long serialVersionUID = 6741913271850798303L;

    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFIntervalDatatype.TYPES, SDFIntervalDatatype.TYPES };

    public IntervalUnionFunction() {
        super("union", 2, IntervalUnionFunction.accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
    }

    @Override
    public Interval<T> getValue() {
        final Interval<T> a = this.getInputValue(0);
        final Interval<T> b = this.getInputValue(1);

        final T max;
        final T min;
        final boolean leftOpen;
        final boolean rightOpen;
        if (a.sup().doubleValue() >= b.sup().doubleValue()) {
            max = a.sup();
            rightOpen = a.isRightOpen();
        }
        else {
            max = b.sup();
            rightOpen = b.isRightOpen();
        }
        if (a.inf().doubleValue() <= b.inf().doubleValue()) {
            min = a.inf();
            leftOpen = a.isLeftOpen();
        }
        else {
            min = b.inf();
            leftOpen = b.isLeftOpen();
        }

        return new Interval<>(min, max, leftOpen, rightOpen);
    }

    @Override
    public boolean determineTypeFromInput() {
        return true;
    }

    @Override
    public SDFDatatype determineType(final IMepExpression<?>[] args) {
        return args[0].getReturnType();
    }
}
