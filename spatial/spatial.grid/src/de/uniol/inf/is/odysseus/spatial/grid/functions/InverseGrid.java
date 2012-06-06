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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class InverseGrid extends AbstractFunction<CartesianGrid> {
	/**
     * 
     */
	private static final long serialVersionUID = 6682089158563417930L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFGridDatatype.GRID } };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument: A grid.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "InverseGrid";
	}

	@Override
	public CartesianGrid getValue() {
		final CartesianGrid grid = this.getInputValue(0);
		final CartesianGrid inverseGrid = new CartesianGrid(grid.origin,
				grid.width, grid.height, grid.cellsize);
		opencv_core.cvNot(grid.getImage(), inverseGrid.getImage());
		return inverseGrid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
