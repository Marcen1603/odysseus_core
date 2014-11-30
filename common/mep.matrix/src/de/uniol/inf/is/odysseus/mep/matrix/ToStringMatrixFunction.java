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
 *
 */
public class ToStringMatrixFunction extends AbstractFunction<String> {

    /**
     *
     */
    private static final long serialVersionUID = -7960728964867547435L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    public ToStringMatrixFunction() {
        super("toString", 1, ToStringMatrixFunction.accTypes, SDFDatatype.STRING);
    }

    @Override
    public String getValue() {
        final double[][] data = (double[][]) this.getInputValue(0);
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < data.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("{");
            for (int j = 0; j < data[i].length; j++) {
                if (j != 0) {
                    sb.append(", ");
                }
                sb.append(data[i][j]);
            }
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }
}
