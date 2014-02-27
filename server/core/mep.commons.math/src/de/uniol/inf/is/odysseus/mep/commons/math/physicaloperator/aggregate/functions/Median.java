/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class Median<R, W> extends AbstractAggregateFunction<R, W> {

    /**
     * 
     */
    private static final long serialVersionUID = -6053900953263781699L;

    protected Median(final boolean partialAggregateInput) {
        super("MEDIAN", partialAggregateInput);
    }

    protected Median(String name, final boolean partialAggregateInput) {
        super(name, partialAggregateInput);
    }
    
    @Override
    public IPartialAggregate<R> merge(IPartialAggregate<R> p,
            R toMerge, boolean createNew) {
        return process_merge(createNew?p.clone():p, toMerge);
    }

    abstract protected IPartialAggregate<R> process_merge(
            IPartialAggregate<R> iPartialAggregate, R toMerge);

}
