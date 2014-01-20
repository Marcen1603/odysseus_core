/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class StandardDeviation<R, W> extends AbstractAggregateFunction<R, W> {
    /**
     * 
     */
    private static final long serialVersionUID = 6829691202348635422L;

    protected StandardDeviation(final boolean partialAggregateInput) {
        super("STDDEV", partialAggregateInput);
    }
}
