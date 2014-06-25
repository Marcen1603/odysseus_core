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
package de.uniol.inf.is.odysseus.image.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToMatrixFunction extends AbstractFunction<double[][]> {

    /**
     * 
     */
    private static final long serialVersionUID = -6326139668233800441L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE } };

    /**
 * 
 */
    public ToMatrixFunction() {
        super("toMatrix", 1, ToMatrixFunction.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[][] getValue() {
        final Image image = this.getInputValue(0);
        Objects.requireNonNull(image);
        double[][] matrix = new double[image.getWidth()][image.getHeight()];
        double[] buffer = image.getBuffer();
        for (int i = 0; i < image.getHeight(); i++) {
            System.arraycopy(buffer, i * image.getWidth(), matrix[i], 0, image.getWidth());
        }
        return matrix;
    }

}
