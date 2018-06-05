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
package cc.kuka.odysseus.statistic.physicaloperator.aggregate.functions;

import org.apache.commons.math3.util.FastMath;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SampleStandardDeviationPartialAggregate<R> extends AbstractStandardDeviationPartialAggregate<R> {
    /**
     *
     */
    private static final long serialVersionUID = -3398124042034586787L;

    /**
     * Class constructor.
     *
     */
    public SampleStandardDeviationPartialAggregate() {
        super();
    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public SampleStandardDeviationPartialAggregate(final Number x) {
        super(x);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public SampleStandardDeviationPartialAggregate(final SampleStandardDeviationPartialAggregate<R> partialAggregate) {
        super(partialAggregate);
    }

    /**
     * @return The sample standard deviation
     */
    @Override
    public double getAggValue() {
        return FastMath.sqrt(this.m1 / (this.n - 1.0));

    }

    @Override
    public SampleStandardDeviationPartialAggregate<R> clone() {
        return new SampleStandardDeviationPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "SSTDDEV=" + this.getAggValue();
    }

}
