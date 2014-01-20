/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class StandardDeviationPartialAggregate<R> implements IPartialAggregate<R> {

    private double sum;
    private double squareSum;
    private int count;

    public StandardDeviationPartialAggregate(final Double value) {
        this.add(value);
    }

    public StandardDeviationPartialAggregate(final StandardDeviationPartialAggregate<R> standardDeviationPartialAggregate) {
        this.sum = standardDeviationPartialAggregate.sum;
        this.squareSum = standardDeviationPartialAggregate.squareSum;
        this.count = standardDeviationPartialAggregate.count;
    }

    public Double getAggValue() {
        return Math.sqrt(((this.squareSum - (Math.pow(this.sum, 2.0) / this.count)) / this.count) - 1.0);
    }

    public void add(final Double value) {
        this.sum += value;
        this.squareSum += Math.pow(value, 2.0);
        this.count++;
    }

    public void add(final StandardDeviationPartialAggregate<?> value) {
        this.sum += value.sum;
        this.squareSum += value.squareSum;
        this.count += value.count;
    }

    @Override
    public StandardDeviationPartialAggregate<R> clone() {
        return new StandardDeviationPartialAggregate<R>(this);
    }

    @Override
    public String toString() {
        return "STDDEV= " + this.getAggValue();
    }

    public static void main(final String[] args) {
        final StandardDeviationPartialAggregate<?> agg = new StandardDeviationPartialAggregate<>(206.0);
        agg.add(76.0);
        agg.add(-224.0);
        agg.add(36.0);
        agg.add(-94.0);
        assert (agg.getAggValue() == 147.31938093815083);
        System.out.println(agg);
    }
}
