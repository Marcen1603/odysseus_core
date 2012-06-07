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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * Spread the existence uncertainty in an occupancy grid
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SpreadOccupancyGrid extends AbstractFunction<CartesianGrid> {
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
	public CartesianGrid getValue() {
		final CartesianGrid grid = (CartesianGrid) this.getInputValue(0);
		final long startTimestamp;
		if (this.getInputValue(1) != null) {
			startTimestamp = this.getNumericalInputValue(1).longValue();
		} else {
			startTimestamp = this.getNumericalInputValue(2).longValue();
		}
		final long currentTimestamp = this.getNumericalInputValue(2)
				.longValue();
		final double velocity = this.getNumericalInputValue(3);
		
		int cells = (int) (((((double) (currentTimestamp - startTimestamp)) / 1000.0)
				* velocity / grid.cellsize) * 2.0 + 1.5);
		if (cells < 3) {
			cells = 3;
		}
		CvMat kernel = CvMat.create(cells, cells, opencv_core.CV_64F, grid
				.getImage().nChannels());
		opencv_core.cvSet(kernel, CvScalar.ONE);

		cvFilter2D(grid.getImage(), grid.getImage(), kernel, new CvPoint(
				-1, -1));
		kernel.deallocate();
		return grid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
