/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MedianPartialAggregate<R> implements IPartialAggregate<R> {
    List<Double> values = new ArrayList<Double>();

    public MedianPartialAggregate(Double value) {
        this.values.add(value);
    }

    public MedianPartialAggregate(List<Double> values) {
        this.values.addAll(values);
    }

    public MedianPartialAggregate(MedianPartialAggregate<R> avgSumPartialAggregate) {
        this.values.addAll(avgSumPartialAggregate.values);
    }

    public Double getAggValue() {
        // Collections.sort(values);
        if (values.size() % 2 == 1)
            return values.get((values.size() + 1) / 2 - 1);
        else {
            double lower = values.get(values.size() / 2 - 1);
            double upper = values.get(values.size() / 2);

            return (lower + upper) / 2.0;
        }
    }

    public List<Double> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void add(Double value) {
        int index = Collections.binarySearch(values, value);
        if (index < 0) {
            index = -(index + 1);
        }
        this.values.add(index, value);
    }

    public void addAll(List<Double> values) {
        for (Double val : values) {
            this.add(val);
        }
    }

    @Override
    public MedianPartialAggregate<R> clone() {
        return new MedianPartialAggregate<R>(this);
    }

    @Override
    public String toString() {
        return "MEDIAN= " + getAggValue();
    }

    public static void main(String[] args) {
        MedianPartialAggregate<?> agg = new MedianPartialAggregate<>(1.0);
        agg.add(3.0);
        agg.add(2.0);
        agg.add(0.0);
        agg.add(5.0);
        agg.add(4.0);
        assert (agg.getValues().get(0) == 0.0);
        assert (agg.getValues().get(agg.getValues().size() - 1) == 5.0);
        assert (agg.getAggValue() == 2.5);
    }
}
