/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Median;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.MedianPartialAggregate;

/**
 * Estimates the median of the given attribute.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalMedian extends Median<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -7768784425424062403L;
    private int pos;

    static public RelationalMedian getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalMedian(pos, partialAggregateInput);
    }

    private RelationalMedian(final int pos, final boolean partialAggregateInput) {
        super(partialAggregateInput);
        this.pos = pos;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(Tuple in) {
        if (isPartialAggregateInput()) {
            return init((MedianPartialAggregate<Tuple<?>>) in.getAttribute(pos));
        }
        MedianPartialAggregate<Tuple<?>> pa = new MedianPartialAggregate<>();
        pa.add(((Number) in.getAttribute(this.pos)));
        return pa;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected IPartialAggregate<Tuple<?>> process_merge(IPartialAggregate p, Tuple toMerge) {
        MedianPartialAggregate pa = (MedianPartialAggregate) p;
        if (isPartialAggregateInput()) {
            return merge(p, (IPartialAggregate) toMerge.getAttribute(pos), false);
        }
        return pa.add(((Number) toMerge.getAttribute(pos)));
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, IPartialAggregate<Tuple<?>> toMerge, boolean createNew) {
        final MedianPartialAggregate<Tuple<?>> pa;
        if (createNew) {
            MedianPartialAggregate<Tuple<?>> h = (MedianPartialAggregate<Tuple<?>>) p;
            pa = new MedianPartialAggregate<>(h);
        }
        else {
            pa = (MedianPartialAggregate<Tuple<?>>) p;
        }
        return pa.merge((MedianPartialAggregate) toMerge);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(IPartialAggregate p) {
        MedianPartialAggregate pa = (MedianPartialAggregate) p;
        Tuple r = new Tuple(1, false);
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
