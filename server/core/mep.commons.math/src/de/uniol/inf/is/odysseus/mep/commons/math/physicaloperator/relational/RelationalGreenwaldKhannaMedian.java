/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions.GreenwaldKhannaMedianPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalGreenwaldKhannaMedian extends AbstractRelationalMedian {

    /**
     * 
     */
    private static final long serialVersionUID = -6927470996153956829L;

    static public RelationalGreenwaldKhannaMedian getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalGreenwaldKhannaMedian(pos, partialAggregateInput);
    }

    private RelationalGreenwaldKhannaMedian(final int pos, final boolean partialAggregateInput) {
        super("AMEDIAN", pos, partialAggregateInput);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple in) {
        if (this.isPartialAggregateInput()) {
            return this.init((GreenwaldKhannaMedianPartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        else {
            return new GreenwaldKhannaMedianPartialAggregate<Tuple<?>>(((Number) in.getAttribute(this.pos)).doubleValue());
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new GreenwaldKhannaMedianPartialAggregate<Tuple<?>>((GreenwaldKhannaMedianPartialAggregate<Tuple<?>>) in);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate p, final Tuple toMerge, final boolean createNew) {
        GreenwaldKhannaMedianPartialAggregate<Tuple> pa = null;
        if (createNew) {
            final GreenwaldKhannaMedianPartialAggregate<Tuple> h = (GreenwaldKhannaMedianPartialAggregate<Tuple>) p;
            pa = new GreenwaldKhannaMedianPartialAggregate<Tuple>(h);
        }
        else {
            pa = (GreenwaldKhannaMedianPartialAggregate<Tuple>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        GreenwaldKhannaMedianPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final GreenwaldKhannaMedianPartialAggregate<Tuple<?>> h = (GreenwaldKhannaMedianPartialAggregate<Tuple<?>>) p;
            pa = new GreenwaldKhannaMedianPartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (GreenwaldKhannaMedianPartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final GreenwaldKhannaMedianPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final GreenwaldKhannaMedianPartialAggregate paToMerge = (GreenwaldKhannaMedianPartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }
}
