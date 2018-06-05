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
import org.opencv.imgproc.Imgproc;

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
public class ResizeImageFunction extends AbstractFunction<BufferedImage> {

    /**
     *
     */
    private static final long serialVersionUID = -8043116077617331093L;
    /**
     *
     */
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE }, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    /**
     *
     */
    public ResizeImageFunction() {
        super("resize", 3, ResizeImageFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    @Override
    public BufferedImage getValue() {
        final BufferedImage image = (BufferedImage) this.getInputValue(0);
        final int width = this.getNumericalInputValue(1).intValue();
        final int height = this.getNumericalInputValue(2).intValue();

        Objects.requireNonNull(image);
        Preconditions.checkArgument(width > 0, "Invalid dimension");
        Preconditions.checkArgument(height > 0, "Invalid dimension");
        final Mat iplImage = OpenCVUtil.imageToIplImage(image);

        final Mat iplResult = new Mat(width, height, iplImage.depth());
        Imgproc.resize(iplImage, iplResult, iplResult.size(), 0.0, 0.0, Imgproc.INTER_CUBIC);

        iplImage.release();

        return OpenCVUtil.iplImageToImage(iplResult);
    }

}
