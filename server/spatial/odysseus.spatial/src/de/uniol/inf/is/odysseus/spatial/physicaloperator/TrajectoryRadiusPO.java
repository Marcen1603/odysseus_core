package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.GeoHashMONoCleanupIndexStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.TrajectoryRadiusAO;

public class TrajectoryRadiusPO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {
	
	private boolean queryAllMovingObjects = true;
	private List<String> movingObjectsToQuery;
	
	public TrajectoryRadiusPO(TrajectoryRadiusAO ao) {
		this.queryAllMovingObjects = ao.isQueryAllMovingObjects();
		this.movingObjectsToQuery = ao.getMovingObjectsToQuery();
	}

	@Override
	protected void process_next(T object, int port) {

		// We received a bunch of new trajectory predictions -> a whole future

		// 1. Create and fill an index structure so that we can work on this set of
		// trajectories
		// TODO Still need name and geometryPosition?
		IMovingObjectDataStructure index = new GeoHashMONoCleanupIndexStructure("bla", 1);
		List<Tuple<ITimeInterval>> trajectories = object.getAttribute(0);
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
			// TODO I don't want / can't really give the geometry (center)
//			index.queryCircle(geometry, radius, t, movingObjectIdToIgnore);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

}
