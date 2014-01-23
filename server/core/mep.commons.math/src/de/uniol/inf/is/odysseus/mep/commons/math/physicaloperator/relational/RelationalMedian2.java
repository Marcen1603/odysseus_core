/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions.Median2PartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalMedian2 extends AbstractRelationalMedian {

    /**
     * 
     */
    private static final long serialVersionUID = 6781001947157878126L;

    static public RelationalMedian2 getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalMedian2(pos, partialAggregateInput);
    }

    private RelationalMedian2(final int pos, final boolean partialAggregateInput) {
        super("MEDIAN2", pos, partialAggregateInput);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple in) {
        if (this.isPartialAggregateInput()) {
            return this.init((Median2PartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        else {
            return new Median2PartialAggregate<Tuple<?>>(((Number) in.getAttribute(this.pos)).doubleValue());
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new Median2PartialAggregate<Tuple<?>>((Median2PartialAggregate<Tuple<?>>) in);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate p, final Tuple toMerge, final boolean createNew) {
        Median2PartialAggregate<Tuple> pa = null;
        if (createNew) {
            final Median2PartialAggregate<Tuple> h = (Median2PartialAggregate<Tuple>) p;
            pa = new Median2PartialAggregate<Tuple>(h);
        }
        else {
            pa = (Median2PartialAggregate<Tuple>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        Median2PartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final Median2PartialAggregate<Tuple<?>> h = (Median2PartialAggregate<Tuple<?>>) p;
            pa = new Median2PartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (Median2PartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final Median2PartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final Median2PartialAggregate paToMerge = (Median2PartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

}
