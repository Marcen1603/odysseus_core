package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialPattern;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialPatternAO;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

@SuppressWarnings("rawtypes")
public class SpatialAreaPatternPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private SpatialPattern pattern;

	// The names of the attributes in the data streams for the moving objects
	private String latMovingName = "";
	private String lngMovingName = "";
	private String latTargetName = "";
	private String lngTargetName = "";

	// Internal: To detect if the first tuple was really the first tuple
	private boolean firstValue = true;
	// The distance between the two objects before the current tuple
	private double lastDistance;
	// Coordinates of the moving object (port 0) from the last known tuple
	private double lastMovingLat;
	private double lastMovingLng;

	// true, if the target is moving. If that's the case, the input data stream
	// of the two objects need to be joined beforehand, so they get in joined on
	// port 0.
	private boolean movingTarget;
	// The radius around the target in which it's detected that the moving
	// object is at the target
	private double radius;
	// Coordinates from the target
	// TODO We should use an geoobject here as well instead of lat and lng
	// directly
	private double latTarget;
	private double lngTarget;

	public SpatialAreaPatternPO(SpatialPatternAO ao) {

		// Set parameters that are used in any case
		this.pattern = ao.getSpatialPattern();
		this.movingTarget = ao.isMovingTarget();
		this.radius = ao.getRadius();
		this.latMovingName = ao.getLatMovingName();
		this.lngMovingName = ao.getLngMovingName();

		if (movingTarget) {
			// We have to get the latitude and longitude for the target from the
			// second port
			this.latTargetName = ao.getLatTargetName();
			this.lngTargetName = ao.getLngTargetName();
		} else {
			// We have to use the given latitude and longitude as the target
			this.latTarget = ao.getTargetLatitude();
			this.lngTarget = ao.getTargetLongitude();
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Do nothing: A punctuation does not change the state of the moving
		// object

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T tuple, int port) {

		// TODO: In case that the target is moving, we need to have a join
		// operator before this -> Then we'll only have 1 input port

		// Update the current positions of the objects
		updatePosition(tuple, port);

		// Calculate distance between object and target
		double distance = calculateDistance() * 1000;

		if (firstValue) {
			// We don't know how far away the moving object was before the
			// beginning, therefore we take the first distance as last distance
			lastDistance = distance;
			firstValue = false;
		}

		if (pattern == SpatialPattern.REACHED_AREA && reachedArea(distance)) {
			// We just entered the target area -> transfer the tuple
			transfer(tuple);
		} else if (pattern == SpatialPattern.LEFT_AREA && leftArea(distance)) {
			// We just left the target area -> transfer the tuple
			transfer(tuple);
		}

		lastDistance = distance;
	}

	/**
	 * Tells, if the objects reached each other at this moment. If the distance
	 * was higher than the radius and now is smaller or equals, it returns true.
	 * If it was already smaller, it will return false, as the reach-moment is
	 * already in the past.
	 * 
	 * The equals is important: If the radius is 0, is reaches the target when
	 * it's exactly at the same position.
	 * 
	 * @param distance
	 *            The distance between the two objects
	 * @return true, if the objects reached each other from last time to this
	 *         time; false, if not.
	 */
	private boolean reachedArea(double distance) {
		// Check, if the moving object was out of the reach of the target
		if (lastDistance > radius) {
			// We were not in the radius
			if (distance <= radius) {
				// We just entered the target area
				return true;
			}
		}
		return false;
	}

	/**
	 * Tells, if the objects left (each other) at this moment (from the last
	 * measurement to this measurement). If the distance was smaller than the
	 * radius and now is greater or equals, it returns true. If it was already
	 * higher, it will return false, as the leave-moment is already in the past.
	 * 
	 * @param distance
	 *            The distance between the two objects
	 * @return true, if the objects left each other from last time to this time;
	 *         false, if not
	 */
	private boolean leftArea(double distance) {
		// Check, if the moving object was inside the reach of the target
		if (lastDistance < radius) {
			// We were not in the radius
			if (distance > radius) {
				// We just left the target area
				return true;
			}
		}
		return false;
	}

	/**
	 * Writes the coordinates from the tuple into the variables
	 * 
	 * @param tuple
	 *            The newest tuple
	 * @param port
	 *            The port on which the tuple got in
	 */
	private void updatePosition(T tuple, int port) {
		// In any case get the position of the moving object (not the target)
		this.lastMovingLat = getValue(latMovingName, tuple, port);
		this.lastMovingLng = getValue(lngMovingName, tuple, port);

		if (movingTarget) {
			// Only if we get the coordinates of the target as a data stream get
			// the coordinates from the tuple
			this.latTarget = getValue(latTargetName, tuple, port);
			this.lngTarget = getValue(lngTargetName, tuple, port);
		}
	}

	/**
	 * Calculates the distance between two points in the earth
	 * 
	 * @return The distance in km between the two points
	 */
	private double calculateDistance() {
		Coordinate coord1 = new Coordinate(lastMovingLat, lastMovingLng);
		Coordinate coord2 = new Coordinate(latTarget, lngTarget);
		
		// TODO Use correct Coordinate Reference system
		MetricSpatialUtils spatialUtils = MetricSpatialUtils.getInstance();
		return spatialUtils.calculateDistance(coord1, coord2);
	}

	/**
	 * Returns the value from the given attribute in the given tuple
	 * 
	 * @param valueAttributeName
	 *            The name of the attribute in the tuple
	 * @param tuple
	 *            The tuple with the data
	 * @param port
	 *            The port on which the tuple got in
	 * @return The value as double
	 */
	private double getValue(String valueAttributeName, T tuple, int port) {
		int valueIndex = getInputSchema(port).findAttributeIndex(valueAttributeName);
		if (valueIndex >= 0) {
			double sensorValue = tuple.getAttribute(valueIndex);
			return sensorValue;
		}
		return 0;
	}

}
