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
public class CorrelationPartialAggregate<R> extends AbstractPartialAggregate<R> {

    private double sumA;
    private double sumB;
    private double squareSumA;
    private double squareSumB;
    private double crossproductSum;
    private int count;

    public CorrelationPartialAggregate(final Double a, final Double b) {
        this.add(a, b);
    }

    public CorrelationPartialAggregate(final CorrelationPartialAggregate<R> correlationPartialAggregate) {
        this.sumA = correlationPartialAggregate.sumA;
        this.sumB = correlationPartialAggregate.sumB;
        this.squareSumA = correlationPartialAggregate.squareSumA;
        this.squareSumB = correlationPartialAggregate.squareSumB;
        this.crossproductSum = correlationPartialAggregate.crossproductSum;
        this.count = correlationPartialAggregate.count;
    }

    public Double getAggValue() {
        return ((this.count * this.crossproductSum) - (this.sumA * this.sumB))
                / (Math.sqrt((this.count * this.squareSumA) - Math.pow(this.sumA, 2.0)) * Math.sqrt((this.count * this.squareSumB) - Math.pow(this.sumB, 2.0)));
    }

    public void add(final Double a, final Double b) {
        this.sumA += a;
        this.sumB += b;
        this.squareSumA += Math.pow(a, 2.0);
        this.squareSumB += Math.pow(b, 2.0);
        this.crossproductSum += a * b;
        this.count++;
    }

    public void add(final CorrelationPartialAggregate<?> value) {
        this.sumA += value.sumA;
        this.sumB += value.sumB;
        this.squareSumA += value.squareSumA;
        this.squareSumB += value.squareSumB;
        this.crossproductSum += value.crossproductSum;
        this.count += value.count;
    }

    @Override
    public CorrelationPartialAggregate<R> clone() {
        return new CorrelationPartialAggregate<R>(this);
    }

    @Override
    public String toString() {
        return "CORR= " + this.getAggValue();
    }

    public static void main(final String[] args) {
        final CorrelationPartialAggregate<?> agg = new CorrelationPartialAggregate<>(110.0, 29.0);
        agg.add(107.0, 32.0);
        agg.add(100.0, 27.0);
        agg.add(96.0, 29.0);
        agg.add(89.0, 25.0);
        agg.add(78.0, 25.0);
        agg.add(67.0, 21.0);
        agg.add(66.0, 26.0);
        agg.add(49.0, 22.0);
        assert (agg.getAggValue() == 0.843);
        System.out.println(agg);
    }

}
