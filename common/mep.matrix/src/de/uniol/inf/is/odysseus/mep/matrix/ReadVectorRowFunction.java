/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ReadVectorRowFunction extends AbstractReadFunction<double[]> {

    /**
     *
     */
    private static final long serialVersionUID = 3550190318743371997L;
    private static final String DELIMITER = ",";
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.STRING }, SDFDatatype.NUMBERS };

    public ReadVectorRowFunction() {
        super("readVector", 2, ReadVectorRowFunction.ACC_TYPES, SDFDatatype.VECTOR_DOUBLE);
    }

    @Override
    public double[] getValue() {
        final String path = this.getInputValue(0);
        final int elements = this.getNumericalInputValue(1).intValue();
        return AbstractReadFunction.getValueInternal(path, ReadVectorRowFunction.DELIMITER, new int[] { elements })[0];
    }

}
