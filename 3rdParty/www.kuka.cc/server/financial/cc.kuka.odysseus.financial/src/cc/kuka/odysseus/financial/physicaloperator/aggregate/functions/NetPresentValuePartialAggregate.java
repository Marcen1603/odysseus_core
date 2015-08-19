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
package cc.kuka.odysseus.financial.physicaloperator.aggregate.functions;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class NetPresentValuePartialAggregate<R> extends AbstractPartialAggregate<R> {
    /**
     *
     */
    private static final long serialVersionUID = -2619610700601245050L;
    private double sum;
    private int n;

    /**
     * Class constructor.
     *
     */
    public NetPresentValuePartialAggregate() {
        super();
    }

    /**
     * Class constructor.
     *
     * @param x
     */
    public NetPresentValuePartialAggregate(final Number x, final Number r) {
        this();
        this.add(x, r);
    }

    /**
     * Class constructor.
     *
     * @param partialAggregate
     */
    public NetPresentValuePartialAggregate(final NetPresentValuePartialAggregate<R> partialAggregate) {
        this.sum = partialAggregate.sum;
        this.n = partialAggregate.n;
    }

    /**
     * @return The net present value
     */
    public double getAggValue() {
        return this.sum;
    }

    public void add(final Number x, final Number r) {
        if ((x != null) && (r != null)) {
            this.sum += (x.doubleValue() / FastMath.pow(1.0 + r.doubleValue(), this.n));
            this.n++;
        }
    }

    public void add(final NetPresentValuePartialAggregate<?> value) {
        // TODO Implement merge
    }

    @Override
    public NetPresentValuePartialAggregate<R> clone() {
        return new NetPresentValuePartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "NPV=" + this.getAggValue();
    }

    public static void main(final String[] args) {
        final NetPresentValuePartialAggregate<?> agg = new NetPresentValuePartialAggregate<>();
        agg.add(-500000.0, 0.1);
        agg.add(200000.0, 0.1);
        agg.add(300000.0, 0.1);
        agg.add(200000.0, 0.1);

        assert (agg.getAggValue() == 80015.0262960179);
        System.out.println(agg + " = 80015.0262960179");
    }
}
