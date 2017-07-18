package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.IMovingObjectPredictor;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.MovingObjectLinearPredictor;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.PredictionEnrichAO;

public class PredictionEnrichPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private final static int DATA_PORT = 0;
	private final static int ESTIMATION_PORT = 1;

	private IMovingObjectPredictor movingObjectInterpolator;

	private int geoObjectIndex;
	private int idIndex;

	public PredictionEnrichPO(PredictionEnrichAO ao) {
		this.movingObjectInterpolator = new MovingObjectLinearPredictor(TimeUnit.MILLISECONDS);

		this.geoObjectIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getGeoObjectAttribute());
		this.idIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
	}

	@Override
	protected void process_next(T tuple, int port) {
		if (port == DATA_PORT) {
			this.addLocation(tuple);
		} else if (port == ESTIMATION_PORT) {
			this.createPrediction(tuple);
		}
	}

	private void addLocation(T tuple) {

		double latitude = 0;
		double longitude = 0;

		if (tuple.getAttribute(this.geoObjectIndex) instanceof GeometryWrapper) {
			Geometry geometry = ((GeometryWrapper) tuple.getAttribute(this.geoObjectIndex)).getGeometry();
			// We always use lat / lng
			latitude = geometry.getCoordinate().x;
			longitude = geometry.getCoordinate().y;
		}

		String id = tuple.getAttribute(this.idIndex).toString();

		// TODO Do we keep speed and direction?
		LocationMeasurement locationMeasurement = new LocationMeasurement(latitude, longitude, 0, 0,
				tuple.getMetadata().getStart(), id);

		this.movingObjectInterpolator.addLocation(locationMeasurement, tuple);
	}

	@SuppressWarnings("unchecked")
	private void createPrediction(T tuple) {
		// Interpolate for all objects until this time
		Map<String, LocationMeasurement> allLocations = this.movingObjectInterpolator
				.calcAllLocations(tuple.getMetadata().getStart());
		for (LocationMeasurement interpolatedLocationMeasurement : allLocations.values()) {
			this.transfer((T) createTuple(interpolatedLocationMeasurement));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Here we have to calculate and output the new location of every
		// moving object
		Map<String, LocationMeasurement> allLocations = this.movingObjectInterpolator
				.calcAllLocations(punctuation.getTime());
		for (LocationMeasurement interpolatedLocationMeasurement : allLocations.values()) {
			this.transfer((T) createTuple(interpolatedLocationMeasurement));
		}
	}

	private Tuple<ITimeInterval> createTuple(LocationMeasurement interpolatedLocationMeasurement) {
		Tuple<ITimeInterval> tupleWithInterpolatedLocation = new Tuple<ITimeInterval>(5, false);
		tupleWithInterpolatedLocation
				.setMetadata(new TimeInterval(interpolatedLocationMeasurement.getMeasurementTime()));
		tupleWithInterpolatedLocation.setAttribute(0, interpolatedLocationMeasurement.getMovingObjectId());
		tupleWithInterpolatedLocation.setAttribute(1, interpolatedLocationMeasurement.getLatitude());
		tupleWithInterpolatedLocation.setAttribute(2, interpolatedLocationMeasurement.getLongitude());
		tupleWithInterpolatedLocation.setAttribute(3, interpolatedLocationMeasurement.getSpeedInMetersPerSecond());
		tupleWithInterpolatedLocation.setAttribute(4, interpolatedLocationMeasurement.getHorizontalDirection());
		return tupleWithInterpolatedLocation;
	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}
}
