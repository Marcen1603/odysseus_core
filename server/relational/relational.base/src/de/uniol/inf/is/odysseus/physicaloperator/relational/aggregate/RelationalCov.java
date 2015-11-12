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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Covariance;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CovariancePartialAggregate;

/**
 * Estimates the covariance between two attributes.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalCov extends Covariance<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -5675774570393031124L;
    private final int posA;
    private final int posB;

    static public RelationalCov getInstance(final int posA, final int posB, final boolean partialAggregateInput) {
        return new RelationalCov(posA, posB, partialAggregateInput);
    }

    private RelationalCov(final int posA, final int posB, final boolean partialAggregateInput) {
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
            return this.init((CovariancePartialAggregate<Tuple<?>>) in.getAttribute(this.posA));
        }
        return new CovariancePartialAggregate<>(((Number) in.getAttribute(this.posA)), ((Number) in.getAttribute(this.posB)));
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new CovariancePartialAggregate<>((CovariancePartialAggregate<Tuple<?>>) in);
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
        CovariancePartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final CovariancePartialAggregate<Tuple<?>> h = (CovariancePartialAggregate<Tuple<?>>) p;
            pa = new CovariancePartialAggregate<>(h);

        }
        else {
            pa = (CovariancePartialAggregate<Tuple<?>>) p;
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
        final CovariancePartialAggregate pa = (CovariancePartialAggregate) p;
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
        CovariancePartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final CovariancePartialAggregate<Tuple<?>> h = (CovariancePartialAggregate<Tuple<?>>) p;
            pa = new CovariancePartialAggregate<>(h);
        }
        else {
            pa = (CovariancePartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final CovariancePartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final CovariancePartialAggregate paToMerge = (CovariancePartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(final IPartialAggregate p) {
        final CovariancePartialAggregate pa = (CovariancePartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFDatatype.COV_PARTIAL_AGGREGATE;
    }

}
