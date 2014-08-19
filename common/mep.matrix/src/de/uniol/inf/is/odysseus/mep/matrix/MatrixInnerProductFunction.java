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

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class MatrixInnerProductFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 8237101784715342239L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.MATRIXS, SDFDatatype.MATRIXS };

    public MatrixInnerProductFunction() {
        super("dotProduct", 2, accTypes, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        RealMatrix b = new Array2DRowRealMatrix((double[][]) this.getInputValue(1), false);
        return new Double(a.multiply(b.transpose()).getTrace());
    }
}
