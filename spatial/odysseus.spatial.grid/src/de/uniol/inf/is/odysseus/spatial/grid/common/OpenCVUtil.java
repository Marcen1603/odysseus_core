package de.uniol.inf.is.odysseus.spatial.grid.common;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;

public final class OpenCVUtil {
	public final static CvScalar UNKNOWN = opencv_core.cvScalarAll(255);
	public final static CvScalar FREE = opencv_core.cvScalarAll(0);
	public final static CvScalar OBSTACLE = opencv_core.cvScalarAll(100);

	public static void imageToGrid(IplImage image, Grid grid) {
		if (image.widthStep() > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getByteBuffer(h * image.widthStep()).get(grid.get(),
						h * grid.width, grid.width);
			}
		} else {
			grid.getBuffer().put(image.getByteBuffer());
		}
	}

	public static void gridToImage(Grid grid, IplImage image) {
		if (image.widthStep() > image.width()) {
			for (int h = 0; h < image.height(); h++) {
				image.getByteBuffer(h * image.widthStep()).put(grid.get(),
						h * grid.width, grid.width);
			}
		} else {
			image.getByteBuffer().put(grid.getBuffer().duplicate());
		}
	}
}
