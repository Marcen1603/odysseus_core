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
import java.util.Objects;

import cc.kuka.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SetImageFunction extends AbstractFunction<BufferedImage> {

    /**
     *
     */
    private static final long serialVersionUID = -7634166024232641502L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE }, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    /**
     *
     */
    public SetImageFunction() {
        super("set", 4, SetImageFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    @Override
    public BufferedImage getValue() {
        final BufferedImage image = (BufferedImage) this.getInputValue(0);
        final int x = this.getNumericalInputValue(1).intValue();
        final int y = this.getNumericalInputValue(2).intValue();
        final int value = this.getNumericalInputValue(3).intValue();

        Objects.requireNonNull(image);

        final BufferedImage result = new BufferedImage(image.getColorModel(), image.copyData(null), image.isAlphaPremultiplied(), null);

        result.setRGB(x, y, value);
        return result;
    }
}
