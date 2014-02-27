/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
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
        this(0.1);
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
        this.stream = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GreenwaldKhannaMedianPartialAggregate<R> clone() {
        return new GreenwaldKhannaMedianPartialAggregate<R>(this);
    }

    private void insert(final double v, final int n) {
        if (this.summary.size() == 0) {
            this.summary.add(new Tuple(v, 1.0, 0.0));
            this.stream++;
            return;
        }
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
            range = FastMath.floor(2.0 * this.epsilon() * n);
            if (pos > 0) {
                prev = this.summary.get(pos - 1);
            }
        }
        else {
            pos = -pos - 1;
            if (pos > 0) {
                prev = this.summary.get(pos - 1);
            }
            if (pos == 0) {
                next = this.summary.get(0);
            }
        }
        final Tuple cur = new Tuple(v, 1.0, range);
        if ((prev != null) && (this.band(prev.range, 2.0 * this.epsilon() * n) < this.band(cur.range, 2.0 * this.epsilon() * n))) {
            cur.setParent(prev);
        }
        if ((next != null) && (this.band(cur.range, 2.0 * this.epsilon() * n) < this.band(next.range, 2.0 * this.epsilon() * n))) {
            next.setParent(cur);
        }
        this.summary.add(pos, cur);
        this.stream++;
    }

    private void compress(final int n, final int s) {
        if (s <= 1) {
            return;
        }
        final ListIterator<Tuple> iter = this.summary.listIterator(s - 1);
        Tuple next = null;
        Tuple cur = null;
        double sum = 0.0;
        while (iter.hasPrevious()) {
            cur = iter.previous();
            iter.next();
            next = iter.next();
            iter.previous();
            iter.previous();
            final int band = this.band(cur.range, 2.0 * this.epsilon() * n);
            final int nextBand = this.band(next.range, 2.0 * this.epsilon() * n);
            if ((next.parent != null) && (next.parent.equals(cur))) {
                sum += cur.gain;
            }
            else {
                sum = cur.gain;
            }
            if ((band <= nextBand) && ((sum + next.gain + next.range) < (2.0 * this.epsilon() * n))) {
                cur = this.delete(cur, next);
                iter.remove();
                iter.next();
                if (iter.hasNext()) {
                    next = iter.next();
                    while ((next.parent != null) && (next.parent.equals(cur))) {
                        cur = this.delete(cur, next);
                        iter.remove();
                        iter.next();
                        next = iter.next();
                        iter.previous();
                        iter.previous();
                    }
                    iter.previous();
                }
                iter.previous();
                sum = 0.0;
            }
        }
    }

    private Tuple delete(final Tuple cur, final Tuple next) {
        final Tuple merged = next.merge(cur);
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
            band = FastMath.log(diff) / FastMath.log(2.0);
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

        public void setParent(final Tuple tuple) {
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

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + this.getOuterType().hashCode();
            long temp;
            temp = Double.doubleToLongBits(this.gain);
            result = (prime * result) + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(this.range);
            result = (prime * result) + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(this.value);
            result = (prime * result) + (int) (temp ^ (temp >>> 32));
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            final Tuple other = (Tuple) obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (Double.doubleToLongBits(this.gain) != Double.doubleToLongBits(other.gain)) {
                return false;
            }
            if (Double.doubleToLongBits(this.range) != Double.doubleToLongBits(other.range)) {
                return false;
            }
            if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
                return false;
            }
            return true;
        }

        private GreenwaldKhannaMedianPartialAggregate<R> getOuterType() {
            return GreenwaldKhannaMedianPartialAggregate.this;
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
        final Random random = new Random();
        final int n = 250000;
        for (int i = 0; i < n; i++) {
            final double value = random.nextDouble() * 100;
            agg.add(value);
            stats.addValue(value);
        }
        assert (agg.getAggValue() == stats.getPercentile(50));
        System.out.println(agg + " == " + stats.getPercentile(50) + " -> " + (stats.getPercentile(50) - agg.getAggValue()) + " < " + (agg.epsilon * 50000));
    }
}
