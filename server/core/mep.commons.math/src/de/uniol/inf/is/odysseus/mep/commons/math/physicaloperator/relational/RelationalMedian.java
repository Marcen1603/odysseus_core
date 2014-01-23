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

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple in) {
        if (this.isPartialAggregateInput()) {
            return this.init((MedianPartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        else {
            return new MedianPartialAggregate<Tuple<?>>(((Number) in.getAttribute(this.pos)).doubleValue());
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new MedianPartialAggregate<Tuple<?>>((MedianPartialAggregate<Tuple<?>>) in);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate p, final Tuple toMerge, final boolean createNew) {
        MedianPartialAggregate<Tuple> pa = null;
        if (createNew) {
            final MedianPartialAggregate<Tuple> h = (MedianPartialAggregate<Tuple>) p;
            pa = new MedianPartialAggregate<Tuple>(h);
        }
        else {
            pa = (MedianPartialAggregate<Tuple>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        MedianPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final MedianPartialAggregate<Tuple<?>> h = (MedianPartialAggregate<Tuple<?>>) p;
            pa = new MedianPartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (MedianPartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final MedianPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final MedianPartialAggregate paToMerge = (MedianPartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

}
