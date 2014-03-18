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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class VectorSMaxFunction extends AbstractFunction<Double> {

    private static final long serialVersionUID = 7768144136126905733L;
    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.VECTOR_BOOLEAN, SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT, SDFDatatype.VECTOR_DOUBLE };

    public VectorSMaxFunction() {
    	super("sMax",1,accTypes,SDFDatatype.DOUBLE);
    }
    
    @Override
    public Double getValue() {
        RealVector a = MatrixUtils.createRealVector((double[]) this.getInputValue(0));
        return getValueInternal(a);
    }

    protected double getValueInternal(RealVector a) {
        return a.getMaxValue();
    }
}
