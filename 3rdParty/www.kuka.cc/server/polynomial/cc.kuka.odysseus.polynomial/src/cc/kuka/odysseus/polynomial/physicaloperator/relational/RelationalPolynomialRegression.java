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
package cc.kuka.odysseus.polynomial.physicaloperator.relational;

import cc.kuka.odysseus.polynomial.physicaloperator.aggregate.functions.PolynomialRegression;
import cc.kuka.odysseus.polynomial.physicaloperator.aggregate.functions.PolynomialRegressionPartialAggregate;
import cc.kuka.odysseus.polynomial.sdf.schema.SDFPolynomialDatatype;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RelationalPolynomialRegression extends PolynomialRegression<Tuple<?>, Tuple<?>> {

    /**
     *
     */
    private static final long serialVersionUID = 6333486056142137645L;
    private final int posX;
    private final int posY;

    static public RelationalPolynomialRegression getInstance(final int posX, final int posY, final boolean partialAggregateInput) {
        return new RelationalPolynomialRegression(posX, posY, partialAggregateInput);
    }

    private RelationalPolynomialRegression(final int posX, final int posY, final boolean partialAggregateInput) {
        super(partialAggregateInput);
        this.posX = posX;
        this.posY = posY;

    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple in) {
        if (this.isPartialAggregateInput()) {
            return this.init((PolynomialRegressionPartialAggregate<Tuple<?>>) in.getAttribute(this.posX));
        }
        return new PolynomialRegressionPartialAggregate<>(((Number) in.getAttribute(this.posX)), ((Number) in.getAttribute(this.posY)));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> init(final IPartialAggregate<Tuple<?>> in) {
        return new PolynomialRegressionPartialAggregate<>((PolynomialRegressionPartialAggregate<Tuple<?>>) in);
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
        PolynomialRegressionPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final PolynomialRegressionPartialAggregate<Tuple<?>> h = (PolynomialRegressionPartialAggregate<Tuple<?>>) p;
            pa = new PolynomialRegressionPartialAggregate<>(h);

        }
        else {
            pa = (PolynomialRegressionPartialAggregate<Tuple<?>>) p;
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
        final PolynomialRegressionPartialAggregate pa = (PolynomialRegressionPartialAggregate) p;
        if (this.isPartialAggregateInput()) {
            return this.merge(p, (IPartialAggregate) toMerge.getAttribute(this.posX), false);
        }
        pa.add(((Number) toMerge.getAttribute(this.posX)), ((Number) toMerge.getAttribute(this.posY)));
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final IPartialAggregate<Tuple<?>> toMerge, final boolean createNew) {
        PolynomialRegressionPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            final PolynomialRegressionPartialAggregate<Tuple<?>> h = (PolynomialRegressionPartialAggregate<Tuple<?>>) p;
            pa = new PolynomialRegressionPartialAggregate<>(h);
        }
        else {
            pa = (PolynomialRegressionPartialAggregate<Tuple<?>>) p;
        }
        return RelationalPolynomialRegression.merge(pa, toMerge);
    }

    /**
     * @param pa
     * @param toMerge
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public static IPartialAggregate<Tuple<?>> merge(final PolynomialRegressionPartialAggregate<Tuple<?>> pa, final IPartialAggregate<Tuple<?>> toMerge) {
        final PolynomialRegressionPartialAggregate paToMerge = (PolynomialRegressionPartialAggregate) toMerge;
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
        final PolynomialRegressionPartialAggregate pa = (PolynomialRegressionPartialAggregate) p;
        final Tuple<? extends IMetaAttribute> r = new Tuple(1, false);
        r.setAttribute(0, pa.getAggValue());
        return r;
    }

    @Override
    public SDFDatatype getPartialAggregateType() {
        return SDFPolynomialDatatype.POLYNOMIALREGRESSION_PARTIAL_AGGREGATE;
    }
}
