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
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ZerosMatrixFunction extends AbstractFunction<double[][]> {

    /**
     *
     */
    private static final long serialVersionUID = -6557201597052503221L;
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public ZerosMatrixFunction() {
        super("zeros", 2, ZerosMatrixFunction.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        final int width = this.getNumericalInputValue(0).intValue();
        final int height = this.getNumericalInputValue(1).intValue();
        return ZerosMatrixFunction.getValueInternal(width, height);
    }

    protected static double[][] getValueInternal(final int width, final int height) {
        final double[][] zeros = new double[height][width];
        Arrays.fill(zeros, new Double(0.0));
        return zeros;
    }
}
