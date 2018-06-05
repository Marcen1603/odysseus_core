/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.statistic.physicaloperator.relational;

import cc.kuka.odysseus.statistic.common.sdf.schema.SDFStatisticDatatype;
import cc.kuka.odysseus.statistic.physicaloperator.aggregate.functions.SampleSkewness;
import cc.kuka.odysseus.statistic.physicaloperator.aggregate.functions.SampleSkewnessPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RelationalSampleSkewness extends SampleSkewness<Tuple<?>, Tuple<?>> {

    /**
     *
     */
    private static final long serialVersionUID = 1436934727889518286L;
    private final int pos;

    static public RelationalSampleSkewness getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalSampleSkewness(pos, partialAggregateInput);
    }

    private RelationalSampleSkewness(final int pos, final boolean partialAggregateInput) {
        super(partialAggregateInput);
        this.pos = pos;
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple in) {
        if (this.isPartialAggregateInput()) {
            return this.init((SampleSkewnessPartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        return new SampleSkewnessPartialAggregate<>(((Number) in.getAttribute(this.pos)));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new SampleSkewnessPartialAggregate<>((SampleSkewnessPartialAggregate<Tuple<?>>) in);
    }

    /**
     *
     * @param p
     * @param toMerge
     * @param createNew
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate p, final Tuple toMerge, final boolean createNew) {
        SampleSkewnessPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final SampleSkewnessPartialAggregate<Tuple<?>> h = (SampleSkewnessPartialAggregate<Tuple<?>>) p;
            pa = new SampleSkewnessPartialAggregate<>(h);

        }
        else {
            pa = (SampleSkewnessPartialAggregate<Tuple<?>>) p;
        }
        return this.merge(pa, toMerge);
    }

    /**
     *
     * @param p
     * @param toMerge
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public IPartialAggregate<Tuple<? extends IMetaAttribute>> merge(final IPartialAggregate p, final Tuple<?> toMerge) {
        final SampleSkewnessPartialAggregate pa = (SampleSkewnessPartialAggregate) p;
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
        SampleSkewnessPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final SampleSkewnessPartialAggregate<Tuple<?>> h = (SampleSkewnessPartialAggregate<Tuple<?>>) p;
            pa = new SampleSkewnessPartialAggregate<>(h);
        }
        else {
            pa = (SampleSkewnessPartialAggregate<Tuple<?>>) p;
        }
        return RelationalSampleSkewness.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public static IPartialAggregate<Tuple<?>> merge(final SampleSkewnessPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final SampleSkewnessPartialAggregate paToMerge = (SampleSkewnessPartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Tuple evaluate(final IPartialAggregate p) {
        final SampleSkewnessPartialAggregate pa = (SampleSkewnessPartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFStatisticDatatype.SAMPLESKEWNESS_PARTIAL_AGGREGATE;
    }

}
