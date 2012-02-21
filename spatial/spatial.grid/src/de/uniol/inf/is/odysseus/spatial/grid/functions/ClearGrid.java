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

package de.uniol.inf.is.odysseus.spatial.grid.functions;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClearGrid extends AbstractFunction<Grid> {
	/**
     * 
     */
	private static final long serialVersionUID = 558853050550138757L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFGridDatatype.GRID }, { SDFGridDatatype.GRID } };

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s): Two grids.");
		} else {
			return accTypes[argPos];
		}
	}

	@Override
	public String getSymbol() {
		return "ClearGrid";
	}

	@Override
	public Grid getValue() {
		final Grid base = this.getInputValue(0);
		final Grid grid = this.getInputValue(1);
		final Grid result = new Grid(base.origin, base.width * base.cellsize,
				base.depth * base.cellsize, base.cellsize);

		IplImage baseImage = IplImage.create(cvSize(base.width, base.depth),
				IPL_DEPTH_8U, 1);
		IplImage gridImage = IplImage.create(cvSize(grid.width, grid.depth),
				IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(base, baseImage);
		OpenCVUtil.gridToImage(grid, gridImage);

		opencv_imgproc.cvThreshold(gridImage, gridImage, 100, 0,
				opencv_imgproc.CV_THRESH_BINARY_INV);

		opencv_core.cvAnd(baseImage, gridImage, baseImage, null);
		OpenCVUtil.imageToGrid(baseImage, result);

		baseImage.release();
		gridImage.release();

		return result;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
