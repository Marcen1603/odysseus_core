package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class ToPoint extends AbstractFunction<GeometryWrapper> {

	private static final long serialVersionUID = 7202373953195273323L;

	public ToPoint() {
		super("ToPoint", 3, accTypes, SDFSpatialDatatype.SPATIAL_POINT);
	}

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFDatatype.DOUBLE, SDFDatatype.FLOAT,
			SDFDatatype.INTEGER };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1, accTypes1, accTypes1 };

	@Override
	public GeometryWrapper getValue() {
		GeometryFactory gf = new GeometryFactory();
		return new GeometryWrapper(gf.createPoint(
				new Coordinate(getNumericalInputValue(0), getNumericalInputValue(1), getNumericalInputValue(2))));
	}

}
