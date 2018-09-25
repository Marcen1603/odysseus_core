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

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class VectorEqualsOperator extends AbstractBinaryOperator<Boolean> {

    /**
     *
     */
    private static final long serialVersionUID = -4984686987986207533L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS, SDFDatatype.VECTORS };

    public VectorEqualsOperator() {
        super("==", VectorEqualsOperator.ACC_TYPES, SDFDatatype.BOOLEAN);
    }

    @Override
    public int getPrecedence() {
        return 9;
    }

    @Override
    public Boolean getValue() {
        final double[] a = (double[]) this.getInputValue(0);
        final double[] b = (double[]) this.getInputValue(1);
        return new Boolean(Arrays.equals(a, b));
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(final IOperator<Boolean> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<Boolean> operator) {
        return false;
    }
}
