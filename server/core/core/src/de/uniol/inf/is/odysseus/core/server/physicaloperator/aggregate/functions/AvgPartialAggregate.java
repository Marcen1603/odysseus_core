/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AvgPartialAggregate<R> extends AbstractPartialAggregate<R> {

    private static final long serialVersionUID = 8223160967939959922L;

    private double mean;
    private int count;

    public AvgPartialAggregate(Number value) {
        this.add(value);
    }

    public AvgPartialAggregate(Number mean, int count) {
        if (mean != null) {
            this.mean = mean.doubleValue();
            this.count = count;
        }
    }

    public AvgPartialAggregate(AvgPartialAggregate<R> avgPartialAggregate) {
        this.mean = avgPartialAggregate.mean;
        this.count = avgPartialAggregate.count;
    }

    public Double getAggValue() {
        return mean;
    }

    public void add(Number value) {
        if (value != null) {
            this.count++;
            this.mean += 1.0 / count * (value.doubleValue() - mean);
        }
    }

    public void add(AvgPartialAggregate<R> avgPartialAggregate) {
        this.mean = (this.mean * this.count + avgPartialAggregate.mean * avgPartialAggregate.count) / (avgPartialAggregate.count + this.count);
        this.count += avgPartialAggregate.count;
    }

    @Override
    public AvgPartialAggregate<R> clone() {
        return new AvgPartialAggregate<>(this);
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
