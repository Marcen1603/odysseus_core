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
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MinMatrixFunction extends AbstractFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = 8985079762992918561L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    public MinMatrixFunction() {
        super("sMin", 1, MinMatrixFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        return MinMatrixFunction.getValueInternal(a);
    }

    protected static Double getValueInternal(final RealMatrix a) {
        return new Double(a.walkInOptimizedOrder(new RealMatrixPreservingVisitor() {
            private double min;

            @Override
            public void start(final int rows, final int columns, final int startRow, final int endRow, final int startColumn, final int endColumn) {
                this.min = Double.MAX_VALUE;
            }

            @Override
            public void visit(final int row, final int column, final double value) {
                this.min = FastMath.min(this.min, value);

            }

            @Override
            public double end() {
                return this.min;
            }

        }));
    }

}
