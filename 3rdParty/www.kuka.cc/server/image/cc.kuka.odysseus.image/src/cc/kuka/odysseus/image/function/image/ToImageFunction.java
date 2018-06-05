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

import cc.kuka.odysseus.image.common.sdf.schema.SDFImageDatatype;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ToImageFunction extends AbstractFunction<BufferedImage> {
    /**
     *
     */
    private static final long serialVersionUID = -6078416764818576545L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    /**
     *
     */
    public ToImageFunction() {
        super("toImage", 2, ToImageFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getValue() {
        final int width = this.getNumericalInputValue(0).intValue();
        final int height = this.getNumericalInputValue(1).intValue();
        Preconditions.checkArgument(width > 0, "Invalid dimension");
        Preconditions.checkArgument(height > 0, "Invalid dimension");
        return new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    }
}
