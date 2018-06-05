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
public class JarqueBeraTestPartialAggregate<R> extends AbstractKurtosisPartialAggregate<R> {

    /**
     *
     */
    private static final long serialVersionUID = 5791895280909539185L;

    /**
     * Class constructor.
     *
     */
    public JarqueBeraTestPartialAggregate() {
        super();
    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public JarqueBeraTestPartialAggregate(final Number x) {
        super(x);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public JarqueBeraTestPartialAggregate(final JarqueBeraTestPartialAggregate<R> partialAggregate) {
        super(partialAggregate);
    }

    /**
     * @return The p-Value of the Jarque–Bera test
     */
    @Override
    public double getAggValue() {
        final double skewness = this.m3 / this.n / FastMath.pow(this.m2 / this.n, 3.0 / 2.0);
        final double kurtosis = ((this.m4 / this.n) / FastMath.pow(this.m2 / this.n, 2.0)) - 3.0;
        final double jb = (this.n / 6.0) * (FastMath.pow(skewness, 2.0) + ((1.0 / 4.0) * FastMath.pow(kurtosis, 2.0)));
        final double chiSquared = 1.0 - FastMath.exp(-jb / 2.0);
        return 1.0 - chiSquared;
    }

    /**
     * @param merge
     */
    public void add(final JarqueBeraTestPartialAggregate<?> merge) {
        // TODO Auto-generated method stub

    }

    @Override
    public JarqueBeraTestPartialAggregate<R> clone() {
        return new JarqueBeraTestPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "JARQUE=" + this.getAggValue();
    }

    public static void main(final String[] args) {
        final JarqueBeraTestPartialAggregate<?> agg = new JarqueBeraTestPartialAggregate<>();
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

        assert (agg.getAggValue() == 0.78945511727273);
        System.out.println(agg + " = 0.78945511727273");
    }

}
