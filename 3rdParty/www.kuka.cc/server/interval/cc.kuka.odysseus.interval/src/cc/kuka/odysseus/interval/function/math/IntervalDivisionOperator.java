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
public class IntervalDivisionOperator extends AbstractBinaryOperator<Interval<Double>> {

    /**
     *
     */
    private static final long serialVersionUID = -3502588472297137429L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFIntervalDatatype.TYPES, SDFIntervalDatatype.TYPES };

    public IntervalDivisionOperator() {
        super("/", IntervalDivisionOperator.accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
    }

    @Override
    public int getPrecedence() {
        return 5;
    }

    @Override
    public Interval<Double> getValue() {
        final Interval<?> a = this.getInputValue(0);
        final Interval<Double> b = this.getInputValue(1);
        return this.getValueInternal(a, b);
    }

    protected Interval<Double> getValueInternal(final Interval<?> a, final Interval<?> b) {
        final double aInf = a.inf().doubleValue();
        final double aSup = a.sup().doubleValue();
        final double bInf = b.inf().doubleValue();
        final double bSup = b.sup().doubleValue();
        if ((bInf == 0.0) && (bSup == 0.0)) {
            return new Interval<>(new Double(Double.NaN), new Double(Double.NaN));
        }
        else if (0.0 <= bInf) {
            final double inf = Math.min(Math.min(IntervalDivisionOperator.divide(aInf, bInf), IntervalDivisionOperator.divide(aInf, bSup)),
                    Math.min(IntervalDivisionOperator.divide(aSup, bInf), IntervalDivisionOperator.divide(aSup, bSup)));
            final double sup = Math.max(Math.max(IntervalDivisionOperator.divide(aInf, bInf), IntervalDivisionOperator.divide(aInf, bSup)),
                    Math.max(IntervalDivisionOperator.divide(aSup, bInf), IntervalDivisionOperator.divide(aSup, bSup)));
            return new Interval<>(new Double(inf), new Double(sup));
        }
        else if (bSup <= 0.0) {
            return this.getValueInternal(new Interval<>(new Double(-aSup), new Double(-aInf)), new Interval<>(new Double(-bSup), new Double(-bInf)));
        }
        else {
            final Interval<Double> left = this.getValueInternal(a, new Interval<>(new Double(bInf), new Double(0.0)));
            final Interval<Double> right = this.getValueInternal(a, new Interval<>(new Double(0.0), new Double(bSup)));
            return new Interval<>(new Double(Math.min(left.inf().doubleValue(), right.inf().doubleValue())), new Double(Math.max(left.sup().doubleValue(), right.sup().doubleValue())));
        }
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean isAssociative() {
        return true;
    }

    @Override
    public boolean isLeftDistributiveWith(final IOperator<Interval<Double>> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<Interval<Double>> operator) {
        return false;
    }

    private final static double divide(final double a, final double b) {
        if ((Double.isInfinite(a)) && (b == 0.0)) {
            return 0.0;
        }
        else if ((a == 0.0) && (b == 0.0)) {
            return 0.0;
        }
        else if (Double.isInfinite(b)) {
            return 0.0;
        }
        else if ((a > 0.0) && (b == 0.0)) {
            return Double.POSITIVE_INFINITY;
        }
        else if ((a < 0.0) && (b == 0.0)) {
            return Double.NEGATIVE_INFINITY;
        }
        else {
            return a / b;
        }
    }

}
