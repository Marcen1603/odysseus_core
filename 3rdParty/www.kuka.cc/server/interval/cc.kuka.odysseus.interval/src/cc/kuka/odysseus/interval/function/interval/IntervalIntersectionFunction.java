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
public class IntervalIntersectionFunction<T extends Number> extends AbstractFunction<Interval<T>> {
    /**
     *
     */
    private static final long serialVersionUID = 5436861306816764016L;

    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFIntervalDatatype.TYPES, SDFIntervalDatatype.TYPES };

    public IntervalIntersectionFunction() {
        super("intersection", 2, IntervalIntersectionFunction.accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Interval<T> getValue() {
        final Interval<T> a = this.getInputValue(0);
        final Interval<T> b = this.getInputValue(1);
        final T max = a.inf().doubleValue() >= b.inf().doubleValue() ? a.inf() : b.inf();
        final T min = a.sup().doubleValue() <= b.sup().doubleValue() ? a.sup() : b.sup();
        if (max.doubleValue() <= min.doubleValue()) {
            return new Interval<>(max, min, false);
        }
        return null;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean determineTypeFromInput() {
        return true;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public SDFDatatype determineType(final IMepExpression<?>[] args) {
        return args[0].getReturnType();
    }
}
