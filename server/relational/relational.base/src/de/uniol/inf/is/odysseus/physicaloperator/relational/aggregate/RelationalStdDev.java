/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.StandardDeviation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.StandardDeviationPartialAggregate;

/**
 * Estimates the standard deviation of the given attribute.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalStdDev extends StandardDeviation<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -7920248360169482197L;
    private final int pos;

    static public RelationalStdDev getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalStdDev(pos, partialAggregateInput);
    }

    private RelationalStdDev(final int pos, final boolean partialAggregateInput) {
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
            return this.init((StandardDeviationPartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        else {
            return new StandardDeviationPartialAggregate<Tuple<?>>(((Number) in.getAttribute(this.pos)));
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new StandardDeviationPartialAggregate<Tuple<?>>((StandardDeviationPartialAggregate<Tuple<?>>) in);
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
        StandardDeviationPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final StandardDeviationPartialAggregate<Tuple<?>> h = (StandardDeviationPartialAggregate<Tuple<?>>) p;
            pa = new StandardDeviationPartialAggregate<Tuple<?>>(h);

        }
        else {
            pa = (StandardDeviationPartialAggregate<Tuple<?>>) p;
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
        final StandardDeviationPartialAggregate pa = (StandardDeviationPartialAggregate) p;
        if (this.isPartialAggregateInput()) {
            return this.merge(p, (IPartialAggregate) toMerge.getAttribute(this.pos), false);
        }
        else {
            pa.add(((Number) toMerge.getAttribute(this.pos)));
            return pa;
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        StandardDeviationPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final StandardDeviationPartialAggregate<Tuple<?>> h = (StandardDeviationPartialAggregate<Tuple<?>>) p;
            pa = new StandardDeviationPartialAggregate<Tuple<?>>(h);
        }
        else {
            pa = (StandardDeviationPartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    public IPartialAggregate<Tuple<?>> merge(final StandardDeviationPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final StandardDeviationPartialAggregate paToMerge = (StandardDeviationPartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(final IPartialAggregate p) {
        final StandardDeviationPartialAggregate pa = (StandardDeviationPartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFDatatype.STDDEV_PARTIAL_AGGREGATE;
    }

}
