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
abstract public class AbstractSkewnessPartialAggregate<R> extends AbstractPartialAggregate<R> {
    /**
     *
     */
    private static final long serialVersionUID = -6455916345169845411L;
    protected double m3;
    protected double m2;
    protected double mean;
    protected int n;

    /**
     * Class constructor.
     *
     */
    public AbstractSkewnessPartialAggregate() {
        this.m3 = 0.0;
        this.m2 = 0.0;
        this.mean = 0.0;
        this.n = 0;
    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public AbstractSkewnessPartialAggregate(final Number x) {
        this();
        this.add(x);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public AbstractSkewnessPartialAggregate(final AbstractSkewnessPartialAggregate<R> partialAggregate) {
        this.m3 = partialAggregate.m3;
        this.m2 = partialAggregate.m2;
        this.mean = partialAggregate.mean;
        this.n = partialAggregate.n;
    }

    /**
     * @return The aggregation value
     */
    abstract public double getAggValue();

    public void add(final Number x) {
        if (x != null) {
            this.n++;
            final double delta = x.doubleValue() - this.mean;
            final double deltaN = delta / this.n;
            final double term = delta * deltaN * (this.n - 1.0);
            this.mean += deltaN;
            this.m3 += (term * deltaN * (this.n - 2.0)) - (3.0 * deltaN * this.m2);
            this.m2 += term;
        }
    }

    /**
     * @param merge
     */
    public void add(final AbstractSkewnessPartialAggregate<?> merge) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "SKEW=" + this.getAggValue();
    }

}
