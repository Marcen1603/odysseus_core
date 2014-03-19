/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.MinusOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.PlusOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class MatrixMultiplicationOperator extends AbstractBinaryOperator<double[][]> {

    private static final long serialVersionUID = 1646121521152263872L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.MATRIXS, SDFDatatype.MATRIXS };

    public MatrixMultiplicationOperator() {
        super("*", accTypes, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public int getPrecedence() {
        return 5;
    }

    @Override
    public double[][] getValue() {
        RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        RealMatrix b = new Array2DRowRealMatrix((double[][]) this.getInputValue(1), false);

        return getValueInternal(a, b);
    }

    protected double[][] getValueInternal(RealMatrix a, RealMatrix b) {
        return a.multiply(b).getData();
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
        return true;
    }

    @Override
    public boolean isLeftDistributiveWith(IOperator<double[][]> operator) {
        return operator.getClass() == MatrixPlusOperator.class || operator.getClass() == MatrixMinusOperator.class || operator.getClass() == PlusOperator.class
                || operator.getClass() == MinusOperator.class;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<double[][]> operator) {
        return operator.getClass() == MatrixPlusOperator.class || operator.getClass() == MatrixMinusOperator.class || operator.getClass() == PlusOperator.class
                || operator.getClass() == MinusOperator.class;
    }

}
