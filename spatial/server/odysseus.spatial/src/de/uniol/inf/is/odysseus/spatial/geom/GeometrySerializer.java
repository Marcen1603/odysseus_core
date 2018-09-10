package de.uniol.inf.is.odysseus.spatial.geom;

import java.io.IOException;
import java.io.StringWriter;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Converts a GeometryWrapper object to a valid GeoJSON string. 
 * @author Tobias Brandt
 *
 */
public class GeometrySerializer extends StdSerializer<GeometryWrapper> {

	private static final long serialVersionUID = -4694861226023038689L;
	private SimpleFeatureType type;

	public GeometrySerializer() {
		this(GeometryWrapper.class);
	}

	protected GeometrySerializer(Class<GeometryWrapper> t) {
		super(t);
		this.type = createSimpleFeatureType();
	}

	@Override
	public void serialize(GeometryWrapper geometryWrapper, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonGenerationException {
		Geometry geometry = geometryWrapper.getGeometry();
		
		// For proper GeoJSON we need to have longitude / latitude
		if (geometryWrapper.getGeometry() instanceof Point) {
			// Switch lat / lng to lng / lat
			Point point = (Point) geometryWrapper.getGeometry();
			double latitude = point.getX();
			double longitude = point.getY();

			GeometryFactory factory = new GeometryFactory();
			Point newPoint = factory.createPoint(new Coordinate(longitude, latitude));
			geometry = newPoint;
		}

		// Create valid GeoJson code
		FeatureJSON fjson = new FeatureJSON();
		StringWriter writer = new StringWriter();
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(this.type);
		builder.add(geometry);
		SimpleFeature simpleFeature = builder.buildFeature("" + geometryWrapper.getId());

		fjson.writeFeature(simpleFeature, writer);
		String json = writer.toString();

		// We don't want out JSON string to be escaped (because it is JSON)
		generator.writeRawValue(json);
	}

	private SimpleFeatureType createSimpleFeatureType() {
		SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();

		// set the name / the id of the geoJSON object
		b.setName("geometry");

		// add a geometry property
		b.setCRS(DefaultGeographicCRS.WGS84); // set crs first
		b.add("location", Geometry.class); // then add geometry

		// build the type
		return b.buildFeatureType();
	}

}
