package de.uniol.inf.odysseus.spatiotemporal.types.point;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * A function for a temporal point that is moving linearly in space.
 * 
 * @author Tobias Brandt
 *
 */
public class LinearMovingPointFunction extends AcceleratingMovingPointFunction {

	/**
	 * 
	 * @param basePoint
	 *            The point from which the calculations are done. The location at
	 *            which the object is at {@code basePointInTime}.
	 * @param basePointInTime
	 *            The time at which the object if at the location {@code basePoint}
	 * @param speedMetersPerTimeInstance
	 *            The meters the object travels per time instance
	 * @param azimuth
	 *            The direction in which the object travels
	 */
	public LinearMovingPointFunction(Geometry basePoint, PointInTime basePointInTime, double speedMetersPerTimeInstance,
			double azimuth) {
		super(basePoint, basePointInTime, speedMetersPerTimeInstance, 0, azimuth);
	}

	public LinearMovingPointFunction(LinearMovingPointFunction other) {
		super(other.basePoint, other.basePointInTime, other.speedMeterPerTimeInstance, 0, other.azimuth);
	}

	@Override
	public LinearMovingPointFunction clone() {
		return new LinearMovingPointFunction(this);
	}

}
