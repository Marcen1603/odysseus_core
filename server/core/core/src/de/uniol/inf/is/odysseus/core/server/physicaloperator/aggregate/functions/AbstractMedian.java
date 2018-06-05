/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class AbstractMedian<R, W> extends AbstractAggregateFunction<R, W> {

    /**
     * 
     */
    private static final long serialVersionUID = -6053900953263781699L;

    /**
     * 
     * @param partialAggregateInput
     */
    protected AbstractMedian(final boolean partialAggregateInput) {
        super("MEDIAN", partialAggregateInput);
    }

    /**
     * 
     * @param name
     * @param partialAggregateInput
     */
    protected AbstractMedian(String name, final boolean partialAggregateInput) {
        super(name, partialAggregateInput);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew) {
        return process_merge(createNew ? p.clone() : p, toMerge);
    }

    /**
     * 
     * @param iPartialAggregate
     * @param toMerge
     * @return
     */
    abstract protected IPartialAggregate<R> process_merge(IPartialAggregate<R> iPartialAggregate, R toMerge);

}
