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

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorPreservingVisitor;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class VectorSAVGFunction extends AbstractFunction<Double> {


    private static final long serialVersionUID = -8567153407682230931L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.VECTORS };

    public VectorSAVGFunction() {
    	super("sAVG",1,accTypes,SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        RealVector a = MatrixUtils.createRealVector((double[]) this.getInputValue(0));
        return getValueInternal(a);
    }

    protected double getValueInternal(RealVector a) {
        return a.walkInDefaultOrder(new RealVectorPreservingVisitor() {
            private double sum;
            private int count;

            @Override
            public void start(int dimension, int start, int end) {
                sum = 0.0;
                count = 0;
            }

            @Override
            public void visit(int index, double value) {
                sum += value;
                count++;
            }

            @Override
            public double end() {
                return sum / count;
            }
        });
    }
}
