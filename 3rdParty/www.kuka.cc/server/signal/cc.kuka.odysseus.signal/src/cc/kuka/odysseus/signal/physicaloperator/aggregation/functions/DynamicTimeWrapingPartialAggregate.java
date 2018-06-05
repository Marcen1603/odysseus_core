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
package cc.kuka.odysseus.signal.physicaloperator.aggregation.functions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DynamicTimeWrapingPartialAggregate<R> extends AbstractPartialAggregate<R> {

    /**
     *
     */
    private static final long serialVersionUID = -274875223704849442L;
    private final List<Double> a = new LinkedList<>();
    private final List<Double> b = new LinkedList<>();

    public DynamicTimeWrapingPartialAggregate() {
    }

    public DynamicTimeWrapingPartialAggregate(final Number a, final Number b) {
        this.add(a, b);
    }

    public DynamicTimeWrapingPartialAggregate(final DynamicTimeWrapingPartialAggregate<R> partialAggregate) {
        this.a.addAll(partialAggregate.a);
        this.b.addAll(partialAggregate.b);
    }

    public Double getAggValue() {
        if ((this.a.size() == 0) || (this.b.size() == 0)) {
            return 0.0;
        }
        final double[][] pointToPointDistance = new double[this.a.size()][this.b.size()];
        final Iterator<Double> aIter = this.a.iterator();
        for (int i = 0; aIter.hasNext(); i++) {
            final double aValue = aIter.next().doubleValue();
            final Iterator<Double> bIter = this.b.iterator();
            for (int j = 0; bIter.hasNext(); j++) {
                final double bValue = bIter.next().doubleValue();
                pointToPointDistance[i][j] = DynamicTimeWrapingPartialAggregate.distance(aValue, bValue);
            }
        }
        final double[][] distanceMatrix = new double[this.a.size()][this.b.size()];
        distanceMatrix[0][0] = pointToPointDistance[0][0];
        // vertically, distanceMatrix[i][0] = sum pointToPointDistance[k][0]
        // where k = 0:i
        for (int i = 1; i < this.a.size(); i++) {
            distanceMatrix[i][0] = pointToPointDistance[i][0] + distanceMatrix[i - 1][0];
        }
        // horizontally, distanceMatrix[0][i] = sum pointToPointDistance[0][k]
        // where k = 0:i
        for (int i = 1; i < this.b.size(); i++) {
            distanceMatrix[0][i] = pointToPointDistance[0][i] + distanceMatrix[0][i - 1];
        }
        // Compute the rest of the DTW matrix.
        for (int i = 1; i < this.a.size(); i++) {
            for (int j = 1; j < this.b.size(); j++) {
                final double min = DynamicTimeWrapingPartialAggregate.min(distanceMatrix[i - 1][j - 1], distanceMatrix[i - 1][j], distanceMatrix[i][j - 1]);
                distanceMatrix[i][j] = pointToPointDistance[i][j] + min;
            }
        }

        int i = this.a.size() - 1;
        int j = this.b.size() - 1;
        int k = 1;
        double dist = distanceMatrix[i][j];
        // Determine the optimal warping path
        while ((i + j) > 2) {
            // The case of horizontal border, then a path moves to left only
            if (i == 0) {
                j--;
            }
            // The case of vertical border, then a path moves to down only
            else if (j == 0) {
                i--;
            }
            // a path in the heart of the matrix
            else {
                final double min = DynamicTimeWrapingPartialAggregate.min(distanceMatrix[i - 1][j - 1], distanceMatrix[i - 1][j], distanceMatrix[i][j - 1]);
                // Pointing to the next DTW matrix element on the warping path.
                if (min == distanceMatrix[i - 1][j - 1]) {
                    i--;
                    j--;
                }
                else if (min == distanceMatrix[i - 1][j]) {
                    i--;
                }
                else if (min == distanceMatrix[i][j - 1]) {
                    j--;
                }
            }
            k++;
            dist += distanceMatrix[i][j];
        }
        return new Double(dist / k);

        // return (1.0 - (FastMath.sqrt(dist) / k / (1.0 + (FastMath.sqrt(dist)
        // / k))));
    }

    public void add(final Number a, final Number b) {
        if ((a != null) && (b != null)) {
            this.a.add(a.doubleValue());
            this.b.add(b.doubleValue());
        }
    }

    public void add(final DynamicTimeWrapingPartialAggregate<?> value) {
        this.a.addAll(value.a);
        this.b.addAll(value.b);
    }

    @Override
    public DynamicTimeWrapingPartialAggregate<R> clone() {
        return new DynamicTimeWrapingPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "DTW= " + this.getAggValue();
    }

    private static double distance(final double a, final double b) {
        final double diff = a - b;
        return FastMath.pow(diff, 2.0);
    }

    private static double min(final double a, final double b, final double c) {
        return FastMath.min(a, FastMath.min(b, c));
    }

    //@SuppressWarnings("boxing")
    public static void main(final String[] args) {
        final DynamicTimeWrapingPartialAggregate<?> agg = new DynamicTimeWrapingPartialAggregate<>();
        agg.add(1.0, 1.0);
        agg.add(4.0, 4.0);
        agg.add(10.0, 10.0);
        for (int i = 0; i < 10000; i++) {
            agg.add(10.0, 10.0);
        }
        assert (agg.getAggValue() == 0.0);
        System.out.println(agg);
        agg.add(5.0, 0.0);
        assert (agg.getAggValue() == 5.0);
        System.out.println(agg);
    }

}
