/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

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
