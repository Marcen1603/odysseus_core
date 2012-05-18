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

package de.uniol.inf.is.odysseus.spatial.grid.model;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvSet2D;

import java.nio.DoubleBuffer;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class CartesianGrid implements Cloneable {
	public final Coordinate origin;

	public final int width;
	public final int height;
	public final double cellsize;
	private final IplImage image;

	public CartesianGrid(Coordinate origin, int width, int height,
			double cellsize) {
		this.origin = origin;
		this.cellsize = cellsize;
		this.width = width;
		this.height = height;
		this.image = IplImage.create(opencv_core.cvSize(width, height),
				opencv_core.IPL_DEPTH_64F, 1);
		this.image.origin(1);
	}

	public CartesianGrid(Coordinate origin, int width, int height,
			double cellsize, IplImage image) {
		this(origin, width, height, cellsize);
		opencv_core.cvCopy(image, this.image);
	}

	@Override
	protected void finalize() throws Throwable {
		this.image.release();
		super.finalize();
	}

	public double get(int x, int y) {
		CvScalar value = cvGet2D(this.image, x, y);
		return value.val(0);
	}

	public double get(double x, double y) {
		int gridX = (int) (x / cellsize);
		int gridY = (int) (y / cellsize);
		return get(gridX, gridY);
	}

	public DoubleBuffer getBuffer() {
		return this.image.getDoubleBuffer();
	}

	public void set(int x, int y, double value) {
		cvSet2D(image, x, y, opencv_core.cvScalarAll(value));
	}

	public void set(double x, double y, double value) {
		int gridX = (int) (x / cellsize);
		int gridY = (int) (y / cellsize);
		set(gridX, gridY, value);
	}

	public void setBuffer(DoubleBuffer value) {
		this.image.getDoubleBuffer().put(value);
	}

	public IplImage getImage() {
		return image;
	}

	public void fill(double value) {
		cvRectangle(image, new CvPoint(0, 0),
				new CvPoint(image.width(), image.height()),
				opencv_core.cvScalarAll(value), CV_FILLED, 4, 0);
	}

	@Override
	public CartesianGrid clone() {
		CartesianGrid grid = new CartesianGrid(new Coordinate(this.origin.x,
				this.origin.y), this.width, this.height, this.cellsize,
				this.image);
		return grid;
	}

	@Override
	public String toString() {
		return "{Origin: " + origin + ", Width: " + width + " Depth: " + height
				+ " CellSize: " + this.cellsize + "}";
	}

}
