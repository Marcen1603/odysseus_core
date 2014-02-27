/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;

/**
 * Implementation of M. Greenwald and S. Khanna. Space-efficient online
 * computation of quantile summaries
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class GreenwaldKhannaMedianPartialAggregate<R> implements IMedianPartialAggregate<R> {
    private final double epsilon;
    private LinkedList<Tuple> summary;
    private double min;
    private double max;
    private int count;
    private int stream;

    /**
     * 
     * @param epsilon
     *            The allowed error according to the stream length.
     */
    public GreenwaldKhannaMedianPartialAggregate(final double epsilon) {
        this.epsilon = epsilon;
        this.summary = new LinkedList<Tuple>();
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
        this.count = 0;
        this.stream = 0;
    }

    /**
     * Default constructor with an allowed error of epsilon=0.25.
     */
    public GreenwaldKhannaMedianPartialAggregate() {
        this(0.0005);
    }

    /**
     * Copy constructor
     * 
     * @param partialAggregate
     *            The copy
     */
    public GreenwaldKhannaMedianPartialAggregate(final GreenwaldKhannaMedianPartialAggregate<R> partialAggregate) {
        this.epsilon = partialAggregate.epsilon;
        this.min = partialAggregate.min;
        this.max = partialAggregate.max;
        this.count = partialAggregate.count;
        this.stream = partialAggregate.stream;
        this.summary = new LinkedList<>(partialAggregate.summary);
    }

    /**
     * {@inheritDoc}
     * 
     * @return
     */
    @Override
    public GreenwaldKhannaMedianPartialAggregate<R> add(final Double value) {
        if ((this.count % (1.0 / (2.0 * this.epsilon()))) == 0.0) {
            this.compress(this.count, this.stream);
        }
        this.insert(value, this.count);
        this.count++;
        return this;
    }

    /**
     * @param paToMerge
     */
    public GreenwaldKhannaMedianPartialAggregate<R> merge(final GreenwaldKhannaMedianPartialAggregate<R> partialAggregate) {
        for (final Tuple t : partialAggregate.summary) {
            for (int i = 0; i < t.gain; i++) {
                this.add(t.value);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getAggValue() {
        int i = 0;
        final Iterator<Tuple> iter = this.summary.iterator();
        final double quantile = this.count / 2.0;
        while (iter.hasNext()) {
            final Tuple cur = iter.next();
            if (quantile <= (i + cur.gain)) {
                if (((this.count % 2) != 0) || ((quantile + 1) <= (i + cur.gain))) {
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

    private void insert(final double v, int n) {
        int pos = Collections.binarySearch(this.summary, new Tuple(v, 1.0, 0.0));
        double range = 0.0;
        if ((v < this.min) || (v > this.max)) {
            this.min = FastMath.min(this.min, v);
            this.max = FastMath.max(this.max, v);
        }
        Tuple prev = null;
        Tuple next = null;
        if (pos >= 0) {
            next = this.summary.get(pos);
            range = (next.gain + next.range) - 1;
            if (pos > 0) {
                prev = this.summary.get(pos - 1);
            }
        }
        else {
            pos = -pos - 1;
            if (pos > 0) {
                prev = this.summary.get(pos - 1);
            }
            if ((pos == 0) && (this.summary.size() > 0)) {
                next = this.summary.get(pos);
            }
        }
        Tuple cur = new Tuple(v, 1.0, range);
        if ((prev != null) && (band(prev.range, 2.0 * this.epsilon() * n) < band(cur.range, 2.0 * this.epsilon() * n))) {
            cur.setParent(prev);
        }
        if ((next != null) && (band(cur.range, 2.0 * this.epsilon() * n) < band(next.range, 2.0 * this.epsilon() * n))) {
            next.setParent(cur);
        }
        this.summary.add(pos, cur);
        this.stream++;
    }

    private void compress(final int n, final int s) {
        if (this.summary.size() <= 1) {
            return;
        }
        double sum = 0.0;
        for (int i = s - 2; i > 0; i--) {
            Tuple cur = this.summary.get(i);
            Tuple next = this.summary.get(i + 1);
            final int band = this.band(cur.range, 2.0 * this.epsilon() * n);
            final int nextBand = this.band(next.range, 2.0 * this.epsilon() * n);
            if ((next.parent != null) && (next.parent.equals(cur))) {
                sum += cur.gain;
            }
            else {
                sum = cur.gain;
            }
            if ((band <= nextBand) && ((sum + next.gain + next.range) < (2.0 * this.epsilon() * n))) {
                cur = delete(cur, next);
                this.summary.remove(i);
                if (i + 1 < summary.size()) {
                    next = this.summary.get(i + 1);
                    while ((next.parent != null) && (next.parent.equals(cur))) {
                        cur = delete(cur, next);
                        this.summary.remove(i);
                        next = this.summary.get(i + 1);
                    }
                }
            }

        }
    }

    private Tuple delete(Tuple cur, Tuple next) {
        Tuple merged = next.merge(cur);
        this.stream--;
        return merged;
    }

    private int band(final double range, final double p) {
        final int diff = (int) ((p - range) + 1.0);
        double band;
        if (diff == 1) {
            return (0);
        }
        else {
            band = FastMath.log((double) diff) / FastMath.log(2.0);
            return ((int) band);
        }
    }

    private double epsilon() {
        return this.epsilon;
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
        private Tuple parent;

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

        public void setParent(Tuple tuple) {
            this.parent = tuple;
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
        DescriptiveStatistics stats = new DescriptiveStatistics();
        GreenwaldKhannaMedianPartialAggregate<?> agg = new GreenwaldKhannaMedianPartialAggregate<>();
        for (double i = 0.0; i < 500.0; i += 0.5) {
            agg.add(i);
            stats.addValue(i);
        }
        assert (agg.getAggValue() == stats.getPercentile(50));
        System.out.println(agg + " == " + stats.getPercentile(50));
        stats = new DescriptiveStatistics();
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        for (double i = 5000.0; i > 0.0; i -= 0.5) {
            agg.add(i);
            stats.addValue(i);
        }
        assert (agg.getAggValue() == stats.getPercentile(50));
        System.out.println(agg + " == " + stats.getPercentile(50));

        stats = new DescriptiveStatistics();
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        for (double i = 5000.0; i > 0.0; i -= 0.5) {
            agg.add(i);
            stats.addValue(i);
        }
        for (double i = .0; i < 5000.0; i += 0.5) {
            agg.add(i);
            stats.addValue(i);
        }
        for (double i = 5000.0; i > 0.0; i -= 0.25) {
            agg.add(i);
            stats.addValue(i);
        }
        assert (agg.getAggValue() == stats.getPercentile(50));
        System.out.println(agg + " == " + stats.getPercentile(50));

        stats = new DescriptiveStatistics();
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        Random random = new Random();
        int n = 50000;
        for (int i = 0; i < n; i++) {
            double value = random.nextDouble() * 100;
            agg.add(value);
            stats.addValue(value);
        }
        assert (agg.getAggValue() == stats.getPercentile(50));
        System.out.println(agg + " == " + stats.getPercentile(50) + " -> " + (stats.getPercentile(50) - agg.getAggValue()) + " < " + agg.epsilon * 50000);
    }
}
