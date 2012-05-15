package de.uniol.inf.is.odysseus.spatial.grid.functions;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.grid.model.PolarGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class ToPolarGrid extends AbstractFunction<PolarGrid> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5134168940606752861L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFSpatialDatatype.SPATIAL_GEOMETRY,
					SDFSpatialDatatype.SPATIAL_POLYGON },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE } };

	@Override
	public int getArity() {
		return 6;
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
							+ " argument(s): A geometry, the x and y coordinates, the radius, the cellangle, and the cellradius.");
		} else {
			return accTypes[argPos];
		}
	}

	@Override
	public String getSymbol() {
		return "ToPolarGrid";
	}

	@Override
	public PolarGrid getValue() {
		final Geometry geometry = (Geometry) this.getInputValue(0);
		final Double x = (Double) this.getInputValue(1);
		final Double y = (Double) this.getInputValue(2);
		final Double radius = (Double) this.getInputValue(3);
		final Double cellangle = (Double) this.getInputValue(4);
		final Double cellradius = ((Double) this.getInputValue(5));

		final Coordinate[] coordinates = geometry.getCoordinates();

		final PolarGrid grid = new PolarGrid(new Coordinate(x, y), radius,
				cellangle, cellradius);

		for (int i = 0; i < coordinates.length; i++) {
			Coordinate coordinate = coordinates[i];
			double length = Math.sqrt(Math.pow(coordinate.x, 2)
					+ Math.pow(coordinate.y, 2));
			double theta = Math.atan2(coordinate.y, coordinate.x);
			double[] pr = new double[(int) (((length + 42.0) / cellradius) + 0.5)];
			for (double r = 0.0; r <= length + 42.0; r += cellradius) {
				grid.set(r, theta, Math.max(grid.get(r, theta), 1.0));
			}
		}
		return grid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.POLARGRID;
	}

}
