package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.datatype.GeoHashWrapper;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Takes a spatial point and calculates the GeoHash for the point with the
 * highest bit precision (full long value, 64 Bit).
 * 
 * @author Tobias Brandt
 *
 */
public class ToGeoHash extends AbstractFunction<GeoHashWrapper> {

	private static final long serialVersionUID = 4863586728786679457L;

	public static final int BIT_PRECISION = 64;

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFSpatialDatatype.LIST_SPATIAL_POINT };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1 };

	public ToGeoHash() {
		super("ToGeoHash", 1, accTypes, SDFSpatialDatatype.LIST_SPATIAL_POINT);
	}

	@Override
	public GeoHashWrapper getValue() {

		Object inputValue = this.getInputValue(0);
		if (!(inputValue instanceof GeometryWrapper)) {
			return null;
		}

		Geometry geometry = ((GeometryWrapper) inputValue).getGeometry();
		double latitude = geometry.getCentroid().getX();
		double longitude = geometry.getCentroid().getY();
		GeoHash geoHash = GeoHash.withBitPrecision(latitude, longitude, BIT_PRECISION);
		return new GeoHashWrapper(geoHash);
	}

}
