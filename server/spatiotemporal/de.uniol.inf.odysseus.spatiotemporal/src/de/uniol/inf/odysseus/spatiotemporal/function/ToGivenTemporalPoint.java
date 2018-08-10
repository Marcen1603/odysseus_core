package de.uniol.inf.odysseus.spatiotemporal.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalGeometry;

public class ToGivenTemporalPoint extends AbstractFunction<TemporalGeometry> {

	private static final long serialVersionUID = 7234937168792519636L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING } };

	// For OSGi
	public ToGivenTemporalPoint() {
		super("FromGeoJson", 1, accTypes, SDFSpatialDatatype.SPATIAL_POINT);
	}

	@Override
	public TemporalGeometry getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
