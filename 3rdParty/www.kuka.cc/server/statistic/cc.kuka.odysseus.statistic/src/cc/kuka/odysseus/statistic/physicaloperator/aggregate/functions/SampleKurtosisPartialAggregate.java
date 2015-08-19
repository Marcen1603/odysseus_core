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
public class SampleKurtosisPartialAggregate<R> extends AbstractKurtosisPartialAggregate<R> {
    /**
     *
     */
    private static final long serialVersionUID = -7041462697650412977L;

    /**
     * Class constructor.
     *
     */
    public SampleKurtosisPartialAggregate() {
        super();
    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public SampleKurtosisPartialAggregate(final Number x) {
        super(x);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public SampleKurtosisPartialAggregate(final SampleKurtosisPartialAggregate<R> partialAggregate) {
        super(partialAggregate);
    }

    /**
     * @return The sample kurtosis
     */
    @Override
    public double getAggValue() {
        return ((this.n - 1.0) / ((this.n - 2.0) * (this.n - 3.0))) * (((this.n + 1.0) * (((this.m4 / this.n) / FastMath.pow(this.m2 / this.n, 2.0)) - 3.0)) + 6.0);
    }

    @Override
    public SampleKurtosisPartialAggregate<R> clone() {
        return new SampleKurtosisPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "SKURT=" + this.getAggValue();
    }

    public static void main(final String[] args) {
        final SampleKurtosisPartialAggregate<?> agg = new SampleKurtosisPartialAggregate<>();
        for (int i = 0; i < 5; i++) {
            agg.add(61.0);
        }
        for (int i = 0; i < 18; i++) {
            agg.add(64.0);
        }
        for (int i = 0; i < 42; i++) {
            agg.add(67.0);
        }
        for (int i = 0; i < 27; i++) {
            agg.add(70.0);
        }
        for (int i = 0; i < 8; i++) {
            agg.add(73.0);
        }

        assert (agg.getAggValue() == -0.20914707275114);
        System.out.println(agg + " = -0.20914707275114");
    }
}
