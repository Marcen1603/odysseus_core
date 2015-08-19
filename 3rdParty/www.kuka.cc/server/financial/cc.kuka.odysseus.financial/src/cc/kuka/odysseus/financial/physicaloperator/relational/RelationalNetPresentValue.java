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
package cc.kuka.odysseus.financial.physicaloperator.relational;

import cc.kuka.odysseus.financial.common.sdf.schema.SDFFinancialDatatype;
import cc.kuka.odysseus.financial.physicaloperator.aggregate.functions.NetPresentValue;
import cc.kuka.odysseus.financial.physicaloperator.aggregate.functions.NetPresentValuePartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RelationalNetPresentValue extends NetPresentValue<Tuple<?>, Tuple<?>> {

    /**
     *
     */
    private static final long serialVersionUID = -7266160036734556366L;
    private final int posX;
    private final int posR;

    static public RelationalNetPresentValue getInstance(final int posX, final int posR, final boolean partialAggregateInput) {
        return new RelationalNetPresentValue(posX, posR, partialAggregateInput);
    }

    private RelationalNetPresentValue(final int posX, final int posR, final boolean partialAggregateInput) {
        super(partialAggregateInput);
        this.posX = posX;
        this.posR = posR;
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple in) {
        if (this.isPartialAggregateInput()) {
            return this.init((NetPresentValuePartialAggregate<Tuple<?>>) in.getAttribute(this.posX));
        }
        return new NetPresentValuePartialAggregate<>(((Number) in.getAttribute(this.posX)), ((Number) in.getAttribute(this.posR)));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new NetPresentValuePartialAggregate<>((NetPresentValuePartialAggregate<Tuple<?>>) in);
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
        NetPresentValuePartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final NetPresentValuePartialAggregate<Tuple<?>> h = (NetPresentValuePartialAggregate<Tuple<?>>) p;
            pa = new NetPresentValuePartialAggregate<>(h);

        }
        else {
            pa = (NetPresentValuePartialAggregate<Tuple<?>>) p;
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
        final NetPresentValuePartialAggregate pa = (NetPresentValuePartialAggregate) p;
        if (this.isPartialAggregateInput()) {
            return this.merge(p, (IPartialAggregate) toMerge.getAttribute(this.posX), false);
        }
        pa.add(((Number) toMerge.getAttribute(this.posX)), ((Number) toMerge.getAttribute(this.posR)));
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        NetPresentValuePartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final NetPresentValuePartialAggregate<Tuple<?>> h = (NetPresentValuePartialAggregate<Tuple<?>>) p;
            pa = new NetPresentValuePartialAggregate<>(h);
        }
        else {
            pa = (NetPresentValuePartialAggregate<Tuple<?>>) p;
        }
        return RelationalNetPresentValue.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public static IPartialAggregate<Tuple<?>> merge(final NetPresentValuePartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final NetPresentValuePartialAggregate paToMerge = (NetPresentValuePartialAggregate) toMerge;
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
        final NetPresentValuePartialAggregate pa = (NetPresentValuePartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, new Double(pa.getAggValue()));
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFFinancialDatatype.NETPRESENTVALUE_PARTIAL_AGGREGATE;
    }
}
