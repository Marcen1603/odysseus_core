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
import cc.kuka.odysseus.signal.physicaloperator.aggregation.functions.FFTransformation;
import cc.kuka.odysseus.signal.physicaloperator.aggregation.functions.FFTransformationPartialAggregate;
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
public class RelationalFFTransformation extends FFTransformation<Tuple<?>, Tuple<?>> {

    /**
     *
     */
    private static final long serialVersionUID = 1402961385223885486L;
    private final int pos;

    static public RelationalFFTransformation getInstance(final int pos, final boolean partialAggregateInput) {
        return new RelationalFFTransformation(pos, partialAggregateInput);
    }

    /**
     *
     */
    public RelationalFFTransformation(final int pos, final boolean partialAggregateInput) {
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
            return this.init((FFTransformationPartialAggregate<Tuple<?>>) in.getAttribute(this.pos));
        }
        return new FFTransformationPartialAggregate<>(((Double) in.getAttribute(this.pos)));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new FFTransformationPartialAggregate<>((FFTransformationPartialAggregate<Tuple<?>>) in);
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
        FFTransformationPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final FFTransformationPartialAggregate<Tuple<?>> h = (FFTransformationPartialAggregate<Tuple<?>>) p;
            pa = new FFTransformationPartialAggregate<>(h);

        }
        else {
            pa = (FFTransformationPartialAggregate<Tuple<?>>) p;
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
        final FFTransformationPartialAggregate pa = (FFTransformationPartialAggregate) p;
        if (this.isPartialAggregateInput()) {
            return this.merge(p, (IPartialAggregate) toMerge.getAttribute(this.pos), false);
        }
        pa.add(((Double) toMerge.getAttribute(this.pos)));
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        FFTransformationPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final FFTransformationPartialAggregate<Tuple<?>> h = (FFTransformationPartialAggregate<Tuple<?>>) p;
            pa = new FFTransformationPartialAggregate<>(h);
        }
        else {
            pa = (FFTransformationPartialAggregate<Tuple<?>>) p;
        }
        return RelationalFFTransformation.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    public static IPartialAggregate<Tuple<?>> merge(final FFTransformationPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final FFTransformationPartialAggregate paToMerge = (FFTransformationPartialAggregate) toMerge;
        pa.add(paToMerge);
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Tuple evaluate(final IPartialAggregate p) {
        final FFTransformationPartialAggregate pa = (FFTransformationPartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, pa.getAggValue());
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFSignalDatatype.FFT_PARTIAL_AGGREGATE;
    }

}
