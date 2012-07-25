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
/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.spatial.grid.functions;

import static com.googlecode.javacv.cpp.opencv_imgproc.cvFilter2D;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * Spread the existence uncertainty in an occupancy grid
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SpreadOccupancyGrid extends AbstractFunction<Grid> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1646765322209354265L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFGridDatatype.GRID },
			{ SDFDatatype.TIMESTAMP, SDFDatatype.LONG },
			{ SDFDatatype.TIMESTAMP, SDFDatatype.LONG }, { SDFDatatype.DOUBLE } };

	@Override
	public int getArity() {
		return 4;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(
					this.getSymbol()
							+ " has only "
							+ this.getArity()
							+ " argument(s): A cartesian grid, the timestamp of the grid, the current timestamp, and the velocity.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "SpreadOccupancyGrid";
	}

	/**
	 * Spread the occupancy grid according to the given speed and the time
	 * difference between the creation of the grid and the time of measurement
	 * of the current value.
	 */
	@Override
	public Grid getValue() {
		final Grid grid = (Grid) this.getInputValue(0);
		final long startTimestamp;
		if (this.getInputValue(1) != null) {
			startTimestamp = this.getNumericalInputValue(1).longValue();
		} else {
			startTimestamp = this.getNumericalInputValue(2).longValue();
		}
		final long currentTimestamp = this.getNumericalInputValue(2)
				.longValue();
		// Velocity in m/s
		final double velocity = this.getNumericalInputValue(3);

		double cellVelocity = (velocity * 100.0) / grid.cellsize;
		double deltaTime = ((double) (currentTimestamp - startTimestamp)) / 1000.0;
		int cells = ((int) (deltaTime * cellVelocity + 0.5)) * 2 + 1;
		if (cells <=0){cells=1;}
		IplImage image = OpenCVUtil.gridToImage(grid);
		OpenCVUtil.imageToLogScale(image);
		CvMat kernel = CvMat.create(cells, cells, opencv_core.CV_64F,
				image.nChannels());
		opencv_core.cvSet(kernel, CvScalar.ONE);

		cvFilter2D(image, image, kernel, new CvPoint(-1, -1));
		kernel.deallocate();
		OpenCVUtil.imageToProbScale(image);
		return OpenCVUtil.imageToGrid(image, grid);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
