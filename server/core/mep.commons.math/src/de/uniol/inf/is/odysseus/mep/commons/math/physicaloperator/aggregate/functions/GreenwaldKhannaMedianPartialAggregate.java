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
    }

    /**
     * Default constructor with an allowed error of epsilon=0.25.
     */
    public GreenwaldKhannaMedianPartialAggregate() {
        this(0.005);
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

        this.summary = new LinkedList<>();
        for (Tuple t : partialAggregate.summary) {
            this.summary.add(t.clone());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @return
     */
    @Override
    public GreenwaldKhannaMedianPartialAggregate<R> add(final Double value) {
        // Compress if number of values n=0mod1/2e but at least after 1000
        // values
        if ((this.count % 1000 == 0) || ((this.count % (1.0 / (2.0 * this.epsilon()))) == 0)) {
            this.compress(this.count);
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
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
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
            this.summary.add(new Tuple(v, 1, 0));
            return;
        }
        int pos = Collections.binarySearch(this.summary, new Tuple(v, 1, 0));
        int range = 0;
        if ((v < this.min) || (v > this.max)) {
            this.min = FastMath.min(this.min, v);
            this.max = FastMath.max(this.max, v);
        }
        Tuple prev = null;
        Tuple next = null;
        if (pos >= 0) {
            next = this.summary.get(pos);
            next.setParent(null);
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
            if (pos == 0) {
                next = this.summary.get(0);
            }
        }
        final Tuple cur = new Tuple(v, 1, range);
        if ((prev != null) && (this.band(prev.range, 2.0 * this.epsilon() * n) < this.band(cur.range, 2.0 * this.epsilon() * n))) {
            cur.setParent(prev);
        }
        if ((next != null) && (this.band(cur.range, 2.0 * this.epsilon() * n) < this.band(next.range, 2.0 * this.epsilon() * n))) {
            next.setParent(cur);
        }
        this.summary.add(pos, cur);
    }

    private void compress(final int n) {
        if (this.summary.size() <= 1) {
            return;
        }
        final ListIterator<Tuple> iter = this.summary.listIterator(this.summary.size() - 1);
        Tuple next = null;
        Tuple cur = null;
        int sum = 0;
        while (iter.hasPrevious()) {
            cur = iter.previous();
            iter.next();
            next = iter.next();
            final int band = this.band(cur.range, 2.0 * this.epsilon() * n);
            final int nextBand = this.band(next.range, 2.0 * this.epsilon() * n);
            if ((next.parent == null) && (band < nextBand)) {
                next.setParent(cur);
            }
            iter.previous();
            iter.previous();
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
                        if (iter.hasNext()) {
                            iter.next();
                            if (iter.hasNext()) {
                                next = iter.next();
                                iter.previous();
                            }
                            iter.previous();
                        }
                    }
                    iter.previous();
                }
                iter.previous();
                sum = 0;
            }
        }
    }

    private Tuple delete(final Tuple cur, final Tuple next) {
        final Tuple merged = next.merge(cur);
        cur.setParent(null);
        return merged;
    }

    private int band(final int range, final double p) {
        final double diff = (int) ((p - range) + 1);
        if (diff == 1.0) {
            return (0);
        }
        else {
            return (int) (FastMath.log(diff) / FastMath.log(2.0));
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
        return "MEDIAN= " + this.getAggValue() + " with " + this.summary.size() + " tuples";
    }

    private class Tuple implements Comparable<Tuple>, Cloneable {
        private int gain;
        private final int range;
        private final double value;
        private Tuple parent;

        /**
         * 
         */
        public Tuple(final double value, final int gain, final int range) {
            this.value = value;
            this.gain = gain;
            this.range = range;
        }

        /**
         * @param tuple
         */
        public Tuple(Tuple tuple) {
            this.value = tuple.value;
            this.gain = tuple.gain;
            this.range = tuple.range;
        }

        public Tuple merge(final Tuple t) {
            this.gain += t.gain;
            this.parent = t.parent;
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
        public Tuple clone() {
            return new Tuple(this);
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
            result = prime * result + getOuterType().hashCode();
            result = prime * result + this.gain;
            result = prime * result + this.range;
            long temp;
            temp = Double.doubleToLongBits(this.value);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            Tuple other = (Tuple) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (this.gain != other.gain) {
                return false;
            }
            if (this.range != other.range) {
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
        for (double i = 0.0; i < 10E6; i += 1.0) {
            agg.add(i);
            stats.addValue(i);
        }
        System.out.println(agg + " == " + stats.getPercentile(50));
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        stats = new DescriptiveStatistics();
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        for (double i = 10E6; i > 0.0; i -= 1.0) {
            agg.add(i);
            stats.addValue(i);
        }
        System.out.println(agg + " == " + stats.getPercentile(50));
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        stats = new DescriptiveStatistics();
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        for (double i = 10E6 / 3.0; i > 0.0; i -= 1.0) {
            agg.add(i);
            stats.addValue(i);
        }
        for (double i = .0; i < 10E6 / 3.0; i += 1.0) {
            agg.add(i);
            stats.addValue(i);
        }
        for (double i = 10E6 / 3.0; i > 0.0; i -= 1.0) {
            agg.add(i);
            stats.addValue(i);
        }
        System.out.println(agg + " == " + stats.getPercentile(50));
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final Random random = new Random();
        random.setSeed(0l);
        final int n = (int) 10E6;
        double[] values = new double[n];
        for (int i = 0; i < n; i++) {
            values[i] = random.nextDouble() * 100;
        }
        stats = new DescriptiveStatistics();
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        for (int i = 0; i < n; i++) {
            agg.add(values[i]);
            stats.addValue(values[i]);
        }
        System.out.println(agg + " == " + stats.getPercentile(50));
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long time = System.currentTimeMillis();
        agg = new GreenwaldKhannaMedianPartialAggregate<>();

        for (int i = 0; i < n; i++) {
            agg.add(values[i]);
        }
        System.out.println(agg + " of " + n + " values in " + (System.currentTimeMillis() - time) + "ms");
    }
}
