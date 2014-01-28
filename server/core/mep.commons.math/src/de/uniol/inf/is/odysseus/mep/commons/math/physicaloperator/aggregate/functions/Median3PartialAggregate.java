/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings("unchecked")
public class Median3PartialAggregate<R> implements IMedianPartialAggregate<R> {

    // Increase min number of elements used in the queues to improve result.
    private final int MIN_ELEMENTS = 10;
    private double totalSum;
    private double totalVariance;

    private final MedianQueue<Double> upper = new MedianQueue<Double>(11, new Comparator<Double>() {
        @Override
        public int compare(final Double a, final Double b) {
            if (a > b) {
                return 1;
            }
            else if (a < b) {
                return -1;
            }
            else {
                return 0;
            }
        }
    });

    private final MedianQueue<Double> lower = new MedianQueue<Double>(11, new Comparator<Double>() {
        @Override
        public int compare(final Double a, final Double b) {
            if (a < b) {
                return 1;
            }
            else if (a > b) {
                return -1;
            }
            else {
                return 0;
            }
        }
    });

    public Median3PartialAggregate(final Double value) {
        this.add(value);
    }

    public Median3PartialAggregate(final Queue<Double> lower, final Queue<Double> upper) {
        this.lower.addAll(lower);
        this.upper.addAll(upper);
    }

    public Median3PartialAggregate(final List<Double> values) {
        this.addAll(values);
    }

    public Median3PartialAggregate(final Median3PartialAggregate<R> medianPartialAggregate) {
        this.lower.addAll(medianPartialAggregate.lower);
        this.upper.addAll(medianPartialAggregate.upper);
    }

    @Override
	public Double getAggValue() {
        if ((this.lower.isEmpty()) && (this.upper.isEmpty())) {
            return null;
        }
        else if (this.lower.size() == this.upper.size()) {
            return (this.lower.peek() + this.upper.peek()) / 2;
        }
        else {
            if (this.lower.size() < this.upper.size()) {
                return this.upper.peek();
            }
            else {
                return this.lower.peek();
            }
        }
    }

    @Override
	public void add(final Double value) {
        int size = this.lower.size + this.upper.size;
        if ((this.lower.isEmpty()) && (this.upper.isEmpty())) {
            this.lower.offer(value);
        }
        else {
            if (value <= this.lower.peek()) {
                this.lower.offer(value);
            }
            else {
                this.upper.offer(value);
            }
            this.rebalance();
        }
        if (size > 0) {
            totalVariance += size * Math.pow(value - totalSum / size, 2.0) / (size + 1);
        }
        totalSum += value;
        if (canPurge(this.lower, this.upper, totalVariance)) {
            this.purge();
        }
    }

    private double harmonicMean(final double n0, final double n1) {
        return 1.0 / ((1.0 / n0) + (1.0 / n1));
    }

    /**
     * Required value for not purging elements out of the queues
     * 
     * @param m
     * @param sigma
     * @param deltaPrime
     * @return
     */
    private double epsilonCut(final double m, final double sigma, final double deltaPrime) {
        return Math.sqrt((2.0 / (m)) * sigma * Math.log(2.0 / deltaPrime)) + ((2.0 / (3.0 * m)) * Math.log(2.0 / deltaPrime));
    }

    public void add(final Median3PartialAggregate<?> value) {
        this.lower.addAll(value.lower);
        this.upper.addAll(value.upper);
        this.rebalance();

    }

    public void purge() {
        int size = this.lower.size + this.upper.size;
        double min = this.lower.removeLast();
        size--;
        totalVariance -= size * Math.pow(min - totalSum / size, 2.0) / (size + 1);
        double max = this.upper.removeLast();
        size--;
        totalVariance -= size * Math.pow(max - totalSum / size, 2.0) / (size + 1);

    }

    public void addAll(final double[] values) {
        for (final double val : values) {
            this.add(val);
        }
    }

    public void addAll(final List<Double> values) {
        for (final double val : values) {
            this.add(val);
        }
    }

