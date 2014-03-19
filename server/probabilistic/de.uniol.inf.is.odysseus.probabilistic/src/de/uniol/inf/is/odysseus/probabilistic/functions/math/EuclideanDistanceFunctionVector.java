/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.functions.math;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class EuclideanDistanceFunctionVector extends AbstractEuclideanDistanceFunction {
    /**
	 * 
	 */
    private static final long serialVersionUID = 109458049612648398L;
    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.MATRIX_DOUBLE }, { SDFDatatype.MATRIX_DOUBLE } };

    public EuclideanDistanceFunctionVector() {
        super(EuclideanDistanceFunctionVector.ACC_TYPES);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final Double getValue() {
        final RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0));
        final RealMatrix b = new Array2DRowRealMatrix((double[][]) this.getInputValue(1));
        return this.getValueInternal(a, b);
    }
}
