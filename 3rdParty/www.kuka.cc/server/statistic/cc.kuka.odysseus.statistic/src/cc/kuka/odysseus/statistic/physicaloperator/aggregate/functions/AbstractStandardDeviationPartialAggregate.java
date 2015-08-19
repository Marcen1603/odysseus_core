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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
abstract public class AbstractStandardDeviationPartialAggregate<R> extends AbstractPartialAggregate<R> {
    /**
     *
     */
    private static final long serialVersionUID = -999339582736993137L;
    protected double m1;
    protected double mean;
    protected int n;

    /**
     * Class constructor.
     *
     */
    public AbstractStandardDeviationPartialAggregate() {

    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public AbstractStandardDeviationPartialAggregate(final Number x) {
        this();
        this.add(x);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public AbstractStandardDeviationPartialAggregate(final AbstractStandardDeviationPartialAggregate<R> partialAggregate) {
        this.mean = partialAggregate.mean;
        this.m1 = partialAggregate.m1;
        this.n = partialAggregate.n;
    }

    /**
     * @return The aggregation value
     */
    abstract public double getAggValue();

    public void add(final Number x) {
        if (x != null) {
            // Estimate online variance value using
            // Donald E. Knuth (1998). The Art of Computer Programming, volume
            // 2:
            // Seminumerical Algorithms, 3rd edn., p. 232. Boston:
            // Addison-Wesley.
            this.n++;
            final double delta = x.doubleValue() - this.mean;
            this.mean += delta / this.n;
            this.m1 += delta * (x.doubleValue() - this.mean);
        }
    }

    /**
     * @param merge
     */
    public void add(final AbstractStandardDeviationPartialAggregate<?> merge) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "STDDEV=" + this.getAggValue();
    }

}
