package de.uniol.inf.is.odysseus.spatial;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 */
public class SpatialDatatypeProvider {

	public static IDataDictionary datadictionary = null;

	protected void bindDataDictionary(IDataDictionary dd) {
		datadictionary = dd;
		if (datadictionary.getDatatype(SDFSpatialDatatype.SPATIAL_GEOMETRY
				.getURI()) == null) {
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_COORDINATE.getURI(),
					SDFSpatialDatatype.SPATIAL_COORDINATE);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_COORDINATE_SEQUENCE.getURI(),
					SDFSpatialDatatype.SPATIAL_COORDINATE_SEQUENCE);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_POINT.getURI(),
					SDFSpatialDatatype.SPATIAL_POINT);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_LINE_STRING.getURI(),
					SDFSpatialDatatype.SPATIAL_LINE_STRING);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_LINEAR_RING.getURI(),
					SDFSpatialDatatype.SPATIAL_LINEAR_RING);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_LINEAR_RING_ARRAY.getURI(),
					SDFSpatialDatatype.SPATIAL_LINEAR_RING_ARRAY);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_POLYGON.getURI(),
					SDFSpatialDatatype.SPATIAL_POLYGON);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_MULTI_POINT.getURI(),
					SDFSpatialDatatype.SPATIAL_MULTI_POINT);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING.getURI(),
					SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_MULTI_POLYGON.getURI(),
					SDFSpatialDatatype.SPATIAL_MULTI_POLYGON);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION.getURI(),
					SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION);
			datadictionary.addDatatype(
					SDFSpatialDatatype.SPATIAL_GEOMETRY.getURI(),
					SDFSpatialDatatype.SPATIAL_GEOMETRY);
		}
	}

	protected void unbindDataDictionary(IDataDictionary dd) {
		if (datadictionary.getDatatype(SDFSpatialDatatype.SPATIAL_GEOMETRY
				.getURI()) != null) {
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_COORDINATE.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_COORDINATE_SEQUENCE
					.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_POINT.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_LINE_STRING.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_LINEAR_RING.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_LINEAR_RING_ARRAY
					.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_POLYGON.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_MULTI_POINT.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING
					.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_MULTI_POLYGON.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION
					.getURI());
			dd.removeDatatype(SDFSpatialDatatype.SPATIAL_GEOMETRY.getURI());
		}
		datadictionary = null;
	}
}
