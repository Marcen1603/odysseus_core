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
public class DAgostinoPearsonOmnibusTestPartialAggregate<R> extends AbstractKurtosisPartialAggregate<R> {

    /**
     *
     */
    private static final long serialVersionUID = 2218192769130999890L;

    /**
     * Class constructor.
     *
     */
    public DAgostinoPearsonOmnibusTestPartialAggregate() {
        super();
    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public DAgostinoPearsonOmnibusTestPartialAggregate(final double x) {
        super(x);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public DAgostinoPearsonOmnibusTestPartialAggregate(final DAgostinoPearsonOmnibusTestPartialAggregate<R> partialAggregate) {
        super(partialAggregate);
    }

    /**
     * @return The p-Value of the D'Agostino-Perason omnibus test
     */
    @Override
    public double getAggValue() {
        final double skewness = ((FastMath.sqrt(this.n * (this.n - 1.0)) / (this.n - 2.0)) * (this.m3 / this.n)) / FastMath.pow(this.m2 / this.n, 3.0 / 2.0);
        final double kurtosis = ((this.n - 1.0) / ((this.n - 2.0) * (this.n - 3.0))) * (((this.n + 1.0) * (((this.m4 / this.n) / FastMath.pow(this.m2 / this.n, 2.0)) - 3.0)) + 6.0);

        // Standard error of skewness (SES)
        final double ses = FastMath.sqrt((6.0 * this.n * (this.n - 1.0)) / ((this.n - 2.0) * (this.n + 1.0) * (this.n + 3.0)));
        // Standard error of kurtisos (SEK)
        final double sek = 2.0 * ses * FastMath.sqrt(((this.n * this.n) - 1.0) / (((this.n - 3.0) * (this.n + 5.0))));

        final double zSkewness = skewness / ses;
        final double zKurtosis = kurtosis / sek;

        final double dp = FastMath.pow(zSkewness, 2.0) + FastMath.pow(zKurtosis, 2.0);
        final double chiSquared = 1.0 - FastMath.exp(-dp / 2.0);

        return 1.0 - chiSquared;
    }

    /**
     * @param merge
     */
    public void add(final DAgostinoPearsonOmnibusTestPartialAggregate<?> merge) {
        // TODO Auto-generated method stub

    }

    @Override
    public DAgostinoPearsonOmnibusTestPartialAggregate<R> clone() {
        return new DAgostinoPearsonOmnibusTestPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "DPO=" + this.getAggValue();
    }

    public static void main(final String[] args) {
        final DAgostinoPearsonOmnibusTestPartialAggregate<?> agg = new DAgostinoPearsonOmnibusTestPartialAggregate<>();
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

        assert (agg.getAggValue() == 0.81949515416189);
        System.out.println(agg + " = 0.81949515416189");
    }

}
