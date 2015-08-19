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

import cc.kuka.odysseus.image.common.sdf.schema.SDFImageDatatype;
import cc.kuka.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class InverseImageFunction extends AbstractFunction<BufferedImage> {
    /**
     *
     */
    private static final long serialVersionUID = -5532725733473509826L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE } };

    /**
     *
     */
    public InverseImageFunction() {
        super("inv", 1, InverseImageFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    @Override
    public BufferedImage getValue() {
        final BufferedImage image = (BufferedImage) this.getInputValue(0);
        Objects.requireNonNull(image);

        final Mat iplImage = OpenCVUtil.imageToIplImage(image);
        final Mat result = iplImage.inv();
        iplImage.release();
        return OpenCVUtil.iplImageToImage(result);
    }
}
