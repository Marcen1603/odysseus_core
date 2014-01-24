/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AvgPartialAggregate<R> implements IPartialAggregate<R> {
    private double mean;
    private int count;

    public AvgPartialAggregate(double value) {
        this.add(value);
    }

    public AvgPartialAggregate(double mean, int count) {
        this.mean = mean;
        this.count = count;
    }

    public AvgPartialAggregate(AvgPartialAggregate<R> avgPartialAggregate) {
        this.mean = avgPartialAggregate.mean;
        this.count = avgPartialAggregate.count;
    }

    public Double getAggValue() {
        return mean;
    }

    public void add(Double value) {
        this.count++;
        this.mean += 1.0 / count * (value - mean);

    }

    public void add(AvgPartialAggregate<R> avgPartialAggregate) {
        this.mean = (this.mean * this.count + avgPartialAggregate.mean * avgPartialAggregate.count) / (avgPartialAggregate.count + this.count);
        this.count += avgPartialAggregate.count;
    }

    @Override
    public AvgPartialAggregate<R> clone() {
        return new AvgPartialAggregate<R>(this);
    }

    @Override
    public String toString() {
        return "AVG= " + getAggValue();
    }

    public static void main(final String[] args) {
        final AvgPartialAggregate<Double> agg = new AvgPartialAggregate<>(1.0);
        agg.add(3.0);
        agg.add(2.0);
        assert (agg.getAggValue() == 2.0);
        System.out.println(agg + "==" + 2.0);
        agg.add(new AvgPartialAggregate<Double>(4.5, 2));
        assert (agg.getAggValue() == 3.0);
        System.out.println(agg + "==" + 3.0);

    }
}
