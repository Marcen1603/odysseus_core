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

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;

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
	private final int flags = opencv_imgproc.CV_INTER_LINEAR
			+ opencv_imgproc.CV_WARP_FILL_OUTLIERS;

	/**
 * 
 */
	public RotateImageFunction() {
		super("rotate", 2, RotateImageFunction.ACC_TYPES,
				SDFImageDatatype.IMAGE, true);
	}

	public Image getValue() {
		final Image image = (Image) this.getInputValue(0);
		// Angle in degrees
		final double angle = this.getNumericalInputValue(1);

		Objects.requireNonNull(image);
		
		final CvPoint2D32f center = new CvPoint2D32f(image.getWidth() / 2,
				image.getHeight() / 2);

		final CvMat mapMatrix = CvMat.create(2, 3, opencv_core.CV_64F);
		opencv_imgproc.cv2DRotationMatrix(center, angle, 1.0, mapMatrix);
		final IplImage iplImage = OpenCVUtil.imageToIplImage(image);
		final IplImage iplResult = iplImage.clone();
		try {
			opencv_imgproc.cvWarpAffine(iplImage, iplResult, mapMatrix,
					this.flags, opencv_core.cvScalarAll(0.0));
		} catch (Exception e) {
			throw e;
		} finally {
			mapMatrix.release();
			iplImage.release();
		}
		return OpenCVUtil.iplImageToImage(iplResult, image);
	}
}
