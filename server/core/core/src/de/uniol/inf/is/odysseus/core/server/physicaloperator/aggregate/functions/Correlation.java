/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class Correlation<R, W> extends AbstractAggregateFunction<R, W> {
    /**
     * 
     */
    private static final long serialVersionUID = 5937453654939284172L;

    protected Correlation(final boolean partialAggregateInput) {
        super("CORR", partialAggregateInput);
    }
}
