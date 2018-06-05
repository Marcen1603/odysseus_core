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

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class RotateImageFunction extends AbstractFunction<Image> {
	/**
     * 
     */
	private static final long serialVersionUID = 6909756855094988348L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
			{ SDFImageDatatype.IMAGE }, SDFDatatype.NUMBERS };

	/**
 * 
 */
	public RotateImageFunction() {
		super("rotate", 2, RotateImageFunction.ACC_TYPES,
				SDFImageDatatype.IMAGE);
	}

	@Override
	public Image getValue() {
		final Image image = (Image) this.getInputValue(0);
		// Angle in degrees
		final double angle = this.getNumericalInputValue(1);

		Objects.requireNonNull(image);

		final Point center = new Point(image.getWidth() / 2,
				image.getHeight() / 2);

		final Mat mapMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0);
		final Mat iplImage = OpenCVUtil.imageToIplImage(image);
		final Mat iplResult = iplImage.clone();
		try {
			Imgproc.warpAffine(iplImage, iplResult, mapMatrix, iplImage.size());
		} catch (Exception e) {
			throw e;
		} finally {
			mapMatrix.release();
			iplImage.release();
		}
		return OpenCVUtil.iplImageToImage(iplResult, image);
	}
}
