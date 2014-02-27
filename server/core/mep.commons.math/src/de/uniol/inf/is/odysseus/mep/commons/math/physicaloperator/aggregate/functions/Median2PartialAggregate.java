/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Median2PartialAggregate<R> implements IMedianPartialAggregate<R> {

    private final Queue<Double> upper = new PriorityQueue<Double>();

    private final Queue<Double> lower = new PriorityQueue<Double>(11, new Comparator<Double>() {
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

    public Median2PartialAggregate() {

    }

    public Median2PartialAggregate(final Queue<Double> lower, final Queue<Double> upper) {
        this.lower.addAll(lower);
        this.upper.addAll(upper);
    }

    public Median2PartialAggregate(final List<Double> values) {
        this.addAll(values);
    }

    public Median2PartialAggregate(final Median2PartialAggregate<R> medianPartialAggregate) {
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
    public Median2PartialAggregate<R> add(final Double value) {
        if ((this.lower.isEmpty()) && (this.upper.isEmpty())) {
            this.lower.add(value);
        }
        else {
            if (value <= this.lower.peek()) {
                this.lower.add(value);
            }
            else {
                this.upper.add(value);
            }
            this.rebalance();
        }
        return this;
    }

    public Median2PartialAggregate<R> merge(final Median2PartialAggregate<?> value) {
        this.lower.addAll(value.lower);
        this.upper.addAll(value.upper);
        this.rebalance();
        return this;
    }

    // public void remove(final Double value) {
    // if ((this.lower.remove(value)) || (this.upper.remove(value))) {
    // this.rebalance();
    // }
    // }

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
    public Median2PartialAggregate<R> clone() {
        return new Median2PartialAggregate<R>(this);
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
        final Median2PartialAggregate<?> agg = new Median2PartialAggregate<>();
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
