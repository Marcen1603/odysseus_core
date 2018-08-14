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

public class ToGivenTemporalPoint extends AbstractFunction<TemporalGeometry> {

	private static final long serialVersionUID = 7234937168792519636L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING } };

	private Gson gson;

	// For OSGi
	public ToGivenTemporalPoint() {
		super("FromTemporalGeoJson", 1, accTypes, SDFSpatialDatatype.SPATIAL_POINT);
	}

	@Override
	public TemporalGeometry getValue() {

		// See https://github.com/filosganga/geogson
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

	private List<Long> getTimes(Feature feature) {
		/*
		 * Use the time element similar to the TimeDimension of Leaflet, as it fits best
		 * to trajectories (LineString):
		 * https://github.com/socib/Leaflet.TimeDimension/issues/99
		 */
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
