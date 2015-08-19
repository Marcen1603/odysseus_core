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
package cc.kuka.odysseus.signal.function.math;

import org.apache.commons.math3.complex.Complex;

import cc.kuka.odysseus.signal.common.sdf.schema.SDFSignalDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ComplexMinusOperator extends AbstractBinaryOperator<Complex> {

    /**
     *
     */
    private static final long serialVersionUID = 7909278926841189610L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSignalDatatype.COMPLEX }, { SDFSignalDatatype.COMPLEX } };

    public ComplexMinusOperator() {
        super("-", ComplexMinusOperator.accTypes, SDFSignalDatatype.COMPLEX);
    }

    @Override
    public int getPrecedence() {
        return 6;
    }

    @Override
    public Complex getValue() {
        final Complex a = (Complex) this.getInputValue(0);
        final Complex b = (Complex) this.getInputValue(1);
        return ComplexMinusOperator.getValueInternal(a, b);
    }

    private static Complex getValueInternal(final Complex a, final Complex b) {
        return new Complex(a.getReal() - b.getReal(), a.getImaginary() - b.getImaginary());

    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(final IOperator<Complex> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<Complex> operator) {
        return false;
    }

}
