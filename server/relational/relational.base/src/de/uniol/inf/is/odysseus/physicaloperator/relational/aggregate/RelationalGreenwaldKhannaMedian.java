/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.GreenwaldKhannaMedianPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AbstractMedian;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalGreenwaldKhannaMedian extends AbstractMedian<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -6927470996153956829L;
    private int pos;

    static public RelationalGreenwaldKhannaMedian getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalGreenwaldKhannaMedian(pos, partialAggregateInput);
    }

    private RelationalGreenwaldKhannaMedian(final int pos, final boolean partialAggregateInput) {
        super("AMEDIAN", partialAggregateInput);
        this.pos = pos;
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
            GreenwaldKhannaMedianPartialAggregate<Tuple<?>> pa = new GreenwaldKhannaMedianPartialAggregate<>();
            pa.add(((Number) in.getAttribute(this.pos)).doubleValue());
            return pa;
        }
    }

    @Override
    protected IPartialAggregate<Tuple<?>> process_merge(IPartialAggregate p, Tuple toMerge) {
        GreenwaldKhannaMedianPartialAggregate pa = (GreenwaldKhannaMedianPartialAggregate) p;
        if (isPartialAggregateInput()) {
            return merge(p, (IPartialAggregate) toMerge.getAttribute(pos), false);
        }
        else {
            return pa.add(((Number) toMerge.getAttribute(pos)).doubleValue());
        }
    }

    @Override
    public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, IPartialAggregate<Tuple<?>> toMerge, boolean createNew) {
        final GreenwaldKhannaMedianPartialAggregate<Tuple<?>> pa;
        if (createNew) {
            GreenwaldKhannaMedianPartialAggregate<Tuple<?>> h = (GreenwaldKhannaMedianPartialAggregate<Tuple<?>>) p;
            pa = new GreenwaldKhannaMedianPartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (GreenwaldKhannaMedianPartialAggregate<Tuple<?>>) p;
        }
        return pa.merge((GreenwaldKhannaMedianPartialAggregate) toMerge);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(final IPartialAggregate p) {
        final GreenwaldKhannaMedianPartialAggregate<Tuple<?>> pa = (GreenwaldKhannaMedianPartialAggregate<Tuple<?>>) p;
        final Tuple r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
        return r;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFDatatype.MEDIAN_PARTIAL_AGGREGATE;
    }
}
