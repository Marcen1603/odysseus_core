package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.GeoHashMONoCleanupIndexStructure;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.SpatioTemporalQueryResult;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.TrajectoryRadiusAO;

public class TrajectoryRadiusPO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	private static final int TRAJECTORIES_ATTRIBUTE_POSITION = 0;

	private boolean queryAllMovingObjects = true;
	private List<String> movingObjectsToQuery;
	private double radiusMeters;

	public TrajectoryRadiusPO(TrajectoryRadiusAO ao) {
		this.queryAllMovingObjects = ao.isQueryAllMovingObjects();
		this.movingObjectsToQuery = ao.getMovingObjectsToQuery();
		this.radiusMeters = ao.getRadius();
	}

	@Override
	protected void process_next(T object, int port) {

		// We received a bunch of new trajectory predictions -> a whole future

		// 1. Create and fill an index structure so that we can work on this set of
		// trajectories
		// TODO Still need name and geometryPosition?
		GeoHashMONoCleanupIndexStructure index = new GeoHashMONoCleanupIndexStructure("bla", 1);
		List<Tuple<ITimeInterval>> trajectories = object.getAttribute(TRAJECTORIES_ATTRIBUTE_POSITION);
		for (Tuple<ITimeInterval> trajectoryTuple : trajectories) {
			String movingObjectId = trajectoryTuple.getAttribute(0);
			List<Tuple<ITimeInterval>> predictedLocations = trajectoryTuple.getAttribute(1);
			for (Tuple<ITimeInterval> predictedLocation : predictedLocations) {
				GeometryWrapper geoWrapper = predictedLocation.getAttribute(1);
				Geometry geometry = geoWrapper.getGeometry();
				// TODO Maybe fill speed and direction?
				LocationMeasurement location = new LocationMeasurement(geometry.getCentroid().getX(),
						geometry.getCentroid().getY(), -1, -1, predictedLocation.getMetadata().getStart(),
						movingObjectId);
				index.add(location, predictedLocation);
			}
		}

		// 2. Query the radius for the given IDs
		if (this.queryAllMovingObjects) {
			this.movingObjectsToQuery = new ArrayList<>(index.getAllMovingObjectIds());
		}
		for (String movingObjectIDToQuery : this.movingObjectsToQuery) {
			Map<String, List<SpatioTemporalQueryResult>> queryCircleTrajectory = index
					.queryCircleTrajectory(movingObjectIDToQuery, this.radiusMeters);

			// Put the result into a tuple
			Tuple<ITimeInterval> resultTupleForMovingObject = createTupleList(queryCircleTrajectory,
					movingObjectIDToQuery);
			this.transfer((T) resultTupleForMovingObject);
		}
	}

	private Tuple<ITimeInterval> createTupleList(
			Map<String, List<SpatioTemporalQueryResult>> resultsForCircleTrajectory, String movingObjectID) {

		List<Tuple<ITimeInterval>> tupleList = new ArrayList<>();
		GeometryFactory factory = new GeometryFactory();

		for (String otherMovingObjectID : resultsForCircleTrajectory.keySet()) {
			List<SpatioTemporalQueryResult> trajectoryElementsInCircle = resultsForCircleTrajectory
					.get(otherMovingObjectID);

			for (SpatioTemporalQueryResult meetingPoint : trajectoryElementsInCircle) {
				Tuple<ITimeInterval> resultTuple = new Tuple<>(6, false);

				// CenterID | CenterGeometry | OtherID | OtherGeometry | meetingTime
				resultTuple.addAttributeValue(0, movingObjectID);

				Geometry centerGeometry = factory
						.createPoint(new Coordinate(meetingPoint.getCenterLocation().getLatitude(),
								meetingPoint.getCenterLocation().getLongitude()));
				resultTuple.addAttributeValue(1, centerGeometry);

				resultTuple.addAttributeValue(2, otherMovingObjectID);

				Geometry otherGeometry = factory.createPoint(new Coordinate(
						meetingPoint.getOtherLocation().getLatitude(), meetingPoint.getOtherLocation().getLongitude()));
				resultTuple.addAttributeValue(3, otherGeometry);
				
				resultTuple.addAttributeValue(4, meetingPoint.getDistanceInMeters());

				resultTuple.addAttributeValue(5, meetingPoint.getMeetingTime());

				tupleList.add(resultTuple);
			}
		}

		Tuple<ITimeInterval> listTuple = new Tuple<>(2, false);
		listTuple.addAttributeValue(0, movingObjectID);
		listTuple.addAttributeValue(1, tupleList);
		return listTuple;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}
