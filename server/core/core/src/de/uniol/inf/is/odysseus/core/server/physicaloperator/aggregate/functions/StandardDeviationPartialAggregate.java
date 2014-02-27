/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class StandardDeviationPartialAggregate<R> extends AbstractPartialAggregate<R> {

    private double diffSquareSum;
    private double mean;
    private int count;

    public StandardDeviationPartialAggregate(final Double value) {
        this.add(value);
    }

    public StandardDeviationPartialAggregate(final StandardDeviationPartialAggregate<R> standardDeviationPartialAggregate) {
        this.mean = standardDeviationPartialAggregate.mean;
        this.diffSquareSum = standardDeviationPartialAggregate.diffSquareSum;
        this.count = standardDeviationPartialAggregate.count;
    }

    public Double getAggValue() {
        return Math.sqrt(this.diffSquareSum / (this.count - 1.0));
    }

    public void add(final Double value) {
        // Estimate online variance value using
        // Donald E. Knuth (1998). The Art of Computer Programming, volume 2:
        // Seminumerical Algorithms, 3rd edn., p. 232. Boston: Addison-Wesley.
        this.count++;
        final double delta = value - this.mean;
        this.mean += delta / this.count;
        this.diffSquareSum += delta * (value - this.mean);
    }

    public void add(final StandardDeviationPartialAggregate<?> value) {
        this.mean += value.mean;
        this.diffSquareSum += value.diffSquareSum;
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
        assert (agg.getAggValue() == 164.7118696390761);
        System.out.println(agg);
    }
}
