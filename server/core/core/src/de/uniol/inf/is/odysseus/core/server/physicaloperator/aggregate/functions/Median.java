/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class Median<R, W> extends AbstractAggregateFunction<R, W> {

    /**
     * 
     */
    private static final long serialVersionUID = -6053900953263781699L;

    protected Median(boolean partialAggregateInput) {
        super("MEDIAN", partialAggregateInput);
    }

}
