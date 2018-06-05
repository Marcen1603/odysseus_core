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
package cc.kuka.odysseus.signal.physicaloperator.relational;

import cc.kuka.odysseus.signal.common.sdf.schema.SDFSignalDatatype;
import cc.kuka.odysseus.signal.physicaloperator.aggregation.functions.SpectralCentroid;
import cc.kuka.odysseus.signal.physicaloperator.aggregation.functions.SpectralCentroidPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalSpectralCentroid extends SpectralCentroid<Tuple<?>, Tuple<?>> {

    /**
     *
     */
    private static final long serialVersionUID = 1488966235231157809L;
    private final int posA;
    private final int posB;

    static public RelationalSpectralCentroid getInstance(final int posA, final int posB, final boolean partialAggregateInput) {
        return new RelationalSpectralCentroid(posA, posB, partialAggregateInput);
    }

    /**
     *
     */
    public RelationalSpectralCentroid(final int posA, final int posB, final boolean partialAggregateInput) {
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
            return this.init((SpectralCentroidPartialAggregate<Tuple<?>>) in.getAttribute(this.posA));
        }
        return new SpectralCentroidPartialAggregate<>(((Double) in.getAttribute(this.posA)), ((Double) in.getAttribute(this.posB)));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new SpectralCentroidPartialAggregate<>((SpectralCentroidPartialAggregate<Tuple<?>>) in);
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
        SpectralCentroidPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final SpectralCentroidPartialAggregate<Tuple<?>> h = (SpectralCentroidPartialAggregate<Tuple<?>>) p;
            pa = new SpectralCentroidPartialAggregate<>(h);

        }
        else {
            pa = (SpectralCentroidPartialAggregate<Tuple<?>>) p;
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
        final SpectralCentroidPartialAggregate pa = (SpectralCentroidPartialAggregate) p;
        if (this.isPartialAggregateInput()) {
            return this.merge(p, (IPartialAggregate) toMerge.getAttribute(this.posA), false);
        }
        pa.add(((Double) toMerge.getAttribute(this.posA)), ((Double) toMerge.getAttribute(this.posB)));
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        SpectralCentroidPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final SpectralCentroidPartialAggregate<Tuple<?>> h = (SpectralCentroidPartialAggregate<Tuple<?>>) p;
            pa = new SpectralCentroidPartialAggregate<>(h);
        }
        else {
            pa = (SpectralCentroidPartialAggregate<Tuple<?>>) p;
        }
        return RelationalSpectralCentroid.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    public static IPartialAggregate<Tuple<?>> merge(final SpectralCentroidPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final SpectralCentroidPartialAggregate paToMerge = (SpectralCentroidPartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(final IPartialAggregate p) {
        final SpectralCentroidPartialAggregate pa = (SpectralCentroidPartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFSignalDatatype.SPECTRAL_CENTROID_PARTIAL_AGGREGATE;
    }

}
