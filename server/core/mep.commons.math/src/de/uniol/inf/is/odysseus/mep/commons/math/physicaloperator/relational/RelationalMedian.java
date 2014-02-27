/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions.MedianPartialAggregate;

/**
 * Estimates the median of the given attribute.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalMedian extends AbstractRelationalMedian {

    /**
     * 
     */
    private static final long serialVersionUID = -7768784425424062403L;

    static public RelationalMedian getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalMedian(pos, partialAggregateInput);
    }

    private RelationalMedian(final int pos, final boolean partialAggregateInput) {
        super("MEDIAN", pos, partialAggregateInput);
    }

    @Override
    public IPartialAggregate<Tuple<?>> init(Tuple in) {
        if (isPartialAggregateInput()) {
            return init((MedianPartialAggregate<Tuple<?>>) in.getAttribute(pos));
        }
        else {
            MedianPartialAggregate<Tuple<?>> pa = new MedianPartialAggregate<Tuple<?>>();
            pa.add(((Number) in.getAttribute(this.pos)).doubleValue());
            return pa;
        }
    }

    @Override
    protected IPartialAggregate<Tuple<?>> process_merge(IPartialAggregate p, Tuple toMerge) {
        MedianPartialAggregate pa = (MedianPartialAggregate) p;
        if (isPartialAggregateInput()) {
            return merge(p, (IPartialAggregate) toMerge.getAttribute(pos), false);
        }
        else {
            return pa.add(((Number) toMerge.getAttribute(pos)).doubleValue());
        }
    }

    @Override
    public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, IPartialAggregate<Tuple<?>> toMerge, boolean createNew) {
        MedianPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            MedianPartialAggregate<Tuple<?>> h = (MedianPartialAggregate<Tuple<?>>) p;
            pa = new MedianPartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (MedianPartialAggregate<Tuple<?>>) p;
        }
        return pa.merge((MedianPartialAggregate) toMerge);
    }

}
