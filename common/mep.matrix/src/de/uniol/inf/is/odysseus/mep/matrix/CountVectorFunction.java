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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class CountVectorFunction extends AbstractFunction<Integer> {

    private static final long serialVersionUID = 3872260101379886451L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS };

    public CountVectorFunction() {
        super("Count", 1, CountVectorFunction.ACC_TYPES, SDFDatatype.INTEGER);
    }

    @Override
    public Integer getValue() {
        final RealVector a = new ArrayRealVector((double[]) this.getInputValue(0), false);
        return CountVectorFunction.getValueInternal(a);
    }

    protected static Integer getValueInternal(final RealVector a) {
        return new Integer(a.getDimension());
    }

}
