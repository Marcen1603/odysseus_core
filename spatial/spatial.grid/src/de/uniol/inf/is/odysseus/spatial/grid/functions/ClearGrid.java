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

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@Deprecated
// Not working
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
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "ClearGrid";
	}

	@Override
	public Grid getValue() {
		final Grid base = this.getInputValue(0);
		final Grid grid = this.getInputValue(1);
		IplImage baseImage = OpenCVUtil.gridToImage(base);
		IplImage image = OpenCVUtil.gridToImage(grid);
		opencv_core.cvAnd(baseImage, image, baseImage, null);
		image.release();
		return OpenCVUtil.imageToGrid(baseImage, base);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
