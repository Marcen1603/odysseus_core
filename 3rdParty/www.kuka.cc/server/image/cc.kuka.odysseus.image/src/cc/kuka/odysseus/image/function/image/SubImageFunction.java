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

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import cc.kuka.odysseus.image.common.sdf.schema.SDFImageDatatype;
import cc.kuka.odysseus.image.util.OpenCVUtil;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SubImageFunction extends AbstractFunction<BufferedImage> {
    /**
     *
     */
    private static final long serialVersionUID = 3380978158532128904L;
    /**
     *
     */
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE }, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    /**
     *
     */
    public SubImageFunction() {
        super("sub", 5, SubImageFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    @Override
    public BufferedImage getValue() {
        final BufferedImage image = (BufferedImage) this.getInputValue(0);
        final int x = this.getNumericalInputValue(1).intValue();
        final int y = this.getNumericalInputValue(2).intValue();
        final int width = this.getNumericalInputValue(3).intValue();
        final int height = this.getNumericalInputValue(4).intValue();
        Objects.requireNonNull(image);
        Preconditions.checkArgument((x >= 0) && (width > 0) && ((x + width) <= image.getWidth()), "Invalid dimension");
        Preconditions.checkArgument((y >= 0) && (height > 0) && ((y + height) <= image.getHeight()), "Invalid dimension");

        final Mat iplImage = OpenCVUtil.imageToIplImage(image);

        final Rect roi = new Rect();
        roi.x = x;
        roi.y = y;
        roi.width = width;
        roi.height = height;

        final Mat iplResult = iplImage.submat(roi);

        iplImage.release();

        return OpenCVUtil.iplImageToImage(iplResult);
    }

}
