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

import com.google.common.base.Preconditions;
import com.googlecode.javacv.cpp.opencv_core;
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
public class ResizeImageFunction extends AbstractFunction<Image> {

	/**
     * 
     */
	private static final long serialVersionUID = -8043116077617331093L;
	/**
     * 
     */
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
			{ SDFImageDatatype.IMAGE }, SDFDatatype.NUMBERS,
			SDFDatatype.NUMBERS };

	/**
 * 
 */
	public ResizeImageFunction() {
		super("resize", 3, ResizeImageFunction.ACC_TYPES,
				SDFImageDatatype.IMAGE, true);
	}

	public Image getValue() {
		final Image image = (Image) this.getInputValue(0);
		final int width = this.getNumericalInputValue(1).intValue();
		final int height = this.getNumericalInputValue(2).intValue();

		Objects.requireNonNull(image);
		Preconditions.checkArgument(width > 0, "Invalid dimension");
		Preconditions.checkArgument(height > 0, "Invalid dimension");
		Image result = new Image(width, height);
		final IplImage iplImage = OpenCVUtil.imageToIplImage(image);

		IplImage iplResult = IplImage.create(opencv_core.cvSize(width, height),
				iplImage.depth(), iplImage.nChannels());
		opencv_imgproc.cvResize(iplImage, iplResult);
		iplImage.release();

		return OpenCVUtil.iplImageToImage(iplResult, result);
	}

}
