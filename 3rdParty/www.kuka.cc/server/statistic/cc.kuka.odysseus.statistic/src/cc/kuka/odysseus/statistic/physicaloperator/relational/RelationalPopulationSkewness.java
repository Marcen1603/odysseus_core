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
import cc.kuka.odysseus.statistic.physicaloperator.aggregate.functions.PopulationSkewness;
import cc.kuka.odysseus.statistic.physicaloperator.aggregate.functions.PopulationSkewnessPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RelationalPopulationSkewness extends PopulationSkewness<Tuple<?>, Tuple<?>> {

    /**
     *
     */
    private static final long serialVersionUID = -4602964341988074743L;
    private final int pos;

    static public RelationalPopulationSkewness getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalPopulationSkewness(pos, partialAggregateInput);
    }

    private RelationalPopulationSkewness(final int pos, final boolean partialAggregateInput) {
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
            return this.init((PopulationSkewnessPartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        return new PopulationSkewnessPartialAggregate<>(((Number) in.getAttribute(this.pos)));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new PopulationSkewnessPartialAggregate<>((PopulationSkewnessPartialAggregate<Tuple<?>>) in);
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
        PopulationSkewnessPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final PopulationSkewnessPartialAggregate<Tuple<?>> h = (PopulationSkewnessPartialAggregate<Tuple<?>>) p;
            pa = new PopulationSkewnessPartialAggregate<>(h);

        }
        else {
            pa = (PopulationSkewnessPartialAggregate<Tuple<?>>) p;
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
        final PopulationSkewnessPartialAggregate pa = (PopulationSkewnessPartialAggregate) p;
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
        PopulationSkewnessPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final PopulationSkewnessPartialAggregate<Tuple<?>> h = (PopulationSkewnessPartialAggregate<Tuple<?>>) p;
            pa = new PopulationSkewnessPartialAggregate<>(h);
        }
        else {
            pa = (PopulationSkewnessPartialAggregate<Tuple<?>>) p;
        }
        return RelationalPopulationSkewness.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public static IPartialAggregate<Tuple<?>> merge(final PopulationSkewnessPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final PopulationSkewnessPartialAggregate paToMerge = (PopulationSkewnessPartialAggregate) toMerge;
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
        final PopulationSkewnessPartialAggregate pa = (PopulationSkewnessPartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFStatisticDatatype.POPULATIONSKEWNESS_PARTIAL_AGGREGATE;
    }

}
