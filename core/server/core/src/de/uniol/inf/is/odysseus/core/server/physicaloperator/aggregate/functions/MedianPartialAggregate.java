/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MedianPartialAggregate<T> extends AbstractPartialAggregate<T> {

    private static final long serialVersionUID = -7798856857638228860L;

    private final Queue<Double> upper = new PriorityQueue<>();

    private final Queue<Double> lower = new PriorityQueue<>(11, new Comparator<Double>() {
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

    public MedianPartialAggregate() {

    }

    public MedianPartialAggregate(final Queue<Double> lower, final Queue<Double> upper) {
        this.lower.addAll(lower);
        this.upper.addAll(upper);
    }

    public MedianPartialAggregate(final List<Double> values) {
        this.addAll(values);
    }

    public MedianPartialAggregate(final MedianPartialAggregate<T> medianPartialAggregate) {
        this.lower.addAll(medianPartialAggregate.lower);
        this.upper.addAll(medianPartialAggregate.upper);
    }

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
            return this.lower.peek();
        }
    }

    public MedianPartialAggregate<T> add(final Number value) {
        if (value != null) {
            if ((this.lower.isEmpty()) && (this.upper.isEmpty())) {
                this.lower.add(value.doubleValue());
            }
            else {
                if (value.doubleValue() <= this.lower.peek()) {
                    this.lower.add(value.doubleValue());
                }
                else {
                    this.upper.add(value.doubleValue());
                }
                this.rebalance();
            }
        }
        return this;
    }

    public MedianPartialAggregate<T> merge(final MedianPartialAggregate<?> value) {
        this.lower.addAll(value.lower);
        this.upper.addAll(value.upper);
        this.rebalance();
        return this;
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

    @Override
    public void clear() {
        lower.clear();
        upper.clear();
    }

    @Override
    public MedianPartialAggregate<T> clone() {
        return new MedianPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "MEDIAN= " + this.getAggValue();
    }

    private void rebalance() {
        if (this.lower.size() < (this.upper.size() - 1)) {
            this.lower.add(this.upper.remove());
        }
        else if (this.upper.size() < (this.lower.size() - 1)) {
            this.upper.add(this.lower.remove());
        }
    }

    public static void main(final String[] args) {
        final MedianPartialAggregate<?> agg = new MedianPartialAggregate<>();
        agg.add(1.0);
        agg.add(3.0);
        agg.add(2.0);
        agg.add(0.0);
        agg.add(5.0);
        agg.add(4.0);
        assert (agg.getAggValue() == 2.5);
        System.out.println(agg);
    }

}
