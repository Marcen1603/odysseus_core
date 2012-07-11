/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.spatial.grid.common;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;

public final class OpenCVUtil {
	private OpenCVUtil() {
	}

	public static IplImage gridToImage(Grid grid) {
		IplImage image = IplImage.create(
				opencv_core.cvSize(grid.width, grid.height),
				opencv_core.IPL_DEPTH_64F, 1);
		int widthStep = image.widthStep() / 8;
		if (widthStep > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getDoubleBuffer(h * widthStep).put(
						grid.getBuffer().array(), h * grid.width, grid.width);
			}
		} else {
			image.getDoubleBuffer().put(grid.getBuffer());
		}
		image.origin(1);
		return image;
	}

	public static Grid imageToGrid(IplImage image, Grid grid) {
		int widthStep = image.widthStep() / 8;
		if (widthStep > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getDoubleBuffer(h * widthStep).get(
						grid.getBuffer().array(), h * grid.width, grid.width);
			}
		} else {
			grid.setBuffer(image.getDoubleBuffer());
		}
		image.release();
		image = null;
		return grid;
	}
	
	public static void imageToProbScale(IplImage image){
		opencv_core.cvExp(image, image);
		opencv_core.cvConvertScale(image, image, -1.0, 0);
		opencv_core.cvAddS(image, opencv_core.cvScalarAll(1), image, null);
	}
	
	public static void imageToLogScale(IplImage image){	
		opencv_core.cvConvertScale(image, image, -1.0, 0);
		opencv_core.cvAddS(image, opencv_core.cvScalarAll(1), image, null);
		opencv_core.cvLog(image, image);
	}
}
