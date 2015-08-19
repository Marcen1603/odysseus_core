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
package cc.kuka.odysseus.polynomial.physicaloperator.aggregate.functions;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import cc.kuka.odysseus.polynomial.datatype.Polynomial;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class PolynomialRegressionPartialAggregate<R> extends AbstractPartialAggregate<R> {
    /**
     *
     */
    private static final long serialVersionUID = 1655107210615646688L;
    private final SimpleRegression regression;

    /**
     * Class constructor.
     *
     */
    public PolynomialRegressionPartialAggregate() {
        this.regression = new SimpleRegression();
    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public PolynomialRegressionPartialAggregate(final Number x, final Number y) {
        this();
        this.add(x, y);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public PolynomialRegressionPartialAggregate(final PolynomialRegressionPartialAggregate<R> partialAggregate) {
        this();
        this.add(partialAggregate);
    }

    /**
     * @return The estimated polynomial
     */

    public Polynomial getAggValue() {
        if (this.regression.getN() >= 2) {
            try {
                return new Polynomial(this.regression.regress().getParameterEstimates());
            }
            catch (Throwable e) {
                return new Polynomial(new double[] { Double.NaN });
            }
        }
        return new Polynomial(new double[] { Double.NaN });
    }

    public void add(final Number x, final Number y) {
        if ((x != null) && (y != null)) {
            this.regression.addData(x.doubleValue(), y.doubleValue());
        }
    }

    /**
     * @param merge
     */
    public void add(final PolynomialRegressionPartialAggregate<?> merge) {
        // this.regression.append(merge.regression);
    }

    @Override
    public PolynomialRegressionPartialAggregate<R> clone() {
        return new PolynomialRegressionPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "REGRESSION=" + this.getAggValue();
    }

    public static void main(final String[] args) {
        final PolynomialRegressionPartialAggregate<?> agg = new PolynomialRegressionPartialAggregate<>();
        for (int i = 0; i < 5; i++) {
            agg.add(61.0, 0.3);
        }
        for (int i = 0; i < 18; i++) {
            agg.add(64.0, 0.5);
        }
        for (int i = 0; i < 42; i++) {
            agg.add(67.0, 0.7);
        }
        for (int i = 0; i < 27; i++) {
            agg.add(70.0, 0.9);
        }
        for (int i = 0; i < 8; i++) {
            agg.add(73.0, 0.3);
        }

        // assert (agg.getAggValue() == 0.78945511727273);
        System.out.println(agg + " = 0.78945511727273");
    }

}
