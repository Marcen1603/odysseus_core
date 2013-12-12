/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;

/**
 * Completeness aggregate: Computes the completeness of an attribute or tuple
 * over a given aggregate window. The completeness is a value in the interval
 * [0,1].
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class Completeness<R, W> extends AbstractAggregateFunction<R, W> {
    /**
     * 
     */
    private static final long serialVersionUID = 2724192781054561135L;
    /** The datatype. */
    final private String datatype;

    /**
     * Creates a new instance of the completeness aggregation.
     * 
     * @param partialAggregateInput
     *            The partial aggregate
     * @param datatype
     *            The datatype
     */
    protected Completeness(boolean partialAggregateInput, String datatype) {
        super("COMPLETENESS", partialAggregateInput);
        this.datatype = datatype;
    }

    /**
     * @return the datatype
     */
    public String getDatatype() {
        return this.datatype;
    }

}
