/**********************************************************************************
 * Copyright 2015 The Odysseus Team
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
import org.apache.commons.math3.linear.RealVectorPreservingVisitor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class StandardDeviationVectorFunction extends AbstractFunction<Double> {

 
    /**
     * 
     */
    private static final long serialVersionUID = -7153737469717789710L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS };
    final DescriptiveStatistics statistics = new DescriptiveStatistics();

    public StandardDeviationVectorFunction() {
        super("StdDev", 1, StandardDeviationVectorFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final RealVector a = new ArrayRealVector((double[]) this.getInputValue(0), false);
        return this.getValueInternal(a);
    }

    protected Double getValueInternal(final RealVector a) {
        return new Double(a.walkInOptimizedOrder(new RealVectorPreservingVisitor() {

            @Override
            public void start(final int dimension, final int start, final int end) {
                StandardDeviationVectorFunction.this.statistics.clear();
            }

            @Override
            public void visit(final int index, final double value) {
                StandardDeviationVectorFunction.this.statistics.addValue(value);

            }

            @Override
            public double end() {
                return StandardDeviationVectorFunction.this.statistics.getStandardDeviation();
            }

        }));
    }

}
