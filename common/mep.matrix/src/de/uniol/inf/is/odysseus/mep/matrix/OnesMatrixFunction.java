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
 *
 */
public class OnesMatrixFunction extends AbstractFunction<double[][]> {

    /**
     *
     */
    private static final long serialVersionUID = -3659254948198127788L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public OnesMatrixFunction() {
        super("ones", 2, OnesMatrixFunction.accTypes, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        final int width = this.getNumericalInputValue(0).intValue();
        final int height = this.getNumericalInputValue(1).intValue();
        return OnesMatrixFunction.getValueInternal(width, height);
    }

    protected static double[][] getValueInternal(final int width, final int height) {
        final double[][] ones = new double[height][width];
        Arrays.fill(ones, new Double(1.0));
        return ones;
    }
}
