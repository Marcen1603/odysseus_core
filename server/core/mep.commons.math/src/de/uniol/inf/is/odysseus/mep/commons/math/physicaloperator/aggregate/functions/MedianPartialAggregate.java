/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MedianPartialAggregate<R> implements IMedianPartialAggregate<R> {
    private final DescriptiveStatistics stats = new DescriptiveStatistics();

    public MedianPartialAggregate() {

    }

    public MedianPartialAggregate(final MedianPartialAggregate<R> medianPartialAggregate) {

        this.addAll(medianPartialAggregate.stats.getValues());
    }

    @Override
    public Double getAggValue() {
        return stats.getPercentile(50);
    }

    @Override
    public MedianPartialAggregate<R> add(final Double value) {
        stats.addValue(value);
        return this;
    }

    public MedianPartialAggregate<R> merge(final MedianPartialAggregate<?> value) {
        this.addAll(value.stats.getValues());
        return this;
    }

    public MedianPartialAggregate<R> addAll(final double[] values) {
        for (final double val : values) {
            this.add(val);
        }
        return this;
    }

    /**
     * 
     */
    @Override
    public void clear() {
        stats.clear();
    }

    @Override
    public MedianPartialAggregate<R> clone() {
        return new MedianPartialAggregate<R>(this);
    }

    @Override
    public String toString() {
        return "MEDIAN= " + this.getAggValue();
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
