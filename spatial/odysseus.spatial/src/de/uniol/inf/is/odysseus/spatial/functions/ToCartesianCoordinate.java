package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToCartesianCoordinate extends AbstractFunction<Coordinate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2881839260208999355L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.INTEGER } };

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
					+ this.getArity() + " argument(s): The x and y coordinates");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "ToCartesianCoordinate";
	}

	@Override
	public Coordinate getValue() {
		double x = this.getNumericalInputValue(0);
		double y = this.getNumericalInputValue(1);
		return new Coordinate(x, y);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFSpatialDatatype.SPATIAL_COORDINATE;
	}

}
