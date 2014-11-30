/**********************************************************************************
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public abstract class AbstractVectorDivisionScalarOperator extends AbstractBinaryOperator<double[]> {

    /**
     *
     */
    private static final long serialVersionUID = -2687853079472500695L;

    public AbstractVectorDivisionScalarOperator(final SDFDatatype[][] accTypes) {
        super("/", accTypes, SDFDatatype.VECTOR_DOUBLE);
    }

    @Override
    public int getPrecedence() {
        return 5;
    }

    protected static double[] getValueInternal(final RealVector a, final double b) {
        return a.mapDivide(b).toArray();
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
    public boolean isLeftDistributiveWith(final IOperator<double[]> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<double[]> operator) {
        return false;
    }

}
