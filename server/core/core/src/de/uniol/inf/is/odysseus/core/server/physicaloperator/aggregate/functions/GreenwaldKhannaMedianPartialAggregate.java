/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * Implementation of M. Greenwald and S. Khanna. Space-efficient online
 * computation of quantile summaries
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class GreenwaldKhannaMedianPartialAggregate<T> extends AbstractPartialAggregate<T> {

    private static final long serialVersionUID = -6999923917595784322L;

    private static final Logger LOG = LoggerFactory.getLogger(GreenwaldKhannaMedianPartialAggregate.class);

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
        this.summary = new LinkedList<>();
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
        this.count = 0;
    }

    /**
     * Default constructor with an allowed error of epsilon=0.005.
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
    public GreenwaldKhannaMedianPartialAggregate(final GreenwaldKhannaMedianPartialAggregate<T> partialAggregate) {
        this.epsilon = partialAggregate.epsilon;
        this.min = partialAggregate.min;
        this.max = partialAggregate.max;
        this.count = partialAggregate.count;

        this.summary = new LinkedList<>();
        for (final Tuple t : partialAggregate.summary) {
            this.summary.add(t.clone());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @return
     */
    public GreenwaldKhannaMedianPartialAggregate<T> add(final Double value) {
        if (value != null) {
            // Compress if number of values n=0mod1/2e
            if ((this.count % (1.0 / (2.0 * this.epsilon()))) == 0) {
                if (GreenwaldKhannaMedianPartialAggregate.LOG.isTraceEnabled()) {
                    GreenwaldKhannaMedianPartialAggregate.LOG.trace("Running compress before inserting the {}. element with value: {}", this.count + 1, value);
                }
                try {
                    this.compress(this.count);
                }
                catch (final Exception e) {
                    GreenwaldKhannaMedianPartialAggregate.LOG.error(e.getMessage(), e);
                }
                if (GreenwaldKhannaMedianPartialAggregate.LOG.isTraceEnabled()) {
                    GreenwaldKhannaMedianPartialAggregate.LOG.trace("Summary size {}", this.summary.size());
                }
            }
            this.insert(value, this.count);
            this.count++;
        }
        return this;
    }

    /**
     * @param paToMerge
     */
    public GreenwaldKhannaMedianPartialAggregate<T> merge(final GreenwaldKhannaMedianPartialAggregate<T> partialAggregate) {
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
                final Tuple next = iter.next();
                return (cur.value + next.value) / 2.0;
            }
            i += cur.gain;
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
    public GreenwaldKhannaMedianPartialAggregate<T> clone() {
        return new GreenwaldKhannaMedianPartialAggregate<>(this);
    }

    private void insert(final double v, final int n) {
        if (this.summary.size() == 0) {
            this.summary.add(new Tuple(v, 1, 0));
            return;
        }
        int pos = Collections.binarySearch(this.summary, new Tuple(v, 1, 0));
        int range = 0;
        if ((v < this.min) || (v > this.max)) {
            this.min = Math.min(this.min, v);
            this.max = Math.max(this.max, v);
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
                    while ((next != null) && (next.parent != null) && (next.parent.equals(cur))) {
                        cur = this.delete(cur, next);
                        iter.remove();
                        if (cur != this.summary.getLast()) {
                            iter.next();
                            next = iter.next();
                            iter.previous();
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
        return (int) (Math.log(diff) / Math.log(2.0));
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
        public Tuple(final Tuple tuple) {
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
            result = (prime * result) + this.getOuterType().hashCode();
            result = (prime * result) + this.gain;
            result = (prime * result) + this.range;
            long temp;
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

        private GreenwaldKhannaMedianPartialAggregate<T> getOuterType() {
            return GreenwaldKhannaMedianPartialAggregate.this;
        }

    }

    public static void main(final String[] args) {
        Runtime.getRuntime().gc();
        final Random random = new Random();
        GreenwaldKhannaMedianPartialAggregate<Object> agg = new GreenwaldKhannaMedianPartialAggregate<>();
        long mem = 0;
        long lastMem = 0;
        long maxMem = 0;
        long time = System.currentTimeMillis();
        int n = (int) 10E5;
        random.setSeed(0l);
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        mem = 0;
        lastMem = 0;
        maxMem = 0;
        Runtime.getRuntime().gc();
        System.out.println("Running median with " + n + " values");
        for (int i = 0; i < n; i++) {
            agg.add(random.nextDouble() * 1000.0);
            if ((i % 100000) == 0) {
                final Runtime rt = Runtime.getRuntime();
                mem = (rt.totalMemory() - rt.freeMemory());
                maxMem = Math.max(maxMem, mem);
                System.out.println("Memory: " + (mem / 1024) + "kb " + (mem > lastMem ? "^" : "v") + " Max: " + (maxMem / 1024000) + "MB");
                lastMem = mem;
            }
        }
        System.out.println(agg + " of " + n + " values in " + (System.currentTimeMillis() - time) + "ms");
        time = System.currentTimeMillis();
        n = (int) 10E6;
        random.setSeed(0l);
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        mem = 0;
        lastMem = 0;
        maxMem = 0;
        Runtime.getRuntime().gc();
        System.out.println("Running median with " + n + " values");
        for (int i = 0; i < n; i++) {
            agg.add(random.nextDouble() * 1000.0);
            if ((i % 100000) == 0) {
                final Runtime rt = Runtime.getRuntime();
                mem = (rt.totalMemory() - rt.freeMemory());
                maxMem = Math.max(maxMem, mem);
                System.out.println("Memory: " + (mem / 1024) + "kb " + (mem > lastMem ? "^" : "v") + " Max: " + (maxMem / 1024000) + "MB");
                lastMem = mem;
            }
        }
        System.out.println(agg + " of " + n + " values in " + (System.currentTimeMillis() - time) + "ms");
        time = System.currentTimeMillis();
        n = (int) 10E7;
        random.setSeed(0l);
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        mem = 0;
        lastMem = 0;
        maxMem = 0;
        Runtime.getRuntime().gc();
        System.out.println("Running median with " + n + " values");
        for (int i = 0; i < n; i++) {
            agg.add(random.nextDouble() * 1000.0);
            if ((i % 100000) == 0) {
                final Runtime rt = Runtime.getRuntime();
                mem = (rt.totalMemory() - rt.freeMemory());
                maxMem = Math.max(maxMem, mem);
                System.out.println("Memory: " + (mem / 1024) + "kb " + (mem > lastMem ? "^" : "v") + " Max: " + (maxMem / 1024000) + "MB");
                lastMem = mem;
            }
        }
        System.out.println(agg + " of " + n + " values in " + (System.currentTimeMillis() - time) + "ms");
        time = System.currentTimeMillis();
        n = (int) 10E8;
        random.setSeed(0l);
        agg = new GreenwaldKhannaMedianPartialAggregate<>();
        mem = 0;
        lastMem = 0;
        maxMem = 0;
        Runtime.getRuntime().gc();
        System.out.println("Running median with " + n + " values");
        for (int i = 0; i < n; i++) {
            agg.add(random.nextDouble() * 1000.0);
            if ((i % 100000) == 0) {
                final Runtime rt = Runtime.getRuntime();
                mem = (rt.totalMemory() - rt.freeMemory());
                maxMem = Math.max(maxMem, mem);
                System.out.println("Memory: " + (mem / 1024) + "kb " + (mem > lastMem ? "^" : "v") + " Max: " + (maxMem / 1024000) + "MB");
                lastMem = mem;
            }
        }
        System.out.println(agg + " of " + n + " values in " + (System.currentTimeMillis() - time) + "ms");

    }
}
