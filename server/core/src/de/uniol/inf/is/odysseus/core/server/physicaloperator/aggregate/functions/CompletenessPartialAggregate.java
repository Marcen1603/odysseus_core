/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class CompletenessPartialAggregate<T> extends AbstractPartialAggregate<T> {

	private static final long serialVersionUID = -7585294235677204519L;
	/** The number of non-null items (depending on implementation). */
    private double nonNullCount = 0.0;
    /** The number of total items. */
    private double totalCount = 0.0;

    /**
     * Creates a new instance of the partial aggregate.
     * 
     * @param value
     *            The first value
     */
    public CompletenessPartialAggregate(Double value) {
        this.add(value);
    }

    /**
     * Copy constructor.
     * 
     * @param p
     *            The clone instance
     */
    public CompletenessPartialAggregate(CompletenessPartialAggregate<T> p) {
        this.nonNullCount = p.nonNullCount;
        this.totalCount = p.totalCount;
    }

    /**
     * Adds a new item to the partial aggregate.
     * 
     * @param value
     *            The new value
     */
    public final void add(final Double value) {
        this.nonNullCount += value;
        this.totalCount++;
    }

    /**
     * Gets the degree completeness of the aggregate.
     * 
     * @return The completeness with a value in [0,1]
     */
    public Double getCompleteness() {
        if (this.totalCount != 0.0) {
            return this.nonNullCount / this.totalCount;
        }
        else {
            return null;
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "" + getCompleteness();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public CompletenessPartialAggregate<T> clone() {
        return new CompletenessPartialAggregate<T>(this);
    }

}
