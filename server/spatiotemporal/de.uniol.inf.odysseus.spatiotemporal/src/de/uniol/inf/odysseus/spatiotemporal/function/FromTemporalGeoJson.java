package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.jts.JtsAdapterFactory;
import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.LineString;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalGeometry;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TrajectoryMovingPointFunction;

/**
 * Temporalization function for the scenario that the future trajectory of a
 * moving object is already known, for example, if its using a navigation
 * system. This is a prototype demo with a limited GeoJSON data model that can
 * be used.
 * 
 * @author Tobias Brandt
 *
 */
public class FromTemporalGeoJson extends AbstractFunction<TemporalGeometry> {

	private static final long serialVersionUID = 7234937168792519636L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING } };

	private Gson gson;

	public FromTemporalGeoJson() {
		super("FromTemporalGeoJson", 1, accTypes, SDFSpatialDatatype.SPATIAL_POINT);
	}

	@Override
	public TemporalGeometry getValue() {

		// Use a library to parse the GeoJSON. See https://github.com/filosganga/geogson
		if (this.gson == null) {
			this.gson = new GsonBuilder()
					.registerTypeAdapterFactory(
							new JtsAdapterFactory(new GeometryFactory(new PrecisionModel(100), 4326)))
					.registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
		}

		String geoJson = this.getInputValue(0);
		FeatureCollection features = this.gson.fromJson(geoJson, FeatureCollection.class);
		Feature feature = features.features().get(0);

		List<Long> timeList = getTimes(feature);
		List<Point> points = getPoints(feature);

		TemporalFunction<GeometryWrapper> temporalFunction = new TrajectoryMovingPointFunction(timeList, points);
		TemporalGeometry temporalPoint = new TemporalGeometry(temporalFunction);
		return temporalPoint;
	}

	/**
	 * Convert the list from the model of the library to JTS objects
	 * 
	 * @param feature The feature from GeoJSON
	 * @return A list of points of the trajectory (LineString) in JTS objects
	 */
	private List<Point> getPoints(Feature feature) {
		Geometry<?> trajectory = feature.geometry();
		List<Point> points = new ArrayList<>();
		if (trajectory instanceof LineString) {
			LineString lineString = (LineString) trajectory;
			List<com.github.filosganga.geogson.model.Point> points2 = lineString.points();
			for (com.github.filosganga.geogson.model.Point gsonPoint : points2) {
				Coordinate coordinate = new Coordinate(gsonPoint.lat(), gsonPoint.lon());
				GeometryFactory factory = new GeometryFactory();
				Point point = factory.createPoint(coordinate);
				points.add(point);
			}
		}
		return points;
	}

	/**
	 * Use the time element similar to the TimeDimension of Leaflet, as it fits best
	 * to trajectories (LineString):
	 * https://github.com/socib/Leaflet.TimeDimension/issues/99
	 */
	private List<Long> getTimes(Feature feature) {
		JsonElement jsonElement = feature.properties().get("times");
		if (!jsonElement.isJsonArray()) {
			return null;
		}
		JsonArray asJsonArray = jsonElement.getAsJsonArray();
		List<Long> timeList = new ArrayList<>();
		Iterator<JsonElement> iterator = asJsonArray.iterator();
		while (iterator.hasNext()) {
			JsonElement next = iterator.next();
			long asLong = next.getAsLong();
			timeList.add(asLong);
		}
		return timeList;
	}

	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		return TemporalDatatype.getTemporalConstraint();
	}

}

/*
 * Example temporal GeoJSON:
 * 
 * 
 * { "type": "FeatureCollection", "features": [ { "type": "Feature",
 * "properties": { "times":[ "1534255083000", "1534255093000", "1534255003000",
 * "1534255103000", "1534255113000", "1534255123000", "1534255133000",
 * "1534255143000", "1534255153000", "1534255163000", "1534255173000",
 * "1534255183000", "1534255193000" ] }, "geometry": { "type": "LineString",
 * "coordinates": [ [ 8.188934326171875, 53.48722843308561 ], [ 8.206787109375,
 * 53.55581022359457 ], [ 8.177947998046875, 53.63161060657857 ], [
 * 8.136749267578125, 53.69345406966439 ], [ 8.24249267578125,
 * 53.747898723904164 ], [ 8.378448486328125, 53.78523783809317 ], [
 * 8.36334228515625, 53.820922446131306 ], [ 8.260345458984375,
 * 53.82983885331911 ], [ 8.1573486328125, 53.82902834926158 ], [
 * 8.029632568359375, 53.82983885331911 ], [ 7.901916503906249,
 * 53.82335438174398 ], [ 7.794799804687499, 53.81038242731128 ], [
 * 7.745361328125, 53.782803690625954 ] ] } } ] }
 * 
 */
