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
package de.uniol.inf.is.odysseus.spatial.grid.aggregation;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridPartialPlausabilityAggregate<T> implements
		IPartialAggregate<T> {

	private double count;
	private final Grid grid;
	private IplImage image;
	private IplImage mask;

	public GridPartialPlausabilityAggregate(final Grid grid) {
		this.count = 1.0;
		this.grid = new Grid(grid.origin, grid.width * grid.cellsize,
				grid.depth * grid.cellsize, grid.cellsize, grid.getBuffer());
		this.image = IplImage.create(
				opencv_core.cvSize(this.grid.width, this.grid.depth),
				opencv_core.IPL_DEPTH_16U, 1);
		this.mask = IplImage.create(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);

		IplImage tmp = IplImage.create(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(this.grid, tmp);

		opencv_imgproc.cvThreshold(tmp, tmp, 100, 0,
				opencv_imgproc.CV_THRESH_TOZERO_INV);

		opencv_core.cvConvertScale(tmp, this.image, 1, 0);
		tmp.release();
		tmp = null;

		OpenCVUtil.gridToImage(grid, this.mask);
		opencv_imgproc.cvThreshold(this.mask, this.mask, 100, 255,
				opencv_imgproc.CV_THRESH_BINARY);
	}

	public GridPartialPlausabilityAggregate(
			final GridPartialPlausabilityAggregate<T> gridPartialAggregate) {
		this.grid = new Grid(this.grid.origin, this.grid.width
				* this.grid.cellsize, this.grid.depth * this.grid.cellsize,
				this.grid.cellsize, gridPartialAggregate.grid.getBuffer());
		this.image = IplImage.create(
				opencv_core.cvSize(this.grid.width, this.grid.depth),
				opencv_core.IPL_DEPTH_16U, 1);
		this.mask = IplImage.create(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);
		opencv_core.cvCopy(gridPartialAggregate.image, this.image);
		opencv_core.cvCopy(gridPartialAggregate.mask, this.mask);
		this.count = gridPartialAggregate.count;
	}

	@Override
	public GridPartialPlausabilityAggregate<T> clone() {
		return new GridPartialPlausabilityAggregate<T>(this);
	}

	public void evaluate() {
		IplImage image = IplImage.create(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);
		opencv_core.cvConvertScale(this.image, image, 1.0 / this.count, 0);
		opencv_core.cvOr(image, this.mask, image, null);

		OpenCVUtil.imageToGrid(image, this.grid);

		image.release();
		this.image.release();
		this.mask.release();
		image = null;
		this.image = null;
		this.mask = null;
	}

	public Grid getGrid() {
		return this.grid;
	}

	public void merge(final Grid grid) {
		this.count++;
		IplImage mask = IplImage.create(
				opencv_core.cvSize(grid.width, grid.depth),
				opencv_core.IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(grid, mask);

		opencv_imgproc.cvThreshold(mask, mask, 100, 255,
				opencv_imgproc.CV_THRESH_BINARY);
		opencv_core.cvAnd(this.mask, mask, this.mask, null);

		IplImage merge = IplImage.create(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_16U, 1);

		IplImage tmp = IplImage.create(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(grid, tmp);

		opencv_imgproc.cvThreshold(tmp, tmp, 100, 0,
				opencv_imgproc.CV_THRESH_TOZERO_INV);

		opencv_core.cvConvertScale(tmp, merge, 1, 0);
		tmp.release();
		tmp = null;

		opencv_core.cvAdd(this.image, merge, this.image, null);

		merge.release();
		mask.release();
		mask = null;
		merge = null;
	}

	@Override
	public String toString() {
		final StringBuffer ret = new StringBuffer("GridPartialAggregate (")
				.append(this.hashCode()).append(")").append(this.grid);
		return ret.toString();
	}

}
