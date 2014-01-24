/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions.Median3PartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalMedian3 extends AbstractRelationalMedian {

    /**
     * 
     */
    private static final long serialVersionUID = -9201017268546062758L;

    static public RelationalMedian3 getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalMedian3(pos, partialAggregateInput);
    }

    private RelationalMedian3(final int pos, final boolean partialAggregateInput) {
        super("MEDIAN3", pos, partialAggregateInput);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple in) {
        if (this.isPartialAggregateInput()) {
            return this.init((Median3PartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        else {
            return new Median3PartialAggregate<Tuple<?>>(((Number) in.getAttribute(this.pos)).doubleValue());
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new Median3PartialAggregate<Tuple<?>>((Median3PartialAggregate<Tuple<?>>) in);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate p, final Tuple toMerge, final boolean createNew) {
        Median3PartialAggregate<Tuple> pa = null;
        if (createNew) {
            final Median3PartialAggregate<Tuple> h = (Median3PartialAggregate<Tuple>) p;
            pa = new Median3PartialAggregate<Tuple>(h);
        }
        else {
            pa = (Median3PartialAggregate<Tuple>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        Median3PartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final Median3PartialAggregate<Tuple<?>> h = (Median3PartialAggregate<Tuple<?>>) p;
            pa = new Median3PartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (Median3PartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final Median3PartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final Median3PartialAggregate paToMerge = (Median3PartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }
}
