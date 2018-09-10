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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Variance;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.VariancePartialAggregate;

/**
 * Estimates the variance of an attribute.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalVar extends Variance<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = 6589183953860990158L;
    private final int pos;

    static public RelationalVar getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalVar(pos, partialAggregateInput);
    }

    private RelationalVar(final int pos, final boolean partialAggregateInput) {
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
            return this.init((VariancePartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        return new VariancePartialAggregate<>(((Number) in.getAttribute(this.pos)));
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new VariancePartialAggregate<>((VariancePartialAggregate<Tuple<?>>) in);
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
        VariancePartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final VariancePartialAggregate<Tuple<?>> h = (VariancePartialAggregate<Tuple<?>>) p;
            pa = new VariancePartialAggregate<>(h);

        }
        else {
            pa = (VariancePartialAggregate<Tuple<?>>) p;
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
        final VariancePartialAggregate pa = (VariancePartialAggregate) p;
        if (this.isPartialAggregateInput()) {
            return this.merge(p, (IPartialAggregate) toMerge.getAttribute(this.pos), false);
        }
        pa.add(((Number) toMerge.getAttribute(this.pos)));
        return pa;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        VariancePartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final VariancePartialAggregate<Tuple<?>> h = (VariancePartialAggregate<Tuple<?>>) p;
            pa = new VariancePartialAggregate<>(h);
        }
        else {
            pa = (VariancePartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final VariancePartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final VariancePartialAggregate paToMerge = (VariancePartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(final IPartialAggregate p) {
        final VariancePartialAggregate pa = (VariancePartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFDatatype.VAR_PARTIAL_AGGREGATE;
    }

}
