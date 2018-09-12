package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class SpatialLength extends AbstractFunction<Double> {

	private static final long serialVersionUID = -4772212259693509945L;
	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFSpatialDatatype.SPATIAL_LINE_STRING,
			SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1 };

	public SpatialLength() {
		super("SpatialLength", 1, accTypes, SDFSpatialDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {

		Object inputValue = this.getInputValue(0);
		if (!(inputValue instanceof GeometryWrapper)) {
			return null;
		}

		GeometryWrapper wrapper = (GeometryWrapper) inputValue;
		Geometry geometry = wrapper.getGeometry();
		return geometry.getLength();
	}

}
