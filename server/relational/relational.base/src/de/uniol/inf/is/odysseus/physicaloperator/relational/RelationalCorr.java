/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CorrelationPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.StandardDeviation;

/**
 * Estimates the correlation coefficient between two attributes.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalCorr extends StandardDeviation<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -5878264128156808625L;
    private final int posA;
    private int posB;

    static public RelationalCorr getInstance(final int posA, final int posB, final boolean partialAggregateInput) {
        return new RelationalCorr(posA, posB, partialAggregateInput);
    }

    static public RelationalCorr getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalCorr(pos, partialAggregateInput);
    }

    private RelationalCorr(final int posA, final int posB, final boolean partialAggregateInput) {
        super(partialAggregateInput);
        this.posA = posA;
        this.posB = posB;
    }

    private RelationalCorr(final int pos, final boolean partialAggregateInput) {
        super(partialAggregateInput);
        this.posA = pos;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple in) {
        if (this.isPartialAggregateInput()) {
            return this.init((CorrelationPartialAggregate<Tuple<?>>) in.getAttribute(this.posA));
        }
        else {
            return new CorrelationPartialAggregate<Tuple<?>>(((Number) in.getAttribute(this.posA)).doubleValue(), ((Number) in.getAttribute(this.posB)).doubleValue());
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new CorrelationPartialAggregate<Tuple<?>>((CorrelationPartialAggregate<Tuple<?>>) in);
    }

    /**
     * 
     * @param p
     * @param toMerge
     * @param createNew
     * @return
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate p, final Tuple toMerge, final boolean createNew) {
        CorrelationPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final CorrelationPartialAggregate<Tuple<?>> h = (CorrelationPartialAggregate<Tuple<?>>) p;
            pa = new CorrelationPartialAggregate<Tuple<?>>(h);

        }
        else {
            pa = (CorrelationPartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * 
     * @param p
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<? extends IMetaAttribute>> merge(final IPartialAggregate p, final Tuple<?> toMerge) {
        final CorrelationPartialAggregate pa = (CorrelationPartialAggregate) p;
        if (this.isPartialAggregateInput()) {
            return this.merge(p, (IPartialAggregate) toMerge.getAttribute(this.posA), false);
        }
        else {
            pa.add(((Number) toMerge.getAttribute(this.posA)).doubleValue(), ((Number) toMerge.getAttribute(this.posB)).doubleValue());
            return pa;
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        CorrelationPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final CorrelationPartialAggregate<Tuple<?>> h = (CorrelationPartialAggregate<Tuple<?>>) p;
            pa = new CorrelationPartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (CorrelationPartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final CorrelationPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final CorrelationPartialAggregate paToMerge = (CorrelationPartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(final IPartialAggregate p) {
        final CorrelationPartialAggregate pa = (CorrelationPartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFDatatype.CORR_PARTIAL_AGGREGATE;
    }

}