    /**
     * 
     */
    @Override
	public void clear() {
        lower.clear();
        upper.clear();
    }

    @Override
    public Median3PartialAggregate<R> clone() {
        return new Median3PartialAggregate<R>(this);
    }

    @Override
    public String toString() {
        return "MEDIAN= " + this.getAggValue();
    }

    /**
     * Check if elements can be purged out of the queues. This should be
     * possible if the variance in both queues are *nearly* equal.
     * 
     * @param lower
     * @param upper
     * @param totalVariance
     * @return
     */
    private boolean canPurge(MedianQueue<Double> lower, MedianQueue<Double> upper, double totalVariance) {
        boolean purge = false;
        if ((lower.size() == upper.size()) && (lower.size() > MIN_ELEMENTS) && (lower.size() > MIN_ELEMENTS)) {
            double epsilon = epsilonCut(harmonicMean(lower.size, upper.size), totalVariance, 0.01);

            // Compare the variances in both queues
            // If the diff is lower than epsilon, the last elements can be
            // purged.
            purge = (Math.abs(lower.variance - upper.variance) < epsilon);
            if (purge) {
                System.out.println("Purge: ");
                System.out.println("Var " + totalVariance);
                System.out.println("Epsi " + epsilonCut(harmonicMean(lower.size, upper.size), totalVariance, 0.01));
                System.out.println("Stat lower: ");
                System.out.println(lower.size);
                System.out.println(lower.sum);
                System.out.println(lower.variance);
                System.out.println("Stat upper: ");
                System.out.println(upper.size);
                System.out.println(upper.sum);
                System.out.println(upper.variance);
            }
        }
        return purge;
    }

    /**
     * Rebalance both queues, so the number of elements are equal or differ by 1
     */
    private void rebalance() {
        if ((this.upper.size() != 0) && (this.lower.size() != 0)) {
            if (this.lower.size() < (this.upper.size() - 1)) {
                this.lower.add(this.upper.remove());
            }
            else if (this.upper.size() < (this.lower.size() - 1)) {
                this.upper.add(this.lower.remove());
            }
        }
    }

    public static void main(final String[] args) {
        final DescriptiveStatistics stats = new DescriptiveStatistics();
        System.out.println("Run 1");
        final Median3PartialAggregate<?> agg = new Median3PartialAggregate<>(1.0);
        agg.add(2.0);
        agg.add(3.0);
        agg.add(4.0);
        agg.add(5.0);
        agg.add(6.0);
        agg.add(7.0);
        agg.add(8.0);
        agg.add(9.0);
        agg.add(0.0);
        agg.add(0.0);
        agg.add(0.0);
        agg.add(0.0);
        agg.add(0.0);
        agg.add(0.0);
        agg.add(0.0);
        agg.add(0.0);
        agg.add(0.0);
        stats.addValue(1.0);
        stats.addValue(2.0);
        stats.addValue(3.0);
        stats.addValue(4.0);
        stats.addValue(5.0);
        stats.addValue(6.0);
        stats.addValue(7.0);
        stats.addValue(8.0);
        stats.addValue(9.0);

        stats.addValue(0.0);
        stats.addValue(0.0);
        stats.addValue(0.0);
        stats.addValue(0.0);
        stats.addValue(0.0);
        stats.addValue(0.0);
        stats.addValue(0.0);
        stats.addValue(0.0);
        stats.addValue(0.0);

        System.out.println(agg.getAggValue() + "==" + stats.getPercentile(50));
        System.out.println("Run 2");
        final Median3PartialAggregate<?> agg2 = new Median3PartialAggregate<>(1.0);
        agg2.add(0.0);
        agg2.add(2.0);
        agg2.add(0.0);
        agg2.add(3.0);
        agg2.add(0.0);
        agg2.add(4.0);
        agg2.add(0.0);
        agg2.add(5.0);
        agg2.add(0.0);
        agg2.add(6.0);
        agg2.add(0.0);
        agg2.add(7.0);
        agg2.add(0.0);
        agg2.add(8.0);
        agg2.add(0.0);
        agg2.add(9.0);
        agg2.add(0.0);

        System.out.println(agg2.getAggValue() + "==" + stats.getPercentile(50));
        System.out.println("Run 3");
        final Median3PartialAggregate<?> agg3 = new Median3PartialAggregate<>(0.0);
        agg3.add(0.0);
        agg3.add(0.0);
        agg3.add(0.0);
        agg3.add(0.0);
        agg3.add(0.0);
        agg3.add(0.0);
        agg3.add(0.0);
        agg3.add(0.0);
        agg3.add(1.0);
        agg3.add(2.0);
        agg3.add(3.0);
        agg3.add(4.0);
        agg3.add(5.0);
        agg3.add(6.0);
        agg3.add(7.0);
        agg3.add(8.0);
        agg3.add(9.0);
        System.out.println(agg3.getAggValue() + "==" + stats.getPercentile(50));
    }

