/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class MultiWorldMinMaxPartialAggregate<T> implements IPartialAggregate<T> {
    /** The sum of the aggregate. */
    private AbstractProbabilisticValue<?> aggregate;
    /** Flag for min and max. */
    private final boolean max;
    /** The result data type. */
    private final String datatype;

    /**
     * Default constructor.
     * 
     * @param datatype
     *            The result datatype
     * @param max
     *            Flag indicating if this is a max or a min aggregation
     */
    public MultiWorldMinMaxPartialAggregate(final String datatype, final boolean max) {
        this.aggregate = new ProbabilisticDouble(0.0, 1.0);
        this.max = max;
        this.datatype = datatype;
    }

    /**
     * Creates a new partial aggregate with the given value.
     * 
     * @param aggregate
     *            The aggregate
     * @param datatype
     *            The result datatype
     * @param max
     *            The max flag
     */
    public MultiWorldMinMaxPartialAggregate(final AbstractProbabilisticValue<?> aggregate, final String datatype, final boolean max) {
        this.datatype = datatype;
        this.aggregate = aggregate;
        this.max = max;
    }

    /**
     * Copy constructor.
     * 
     * @param avgPartialAggregate
     *            The object to copy from
     */
    public MultiWorldMinMaxPartialAggregate(final MultiWorldMinMaxPartialAggregate<T> avgPartialAggregate) {
        this.aggregate = avgPartialAggregate.aggregate;
        this.max = avgPartialAggregate.max;
        this.datatype = avgPartialAggregate.datatype;
    }

    /**
     * Add the given value to the aggregate.
     * 
     * @param value
     *            The value
     */
    public final void add(final AbstractProbabilisticValue<?> value) {
        final Map<Double, Double> newValues = new HashMap<Double, Double>();
        for (final Entry<?, Double> aggregateEntry : this.aggregate.getValues().entrySet()) {
            for (final Entry<?, Double> valueEntry : value.getValues().entrySet()) {
                final double newValue;
                if (this.isMax()) {
                    newValue = FastMath.max(((Number) aggregateEntry.getKey()).doubleValue(), ((Number) valueEntry.getKey()).doubleValue());
                }
                else {
                    newValue = FastMath.min(((Number) aggregateEntry.getKey()).doubleValue(), ((Number) valueEntry.getKey()).doubleValue());

                }
                if (newValues.containsKey(newValue)) {
                    newValues.put(newValue, newValues.get(newValue) + (aggregateEntry.getValue() * valueEntry.getValue()));
                }
                else {
                    newValues.put(newValue, aggregateEntry.getValue() * valueEntry.getValue());
                }
            }
        }
        this.aggregate = new ProbabilisticDouble(newValues);
    }

    /**
     * Gets the value of the aggregate.
     * 
     * @return The aggregate
     */
    public AbstractProbabilisticValue<?> getAggregate() {
        return this.aggregate;
    }

    /**
     * @return the max flag
     */
    public boolean isMax() {
        return this.max;
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuffer ret = new StringBuffer("MinMaxPartialAggregate (").append(this.hashCode()).append(")").append(this.aggregate);
        return ret.toString();
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final MultiWorldMinMaxPartialAggregate<T> clone() {
        return new MultiWorldMinMaxPartialAggregate<T>(this);
    }

}
