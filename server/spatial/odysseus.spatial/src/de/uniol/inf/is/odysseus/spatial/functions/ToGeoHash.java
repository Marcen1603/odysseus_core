package de.uniol.inf.is.odysseus.spatial.functions;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.datatype.GeoHashWrapper;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Converts a Geometry to a GeoHash.
 * 
 * @author Tobias Brandt
 *
 */
public class ToGeoHash extends AbstractFunction<GeoHashWrapper> {

	private static final long serialVersionUID = 4923924196446641586L;

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFSpatialDatatype.SPATIAL_POINT };

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1 };

	private static final int PRECISION = 64;

	public ToGeoHash() {
		super("ToGeoHash", 1, accTypes, SDFSpatialDatatype.SPATIAL_POINT);
	}

	@Override
	public GeoHashWrapper getValue() {
		Object inputValue = getInputValue(0);
		if (!(inputValue instanceof GeometryWrapper)) {
			return null;
		}

		GeometryWrapper geometry = (GeometryWrapper) inputValue;
		return new GeoHashWrapper(GeoHash.withBitPrecision(geometry.getGeometry().getCentroid().getX(),
				geometry.getGeometry().getCentroid().getY(), PRECISION));
	}

}
