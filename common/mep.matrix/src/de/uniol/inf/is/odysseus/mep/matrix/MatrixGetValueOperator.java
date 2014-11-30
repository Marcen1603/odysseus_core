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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MatrixGetValueOperator extends AbstractFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = -7381160514480433136L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS, SDFDatatype.VECTORS };

    public MatrixGetValueOperator() {
        super("[]", 2, MatrixGetValueOperator.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final double[][] a = (double[][]) this.getInputValue(0);
        final double[] pos = (double[]) this.getInputValue(1);
        if (pos.length > 1) {
            return new Double(a[(int) pos[0]][(int) pos[1]]);
        }
        else if (pos.length == 1) {
            return new Double(a[(int) pos[0]][0]);
        }
        return new Double(a[0][0]);
    }

}
