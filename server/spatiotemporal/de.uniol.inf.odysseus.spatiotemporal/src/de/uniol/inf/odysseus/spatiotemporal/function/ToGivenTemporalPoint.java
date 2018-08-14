package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.Collection;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.jts.JtsAdapterFactory;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vividsolutions.jts.geom.GeometryFactory;
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

		TemporalFunction<GeometryWrapper> temporalFunction = new TrajectoryMovingPointFunction();
		TemporalGeometry temporalPoint = new TemporalGeometry(temporalFunction);
		return temporalPoint;
	}

	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		return TemporalDatatype.getTemporalConstraint();
	}

}
