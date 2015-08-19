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
package cc.kuka.odysseus.polynomial.function.math;

import cc.kuka.odysseus.polynomial.datatype.Polynomial;
import cc.kuka.odysseus.polynomial.sdf.schema.SDFPolynomialDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MultiplicationOperator extends AbstractBinaryOperator<Polynomial> {

    /**
     *
     */
    private static final long serialVersionUID = 180709477732028919L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFPolynomialDatatype.POLYNOMIAL }, { SDFPolynomialDatatype.POLYNOMIAL } };

    /**
     *
     * Class constructor.
     *
     */
    public MultiplicationOperator() {
        super("*", MultiplicationOperator.accTypes, SDFDatatype.DOUBLE);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int getPrecedence() {
        return 5;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Polynomial getValue() {
        final Polynomial a = this.getInputValue(0);
        final Polynomial b = this.getInputValue(1);
        return a.multiply(b);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isCommutative() {
        return true;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isAssociative() {
        return true;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isLeftDistributiveWith(final IOperator<Polynomial> operator) {
        return (operator.getClass() == PlusOperator.class) || (operator.getClass() == MinusOperator.class);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isRightDistributiveWith(final IOperator<Polynomial> operator) {
        return (operator.getClass() == PlusOperator.class) || (operator.getClass() == MinusOperator.class);
    }

}