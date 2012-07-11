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
package de.uniol.inf.is.odysseus.spatial.grid.functions;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.common.GridUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Create an empty occupancy grids if the input value is null
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class CreateGridIfNull extends AbstractFunction<Grid> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2171571693923264766L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFGridDatatype.GRID },
			{ SDFSpatialDatatype.SPATIAL_COORDINATE }, { SDFDatatype.INTEGER },
			{ SDFDatatype.INTEGER }, { SDFDatatype.DOUBLE } };

	@Override
	public int getArity() {
		return 5;
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
							+ " argument(s): A grid, the x and y coordinates, the width and height, and the cellsize.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "CreateGridIfNull";
	}

	@Override
	public Grid getValue() {
		final Coordinate origin = (Coordinate) this.getInputValue(1);
		final Integer width = this.getNumericalInputValue(2).intValue();
		final Integer height = this.getNumericalInputValue(3).intValue();
		final Double cellsize = this.getNumericalInputValue(4);
		Grid grid;
		if (this.getInputValue(0) != null) {
			grid = (Grid) this.getInputValue(0);
		} else {
			grid = new Grid(origin, width, height, cellsize);
			grid.fill(GridUtil.UNKNOWN);
		}
		return grid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
