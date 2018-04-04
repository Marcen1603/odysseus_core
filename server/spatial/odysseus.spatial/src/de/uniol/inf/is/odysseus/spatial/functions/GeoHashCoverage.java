package de.uniol.inf.is.odysseus.spatial.functions;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.datatype.GeoHashWrapper;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.index.GeoHashHelper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * This function calculates the GeoHashes that cover a given radius. This is
 * only an approximation!
 * 
 * @author Tobias Brandt
 *
 */
public class GeoHashCoverage extends AbstractFunction<List<GeoHashWrapper>> {

	private static final long serialVersionUID = 1661673870725509296L;

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFSpatialDatatype.LIST_SPATIAL_POINT };
	public static final SDFDatatype[] accTypes2 = new SDFDatatype[] { SDFSpatialDatatype.DOUBLE };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1, accTypes2 };

	public static final int WGS84_SRID = 4326;

	public GeoHashCoverage() {
		super("GeoHashCoverage", accTypes.length, accTypes, SDFSpatialDatatype.LIST_SPATIAL_POINT);
	}

	@Override
	public List<GeoHashWrapper> getValue() {
		Object inputValue = this.getInputValue(0);
		Object inputValue2 = this.getInputValue(1);

		if (!(inputValue instanceof GeometryWrapper)) {
			return new ArrayList<>();
		}

		Geometry center = ((GeometryWrapper) inputValue).getGeometry();
		double radius = (Double) inputValue2;

		List<GeoHash> approximateCircle = GeoHashHelper.approximateCircle(center.getCentroid().getX(),
				center.getCentroid().getY(), radius, WGS84_SRID);

		List<GeoHashWrapper> circleWrapper = new ArrayList<>();
		approximateCircle.stream().forEach(g -> circleWrapper.add(new GeoHashWrapper(g)));
		return circleWrapper;
	}

}
