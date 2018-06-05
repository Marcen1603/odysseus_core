/*
 * Copyright 2013 The Odysseus Team
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

package de.uniol.inf.is.odysseus.datatype.interval.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalDivisionOperator extends AbstractBinaryOperator<IntervalDouble> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3502588472297137429L;

    public IntervalDivisionOperator() {
        super("/", accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
    }

    @Override
    public int getPrecedence() {
        return 5;
    }

    @Override
    public IntervalDouble getValue() {
        IntervalDouble a = getInputValue(0);
        IntervalDouble b = getInputValue(1);
        return getValueInternal(a, b);
    }

    protected IntervalDouble getValueInternal(IntervalDouble a, IntervalDouble b) {
        if ((b.inf() == 0.0) && (b.sup() == 0.0)) {
            return new IntervalDouble(Double.NaN, Double.NaN);
        }
        else if (0.0 <= b.inf()) {
            final double inf = Math.min(Math.min(divide(a.inf(), b.inf()), divide(a.inf(), b.sup())), Math.min(divide(a.sup(), b.inf()), divide(a.sup(), b.sup())));
            final double sup = Math.max(Math.max(divide(a.inf(), b.inf()), divide(a.inf(), b.sup())), Math.max(divide(a.sup(), b.inf()), divide(a.sup(), b.sup())));
            return new IntervalDouble(inf, sup);
        }
        else if (b.sup() <= 0.0) {
            return getValueInternal(new IntervalDouble(-a.sup(), -a.inf()), new IntervalDouble(-b.sup(), -b.inf()));
        }
        else {
            IntervalDouble left = getValueInternal(a, new IntervalDouble(b.inf(), 0.0));
            IntervalDouble right = getValueInternal(a, new IntervalDouble(0.0, b.sup()));
            return new IntervalDouble(Math.min(left.inf(), right.inf()), Math.max(left.sup(), right.sup()));
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
    public boolean isLeftDistributiveWith(IOperator<IntervalDouble> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<IntervalDouble> operator) {
        return false;
    }

    public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFIntervalDatatype.INTERVAL_BYTE, SDFIntervalDatatype.INTERVAL_SHORT, SDFIntervalDatatype.INTERVAL_INTEGER,
            SDFIntervalDatatype.INTERVAL_FLOAT, SDFIntervalDatatype.INTERVAL_DOUBLE, SDFIntervalDatatype.INTERVAL_LONG };
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {accTypes1, accTypes1 };

    private final double divide(final double a, final double b) {
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
