/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.spatial.grid.common;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.model.PolarGrid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public final class OpenCVUtil {
	public final static CvScalar UNKNOWN = opencv_core.cvScalarAll(255);
	public final static CvScalar FREE = opencv_core.cvScalarAll(0);
	public final static CvScalar OBSTACLE = opencv_core.cvScalarAll(100);

	@Deprecated
	public static void imageToGrid(IplImage image, Grid grid) {
		int widthStep = image.widthStep();
		if (widthStep > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getByteBuffer(h * widthStep).get(grid.get(),
						h * grid.width, grid.width);
			}
		} else {
			grid.getBuffer().put(image.getByteBuffer());
		}
	}

	public static void imageToGrid(IplImage image, CartesianGrid grid) {
		int widthStep = image.widthStep() / 8;
		if (widthStep > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getDoubleBuffer(h * widthStep).get(grid.get(),
						h * grid.width, grid.width);
			}
		} else {
			grid.getBuffer().put(image.getDoubleBuffer());
		}
	}

	public static void imageToGrid(IplImage image, PolarGrid grid) {
		int widthStep = image.widthStep() / 8;
		if (widthStep > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getDoubleBuffer(h * widthStep).get(grid.get(),
						h * grid.angle, grid.angle);
			}
		} else {
			grid.getBuffer().put(image.getDoubleBuffer());
		}
	}

	@Deprecated
	public static void gridToImage(Grid grid, IplImage image) {
		int widthStep = image.widthStep();
		if (widthStep > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getByteBuffer(h * widthStep).put(grid.get(),
						h * grid.width, grid.width);
			}
		} else {
			image.getByteBuffer().put(grid.getBuffer().duplicate());
		}
	}

	public static void gridToImage(CartesianGrid grid, IplImage image) {
		int widthStep = image.widthStep() / 8;
		if (widthStep > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				System.out.println("Cap: " + image.getDoubleBuffer().capacity()
						+ " " + h + " " + widthStep);
				image.getDoubleBuffer(h * widthStep).put(grid.get(),
						h * grid.width, grid.width);
			}
		} else {
			image.getDoubleBuffer().put(grid.getBuffer().duplicate());
		}
	}

	public static void gridToImage(PolarGrid grid, IplImage image) {
		int widthStep = image.widthStep() / 8;
		if (widthStep > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getDoubleBuffer(h * widthStep).put(grid.get(),
						h * grid.angle, grid.angle);
			}
		} else {
			image.getDoubleBuffer().put(grid.getBuffer().duplicate());
		}
	}
}
