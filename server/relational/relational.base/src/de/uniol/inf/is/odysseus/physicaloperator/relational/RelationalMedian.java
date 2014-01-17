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

    static public RelationalMedian getInstance(int pos, boolean partialAggregateInput) {
        return new RelationalMedian(pos, partialAggregateInput);
    }

    private RelationalMedian(int pos, boolean partialAggregateInput) {
        super(partialAggregateInput);
        this.pos = pos;
    }

    @Override
    public IPartialAggregate<Tuple<?>> init(Tuple in) {
        if (isPartialAggregateInput()) {
            return init((MedianPartialAggregate<Tuple<?>>) in.getAttribute(pos));
        }
        else {
            return new MedianPartialAggregate<Tuple<?>>(((Number) in.getAttribute(pos)).doubleValue());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
     * .AbstractAggregateFunction#init(de.uniol.inf.is.odysseus.core.server.
     * physicaloperator.aggregate.basefunctions.IPartialAggregate)
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(IPartialAggregate<Tuple<?>> in) {
        return new MedianPartialAggregate<Tuple<?>>((MedianPartialAggregate<Tuple<?>>) in);
    }

    @Override
    public IPartialAggregate<Tuple<?>> merge(IPartialAggregate p, Tuple toMerge, boolean createNew) {
        MedianPartialAggregate<Tuple> pa = null;
        if (createNew) {
            MedianPartialAggregate<Tuple> h = (MedianPartialAggregate<Tuple>) p;
            pa = new MedianPartialAggregate<Tuple>(h.getValues());

        }
        else {
            pa = (MedianPartialAggregate<Tuple>) p;
        }
        return merge(pa, toMerge);
    }

    public IPartialAggregate<Tuple<?>> merge(IPartialAggregate p, Tuple toMerge) {
        MedianPartialAggregate pa = (MedianPartialAggregate) p;
        if (isPartialAggregateInput()) {
            return merge(p, (IPartialAggregate) toMerge.getAttribute(pos), false);
        }
        else {
            pa.add(((Number) toMerge.getAttribute(pos)).doubleValue());
            return pa;
        }
    }

    @Override
    public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, IPartialAggregate<Tuple<?>> toMerge, boolean createNew) {
        MedianPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            MedianPartialAggregate<Tuple<?>> h = (MedianPartialAggregate<Tuple<?>>) p;
            pa = new MedianPartialAggregate<Tuple<?>>(h.getValues());
        }
        else {
            pa = (MedianPartialAggregate<Tuple<?>>) p;
        }
        return merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(MedianPartialAggregate<Tuple<?>> pa, IPartialAggregate<Tuple<?>> toMerge) {
        MedianPartialAggregate paToMerge = (MedianPartialAggregate) toMerge;
        pa.add(paToMerge.getAggValue().doubleValue());
        return pa;
    }

    @Override
    public Tuple evaluate(IPartialAggregate p) {
        MedianPartialAggregate pa = (MedianPartialAggregate) p;
        Tuple r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFDatatype.MEDIAN_PARTIAL_AGGREGATE;
    }

}
