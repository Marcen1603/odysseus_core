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
public class PopulationStandardDeviationPartialAggregate<R> extends AbstractStandardDeviationPartialAggregate<R> {
    /**
     *
     */
    private static final long serialVersionUID = -2192065454186826354L;

    /**
     * Class constructor.
     *
     */
    public PopulationStandardDeviationPartialAggregate() {
        super();
    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public PopulationStandardDeviationPartialAggregate(final Number x) {
        super(x);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public PopulationStandardDeviationPartialAggregate(final PopulationStandardDeviationPartialAggregate<R> partialAggregate) {
        super(partialAggregate);
    }

    /**
     * @return The population standard deviation.
     */
    @Override
    public double getAggValue() {
        return FastMath.sqrt(this.m1 / this.n);
    }

    @Override
    public PopulationStandardDeviationPartialAggregate<R> clone() {
        return new PopulationStandardDeviationPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "PSTDDEV=" + this.getAggValue();
    }

}
