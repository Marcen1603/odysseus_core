package de.uniol.inf.is.odysseus.spatial.functions;

import java.util.List;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.datatype.GeoHashWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Checks if the given GeoHash (first input) is within the given list of
 * GeoHashes (second input). Uses the GeoHash-properties for this, hence, should
 * be fast.
 * 
 * @author Tobias Brandt
 *
 */
public class GeoHashInside extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = 6856650830160370453L;

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFSpatialDatatype.SPATIAL_POINT };
	public static final SDFDatatype[] accTypes2 = new SDFDatatype[] { SDFSpatialDatatype.LIST_SPATIAL_POINT };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1, accTypes2 };

	public GeoHashInside() {
		super("GeoHashInside", accTypes.length, accTypes, SDFSpatialDatatype.BOOLEAN);
	}

	@Override
	public Boolean getValue() {
		Object inputValue = this.getInputValue(0);
		if (!(inputValue instanceof GeoHashWrapper)) {
			return false;
		}

		Object inputValue2 = this.getInputValue(1);
		if (!(inputValue2 instanceof List)) {
			return false;
		}

		GeoHash geoHash = ((GeoHashWrapper) inputValue).getGeoHash();
		@SuppressWarnings("unchecked")
		List<GeoHashWrapper> areaHashes = (List<GeoHashWrapper>) inputValue2;

		for (GeoHashWrapper geoHashArea : areaHashes) {
			if (geoHash.within(geoHashArea.getGeoHash())) {
				return true;
			}
		}

		return false;
	}

}
