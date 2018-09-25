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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class VectorMultiplicationScalarRHSOperator extends AbstractVectorMultiplicationScalarOperator {

    /**
     *
     */
    private static final long serialVersionUID = 7719369246387464398L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS, SDFDatatype.NUMBERS };

    public VectorMultiplicationScalarRHSOperator() {
        super(VectorMultiplicationScalarRHSOperator.ACC_TYPES);
    }

    @Override
    public double[] getValue() {
        final RealVector a = new ArrayRealVector((double[]) this.getInputValue(0), false);
        final double b = this.getNumericalInputValue(1).doubleValue();
        return AbstractVectorMultiplicationScalarOperator.getValueInternal(a, b);
    }

}
