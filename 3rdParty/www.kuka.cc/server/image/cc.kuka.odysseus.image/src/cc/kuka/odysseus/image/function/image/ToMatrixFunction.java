/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
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
package cc.kuka.odysseus.image.function.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Objects;

import cc.kuka.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
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
        final BufferedImage image = this.getInputValue(0);
        Objects.requireNonNull(image);
        final double[][] matrix = new double[image.getWidth()][image.getHeight()];

        final byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < image.getHeight(); i++) {
            System.arraycopy(buffer, i * image.getWidth(), matrix[i], 0, image.getWidth());
        }
        return matrix;
    }

}
