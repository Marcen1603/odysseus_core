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
import cc.kuka.odysseus.statistic.physicaloperator.aggregate.functions.SampleStandardDeviation;
import cc.kuka.odysseus.statistic.physicaloperator.aggregate.functions.SampleStandardDeviationPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RelationalSampleStandardDeviation extends SampleStandardDeviation<Tuple<?>, Tuple<?>> {

    /**
     *
     */
    private static final long serialVersionUID = -1868826412480833487L;
    private final int pos;

    static public RelationalSampleStandardDeviation getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalSampleStandardDeviation(pos, partialAggregateInput);
    }

    private RelationalSampleStandardDeviation(final int pos, final boolean partialAggregateInput) {
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
            return this.init((SampleStandardDeviationPartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        return new SampleStandardDeviationPartialAggregate<>(((Number) in.getAttribute(this.pos)));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new SampleStandardDeviationPartialAggregate<>((SampleStandardDeviationPartialAggregate<Tuple<?>>) in);
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
        SampleStandardDeviationPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final SampleStandardDeviationPartialAggregate<Tuple<?>> h = (SampleStandardDeviationPartialAggregate<Tuple<?>>) p;
            pa = new SampleStandardDeviationPartialAggregate<>(h);

        }
        else {
            pa = (SampleStandardDeviationPartialAggregate<Tuple<?>>) p;
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
        final SampleStandardDeviationPartialAggregate pa = (SampleStandardDeviationPartialAggregate) p;
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
        SampleStandardDeviationPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final SampleStandardDeviationPartialAggregate<Tuple<?>> h = (SampleStandardDeviationPartialAggregate<Tuple<?>>) p;
            pa = new SampleStandardDeviationPartialAggregate<>(h);
        }
        else {
            pa = (SampleStandardDeviationPartialAggregate<Tuple<?>>) p;
        }
        return RelationalSampleStandardDeviation.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public static IPartialAggregate<Tuple<?>> merge(final SampleStandardDeviationPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final SampleStandardDeviationPartialAggregate paToMerge = (SampleStandardDeviationPartialAggregate) toMerge;
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
        final SampleStandardDeviationPartialAggregate pa = (SampleStandardDeviationPartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFStatisticDatatype.POPULATIONSTANDARDDEVIATION_PARTIAL_AGGREGATE;
    }

}
