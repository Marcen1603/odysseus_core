/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Correlation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CorrelationPartialAggregate;

/**
 * Estimates the correlation coefficient between two attributes.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalCorr extends Correlation<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -5878264128156808625L;
    private final int posA;
    private final int posB;

    static public RelationalCorr getInstance(final int posA, final int posB, final boolean partialAggregateInput) {
        return new RelationalCorr(posA, posB, partialAggregateInput);
    }

    private RelationalCorr(final int posA, final int posB, final boolean partialAggregateInput) {
        super(partialAggregateInput);
        this.posA = posA;
        this.posB = posB;
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
        return new CorrelationPartialAggregate<>(((Number) in.getAttribute(this.posA)), ((Number) in.getAttribute(this.posB)));
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new CorrelationPartialAggregate<>((CorrelationPartialAggregate<Tuple<?>>) in);
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
            pa = new CorrelationPartialAggregate<>(h);

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
        pa.add(((Number) toMerge.getAttribute(this.posA)), ((Number) toMerge.getAttribute(this.posB)));
        return pa;
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
            pa = new CorrelationPartialAggregate<>(h);
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
