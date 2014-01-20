/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

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

    private final int pos;

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
     * @param p
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate p, final Tuple toMerge) {
        final MedianPartialAggregate pa = (MedianPartialAggregate) p;
        if (this.isPartialAggregateInput()) {
            return this.merge(p, (IPartialAggregate) toMerge.getAttribute(this.pos), false);
        }
        else {
            pa.add(((Number) toMerge.getAttribute(this.pos)).doubleValue());
            return pa;
        }
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

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(final IPartialAggregate p) {
        final MedianPartialAggregate pa = (MedianPartialAggregate) p;
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
