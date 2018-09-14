package de.uniol.inf.is.odysseus.temporaltypes.function;

import com.vividsolutions.jts.geom.Coordinate;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * The speed function from the Moving Object Algebra. Uses not the Euclidean
 * distance but the orthodromic distance to respect the shape of the Earth. The
 * speed is given in meters per time instance of the stream time base time unit.
 * 
 * @author Tobias Brandt
 *
 */
public class SpeedFunction extends AbstractFunction<GenericTemporalType<?>> implements TemporalFunction {

	private static final long serialVersionUID = -4062575755075928525L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.OBJECT } };

	public SpeedFunction() {
		super("speed", 2, accTypes, SDFDatatype.OBJECT);
	}

	@Override
	public GenericTemporalType<?> getValue() {

		if (!(this.getInputValue(0) instanceof TemporalType<?>)) {
			return null;
		}

		TemporalType<GeometryWrapper> temporalPoint = this.getInputValue(0);
		IPredictionTimes predictionTimes = this.getInputValue(1);

		GenericTemporalType<Double> result = new GenericTemporalType<>();
		PointInTime prevTime = null;

		for (IPredictionTime predictionTime : predictionTimes.getPredictionTimes()) {
			for (PointInTime currentTime = predictionTime.getPredictionStart(); currentTime
					.before(predictionTime.getPredictionEnd()); currentTime = currentTime.plus(1)) {

				// Convert into stream time, cause temporal types always work in stream time
				PointInTime inStreamTime = new PointInTime(this.getBaseTimeUnit().convert(currentTime.getMainPoint(),
						predictionTimes.getPredictionTimeUnit()));

				// For the very first location there is no speed
				if (prevTime == null) {
					prevTime = inStreamTime;
					result.setValue(inStreamTime, 0.0);
					continue;
				}

				// From the second point on the moving object has a speed
				double speed = calculateSpeed(temporalPoint, inStreamTime, prevTime);

				result.setValue(inStreamTime, speed);
				prevTime = inStreamTime;
			}
		}

		return result;
	}

	/**
	 * Calculates the speed in meters per time instance
	 */
	private double calculateSpeed(TemporalType<GeometryWrapper> temporalPoint, PointInTime currentTime,
			PointInTime prevTime) {
		GeometryWrapper prevValue = temporalPoint.getValue(prevTime);
		GeometryWrapper currentValue = temporalPoint.getValue(currentTime);
		Coordinate coord1 = prevValue.getGeometry().getCoordinate();
		Coordinate coord2 = currentValue.getGeometry().getCoordinate();
		double distance = MetricSpatialUtils.getInstance().calculateDistance(coord1, coord2);
		double speed = distance / (currentTime.minus(prevTime).getMainPoint());
		return speed;
	}

}
