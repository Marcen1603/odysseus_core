/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.math3.util.FastMath;

/**
 * Implementation of M. Greenwald and S. Khanna. Space-efficient online
 * computation of quantile summaries
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class GreenwaldKhannaMedianPartialAggregate<R> implements IMedianPartialAggregate<R> {
    private final static double EPSILON = 0.25;
    private LinkedList<Tuple> summary;
    private double min;
    private double max;
    private int count;

    /**
 * 
 */
    public GreenwaldKhannaMedianPartialAggregate() {
        this.summary = new LinkedList<Tuple>();
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
    }

    /**
 * 
 */
    public GreenwaldKhannaMedianPartialAggregate(final double value) {
        this();
        this.add(value);
    }

    /**
 * 
 */
    public GreenwaldKhannaMedianPartialAggregate(final GreenwaldKhannaMedianPartialAggregate<R> partialAggregate) {
        this.min = partialAggregate.min;
        this.max = partialAggregate.max;
        this.count = partialAggregate.count;
        this.summary = new LinkedList<>(partialAggregate.summary);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final Double value) {
        if ((this.count % (1.0 / (2.0 * this.epsilon()))) == 0.0) {
            this.compress(this.count);
        }
        this.insert(value);
        this.count++;
    }

    /**
     * @param paToMerge
     */
    public void add(GreenwaldKhannaMedianPartialAggregate<R> partialAggregate) {
        for (Tuple t : partialAggregate.summary) {
            for (int i = 0; i < t.gain; i++) {
                add(t.value);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getAggValue() {
        int i = 0;
        final Iterator<Tuple> iter = this.summary.iterator();
        final int quantile = this.count / 2;
        while (iter.hasNext()) {
            final Tuple cur = iter.next();
            if (quantile <= (i + cur.gain)) {
                if (((this.count % 2) == 0) || ((quantile + 1) <= (i + cur.gain))) {
                    return cur.value;
                }
                else {
                    final Tuple next = iter.next();
                    return (cur.value + next.value) / 2.0;
                }
            }
            else {
                i += cur.gain;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.summary.clear();
        this.count = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GreenwaldKhannaMedianPartialAggregate<R> clone() {
        return new GreenwaldKhannaMedianPartialAggregate<R>(this);
    }

    private void insert(final double v) {
        int pos = Collections.binarySearch(this.summary, new Tuple(v, 1.0, 0.0));
        double range = 0.0;
        if ((v < this.min) || (v > this.max)) {
            this.min = FastMath.min(this.min, v);
            this.max = FastMath.max(this.max, v);
        }
        if (pos >= 0) {
            if (pos < summary.size()) {
                Tuple next = summary.get(pos + 1);
                range = (next.gain + next.range) - 1;
            }
        }
        else {
            pos = -pos - 1;
        }
        this.summary.add(pos, new Tuple(v, 1.0, range));
    }

    private void compress(final int n) {
        if (this.summary.size() <= 1) {
            return;
        }
        final Iterator<Tuple> iter = this.summary.descendingIterator();
        // Skip last element
        Tuple next = null;
        if (iter.hasNext()) {
            next = iter.next();
        }
        while (iter.hasNext()) {
            final Tuple cur = iter.next();
            // Ignore min value
            if (cur == this.summary.getFirst()) {
                return;
            }
            final int band = this.band(cur.range, 2.0 * this.epsilon() * n);
            final int nextBand = this.band(next.range, 2.0 * this.epsilon() * n);
            if ((band <= nextBand) && ((cur.gain + next.gain + next.range) < (2.0 * this.epsilon() * n))) {
                next = next.merge(cur);
                iter.remove();
            }
            else {
                next = cur;
            }
        }
    }

    private int band(final double range, final double p) {
        final int diff = (int) ((p - range) + 1.0);
        double band;
        if (diff == 1) {
            return (0);
        }
        else {
            band = FastMath.log(diff) / FastMath.log(2);
            return ((int) band);
        }
    }

    private double epsilon() {
        return GreenwaldKhannaMedianPartialAggregate.EPSILON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MEDIAN= " + this.getAggValue();
    }

    private class Tuple implements Comparable<Tuple> {
        private double gain;
        private final double range;
        private final double value;

        /**
         * 
         */
        public Tuple(final double value, final double gain, final double range) {
            this.value = value;
            this.gain = gain;
            this.range = range;
        }

        public Tuple merge(final Tuple t) {
            this.gain += t.gain;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(final Tuple o) {
            return Double.compare(this.value, o.value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "(" + this.value + "," + this.gain + "," + this.range + ")";
        }
    }

    public static void main(final String[] args) {
        final GreenwaldKhannaMedianPartialAggregate<?> agg = new GreenwaldKhannaMedianPartialAggregate<>();
        agg.add(12.0);
        agg.add(10.0);
        agg.add(11.0);
        agg.add(10.0);
        agg.add(1.0);
        agg.add(10.0);
        agg.add(11.0);
        agg.add(9.0);
        agg.add(6.0);

        assert (agg.getAggValue() == 10.0);
        System.out.println(agg);
    }

}
