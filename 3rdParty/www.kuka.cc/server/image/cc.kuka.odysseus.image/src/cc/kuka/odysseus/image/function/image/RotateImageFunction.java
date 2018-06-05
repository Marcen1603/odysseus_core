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
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import cc.kuka.odysseus.image.common.sdf.schema.SDFImageDatatype;
import cc.kuka.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RotateImageFunction extends AbstractFunction<BufferedImage> {
    /**
     *
     */
    private static final long serialVersionUID = 6909756855094988348L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE }, SDFDatatype.NUMBERS };

    /**
     *
     */
    public RotateImageFunction() {
        super("rotate", 2, RotateImageFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    @Override
    public BufferedImage getValue() {
        final BufferedImage image = (BufferedImage) this.getInputValue(0);
        // Angle in degrees
        final double angle = this.getNumericalInputValue(1).doubleValue();

        Objects.requireNonNull(image);

        final Point center = new Point(image.getWidth() / 2, image.getHeight() / 2);

        final Mat mapMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0);
        final Mat iplImage = OpenCVUtil.imageToIplImage(image);
        final Mat iplResult = iplImage.clone();
        try {
            Imgproc.warpAffine(iplImage, iplResult, mapMatrix, iplImage.size());
        }
        catch (final Exception e) {
            throw e;
        }
        finally {
            mapMatrix.release();
            iplImage.release();
        }
        return OpenCVUtil.iplImageToImage(iplResult);
    }
}
