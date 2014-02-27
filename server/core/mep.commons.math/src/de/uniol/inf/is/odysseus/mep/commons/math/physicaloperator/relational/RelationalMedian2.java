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

    @Override
    public IPartialAggregate<Tuple<?>> init(Tuple in) {
        if (isPartialAggregateInput()) {
            return init((Median2PartialAggregate<Tuple<?>>) in.getAttribute(pos));
        }
        else {
            Median2PartialAggregate<Tuple<?>> pa = new Median2PartialAggregate<Tuple<?>>();
            pa.add(((Number) in.getAttribute(this.pos)).doubleValue());
            return pa;
        }
    }

    @Override
    protected IPartialAggregate<Tuple<?>> process_merge(IPartialAggregate p, Tuple toMerge) {
        Median2PartialAggregate pa = (Median2PartialAggregate) p;
        if (isPartialAggregateInput()) {
            return merge(p, (IPartialAggregate) toMerge.getAttribute(pos), false);
        }
        else {
            return pa.add(((Number) toMerge.getAttribute(pos)).doubleValue());
        }
    }

    @Override
    public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, IPartialAggregate<Tuple<?>> toMerge, boolean createNew) {
        Median2PartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            Median2PartialAggregate<Tuple<?>> h = (Median2PartialAggregate<Tuple<?>>) p;
            pa = new Median2PartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (Median2PartialAggregate<Tuple<?>>) p;
        }
        return pa.merge((Median2PartialAggregate) toMerge);
    }
}
