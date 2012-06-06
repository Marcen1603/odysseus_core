package de.uniol.inf.is.odysseus.spatial.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToPolarCoordinate extends AbstractFunction<PolarCoordinate> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7736127847801899025L;
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
					+ this.getArity()
					+ " argument(s): The radius and the angle");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "ToPolarCoordinate";
	}

	@Override
	public PolarCoordinate getValue() {
		double r = this.getNumericalInputValue(0);
		double a = this.getNumericalInputValue(1);
		return new PolarCoordinate(r, a);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFSpatialDatatype.SPATIAL_POLAR_COORDINATE;
	}
}
