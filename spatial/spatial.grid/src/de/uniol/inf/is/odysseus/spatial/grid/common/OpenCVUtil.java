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
}
