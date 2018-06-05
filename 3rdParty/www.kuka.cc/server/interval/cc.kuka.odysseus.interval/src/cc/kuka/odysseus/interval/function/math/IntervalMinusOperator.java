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
package cc.kuka.odysseus.interval.function.math;

import cc.kuka.odysseus.interval.datatype.Interval;
import cc.kuka.odysseus.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class IntervalMinusOperator extends AbstractBinaryOperator<Interval<Double>> {

    /**
     *
     */
    private static final long serialVersionUID = 7665159518653289431L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFIntervalDatatype.TYPES, SDFIntervalDatatype.TYPES };

    public IntervalMinusOperator() {
        super("-", IntervalMinusOperator.accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
    }

    @Override
    public int getPrecedence() {
        return 6;
    }

    @Override
    public Interval<Double> getValue() {
        final Interval<?> a = this.getInputValue(0);
        final Interval<?> b = this.getInputValue(1);
        return IntervalMinusOperator.getValueInternal(a, b);
    }

    protected static Interval<Double> getValueInternal(final Interval<?> a, final Interval<?> b) {
        return new Interval<>(new Double(a.inf().doubleValue() - b.sup().doubleValue()), new Double(a.sup().doubleValue() - b.inf().doubleValue()), a.isLeftOpen() && b.isLeftOpen(),
                a.isRightOpen() && b.isRightOpen());
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(final IOperator<Interval<Double>> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<Interval<Double>> operator) {
        return false;
    }

}
