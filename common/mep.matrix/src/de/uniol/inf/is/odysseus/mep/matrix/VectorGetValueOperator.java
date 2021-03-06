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
public class VectorGetValueOperator extends AbstractFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = 6027555300174041670L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS, SDFDatatype.NUMBERS };

    public VectorGetValueOperator() {
        super("[]", 2, VectorGetValueOperator.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final double[] a = (double[]) this.getInputValue(0);
        final int pos = this.getNumericalInputValue(1).intValue();
        return new Double(a[pos]);
    }

}
