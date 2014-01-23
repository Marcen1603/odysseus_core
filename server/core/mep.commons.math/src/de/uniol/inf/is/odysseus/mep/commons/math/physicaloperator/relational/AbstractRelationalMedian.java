/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions.IMedianPartialAggregate;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions.Median;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
abstract public class AbstractRelationalMedian extends Median<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = 1124186345445345701L;
    protected int pos;

    /**
     * 
     */
    public AbstractRelationalMedian(final String name, final int pos, final boolean partialAggregateInput) {
        super(name, partialAggregateInput);
        this.pos = pos;
    }

    public IPartialAggregate<Tuple<?>> merge(final IMedianPartialAggregate p, final Tuple<?> toMerge) {
        final IMedianPartialAggregate pa = (IMedianPartialAggregate) p;
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
    public Tuple evaluate(final IPartialAggregate p) {
        final IMedianPartialAggregate pa = (IMedianPartialAggregate) p;
        final Tuple r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
        pa.clear();
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