    /**
     * {@link PriorityQueue} extension that caclulates the variance of the
     * elements and can remove the last element in the queue.
     * 
     * @author Christian Kuka <christian@kuka.cc>
     * 
     * @param <E>
     */
    private class MedianQueue<E extends Number> extends AbstractQueue<E> implements java.io.Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -4346577637412635917L;
        private transient Object[] queue;
        private int size = 0;
        private final Comparator<? super E> comparator;
        private double variance;
        private double sum;

        /**
         * 
         */
        public MedianQueue(int initialCapacity, Comparator<? super E> comparator) {
            this.queue = new Object[initialCapacity];
            this.comparator = comparator;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean offer(E e) {
            int i = size;
            if (i >= queue.length)
                grow(i + 1);

            if (size > 0) {
                variance += size * Math.pow(e.doubleValue() - sum / size, 2.0) / (size + 1);
            }
            size = i + 1;
            sum += e.doubleValue();
            if (i == 0)
                queue[0] = e;
            else
                siftUp(i, e);
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public E poll() {
            if (size == 0)
                return null;
            int s = --size;
            E result = (E) queue[0];

            sum -= result.doubleValue();
            variance -= size * Math.pow(result.doubleValue() - sum / size, 2.0) / (size + 1);

            E x = (E) queue[s];
            queue[s] = null;
            if (s != 0)
                siftDown(0, x);
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public E peek() {
            if (size == 0)
                return null;
            return (E) queue[0];
        }

        public E removeLast() {
            E result = (E) queue[size - 1];
            this.removeAt(size - 1);
            variance -= size * Math.pow(result.doubleValue() - sum / size, 2.0) / (size + 1);
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Iterator<E> iterator() {
            return new Itr();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int size() {
            return size;
        }

        private void grow(int minCapacity) {
            int oldCapacity = queue.length;
            // Double size if small; else grow by 50%
            int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));

            queue = Arrays.copyOf(queue, newCapacity);
        }

        private E removeAt(int i) {
            assert i >= 0 && i < size;
            int s = --size;
            if (s == i) // removed last element
                queue[i] = null;
            else {
                E moved = (E) queue[s];
                queue[s] = null;
                siftDown(i, moved);
                if (queue[i] == moved) {
                    siftUp(i, moved);
                    if (queue[i] != moved)
                        return moved;
                }
            }
            return null;
        }

        boolean removeEq(Object o) {
            for (int i = 0; i < size; i++) {
                if (o == queue[i]) {
                    removeAt(i);
                    return true;
                }
            }
            return false;
        }

        /**
         * Inserts item x at position k, maintaining heap invariant by
         * promoting x up the tree until it is greater than or equal to
         * its parent, or is the root.
         * 
         * To simplify and speed up coercions and comparisons. the
         * Comparable and Comparator versions are separated into different
         * methods that are otherwise identical. (Similarly for siftDown.)
         * 
         * @param k
         *            the position to fill
         * @param x
         *            the item to insert
         */
        private void siftUp(int k, E x) {
            if (comparator != null)
                siftUpUsingComparator(k, x);
            else
                siftUpComparable(k, x);
        }

        private void siftUpComparable(int k, E x) {
            Comparable<? super E> key = (Comparable<? super E>) x;
            while (k > 0) {
                int parent = (k - 1) >>> 1;
                Object e = queue[parent];
                if (key.compareTo((E) e) >= 0)
                    break;
                queue[k] = e;
                k = parent;
            }
            queue[k] = key;
        }

        private void siftUpUsingComparator(int k, E x) {
            while (k > 0) {
                int parent = (k - 1) >>> 1;
                Object e = queue[parent];
                if (comparator.compare(x, (E) e) >= 0)
                    break;
                queue[k] = e;
                k = parent;
            }
            queue[k] = x;
        }

        /**
         * Inserts item x at position k, maintaining heap invariant by
         * demoting x down the tree repeatedly until it is less than or
         * equal to its children or is a leaf.
         * 
         * @param k
         *            the position to fill
         * @param x
         *            the item to insert
         */
        private void siftDown(int k, E x) {
            if (comparator != null)
                siftDownUsingComparator(k, x);
            else
                siftDownComparable(k, x);
        }

        private void siftDownComparable(int k, E x) {
            Comparable<? super E> key = (Comparable<? super E>) x;
            int half = size >>> 1; // loop while a non-leaf
            while (k < half) {
                int child = (k << 1) + 1; // assume left child is least
                Object c = queue[child];
                int right = child + 1;
                if (right < size && ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
                    c = queue[child = right];
                if (key.compareTo((E) c) <= 0)
                    break;
                queue[k] = c;
                k = child;
            }
            queue[k] = key;
        }

        private void siftDownUsingComparator(int k, E x) {
            int half = size >>> 1;
            while (k < half) {
                int child = (k << 1) + 1;
                Object c = queue[child];
                int right = child + 1;
                if (right < size && comparator.compare((E) c, (E) queue[right]) > 0)
                    c = queue[child = right];
                if (comparator.compare(x, (E) c) <= 0)
                    break;
                queue[k] = c;
                k = child;
            }
            queue[k] = x;
        }

        private final class Itr implements Iterator<E> {
            /**
             * Index (into queue array) of element to be returned by
             * subsequent call to next.
             */
            private int cursor = 0;

            /**
             * Index of element returned by most recent call to next,
             * unless that element came from the forgetMeNot list.
             * Set to -1 if element is deleted by a call to remove.
             */
            private int lastRet = -1;

            /**
             * A queue of elements that were moved from the unvisited portion of
             * the heap into the visited portion as a result of "unlucky"
             * element
             * removals during the iteration. (Unlucky element removals are
             * those
             * that require a siftup instead of a siftdown.) We must visit all
             * of
             * the elements in this list to complete the iteration. We do this
             * after we've completed the "normal" iteration.
             * 
             * We expect that most iterations, even those involving removals,
             * will not need to store elements in this field.
             */
            private ArrayDeque<E> forgetMeNot = null;

            /**
             * Element returned by the most recent call to next iff that
             * element was drawn from the forgetMeNot list.
             */
            private E lastRetElt = null;

            @Override
			public boolean hasNext() {
                return cursor < size || (forgetMeNot != null && !forgetMeNot.isEmpty());
            }

            @Override
			public E next() {
                if (cursor < size)
                    return (E) queue[lastRet = cursor++];
                if (forgetMeNot != null) {
                    lastRet = -1;
                    lastRetElt = forgetMeNot.poll();
                    if (lastRetElt != null)
                        return lastRetElt;
                }
                throw new NoSuchElementException();
            }

            @Override
			public void remove() {
                if (lastRet != -1) {
                    E moved = MedianQueue.this.removeAt(lastRet);
                    lastRet = -1;
                    if (moved == null)
                        cursor--;
                    else {
                        if (forgetMeNot == null)
                            forgetMeNot = new ArrayDeque<>();
                        forgetMeNot.add(moved);
                    }
                }
                else if (lastRetElt != null) {
                    MedianQueue.this.removeEq(lastRetElt);
                    lastRetElt = null;
                }
                else {
                    throw new IllegalStateException();
                }
            }
        }
    }
}
