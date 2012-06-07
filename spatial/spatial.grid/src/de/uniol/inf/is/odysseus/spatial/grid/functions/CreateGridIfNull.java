package de.uniol.inf.is.odysseus.spatial.grid.functions;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Create an empty occupancy grids if the input value is null
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class CreateGridIfNull extends AbstractFunction<CartesianGrid> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2171571693923264766L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFGridDatatype.GRID },
			{ SDFSpatialDatatype.SPATIAL_COORDINATE }, { SDFDatatype.INTEGER },
			{ SDFDatatype.INTEGER }, { SDFDatatype.DOUBLE } };
	private static final double UNKNOWN = -Math.log(1.0 - 0.5);

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
	public CartesianGrid getValue() {
		final Coordinate origin = (Coordinate) this.getInputValue(1);
		final Integer width = this.getNumericalInputValue(2).intValue();
		final Integer height = this.getNumericalInputValue(3).intValue();
		final Double cellsize = this.getNumericalInputValue(4);
		CartesianGrid grid;
		if (this.getInputValue(0) != null) {
			grid = (CartesianGrid) this.getInputValue(0);
		} else {
			grid = new CartesianGrid(origin, width, height, cellsize);
			grid.fill(UNKNOWN);
		}
		return grid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
