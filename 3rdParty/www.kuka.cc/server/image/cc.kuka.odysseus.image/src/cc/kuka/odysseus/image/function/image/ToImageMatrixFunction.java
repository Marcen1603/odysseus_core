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

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ToImageMatrixFunction extends AbstractFunction<BufferedImage> {
    /**
     *
     */
    private static final long serialVersionUID = 8688410794703166746L;

    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    /**
     *
     */
    public ToImageMatrixFunction() {
        super("toImage", 1, ToImageMatrixFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getValue() {
        final double data[][] = this.getInputValue(0);
        Objects.requireNonNull(data);
        Preconditions.checkArgument(data.length > 0, "Invalid dimension");
        Preconditions.checkArgument(data[0].length > 0, "Invalid dimension");
        final BufferedImage image = new BufferedImage(data[0].length, data.length, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, buffer, i * image.getWidth(), data[i].length);
        }
        return image;
    }
}
