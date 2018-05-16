package de.uniol.inf.is.odysseus.temporaltypes.function;

import com.vividsolutions.jts.geom.Coordinate;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * The speed function from the Moving Object Algebra. Uses not the Euclidean
 * distance but the orthodromic distance to respect the shape of the Earth.
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
		IValidTimes validTimes = this.getInputValue(1);

		GenericTemporalType<Double> result = new GenericTemporalType<>();
		PointInTime prevTime = null;

		for (IValidTime validTime : validTimes.getValidTimes()) {
			for (PointInTime currentTime = validTime.getValidStart(); currentTime
					.before(validTime.getValidEnd()); currentTime = currentTime.plus(1)) {

				// For the very first location there is no speed
				if (prevTime == null) {
					prevTime = currentTime;
					result.setValue(currentTime, 0.0);
					continue;
				}

				// From the second point on the moving object has a speed
				double speed = calculateSpeed(temporalPoint, currentTime, prevTime);
				result.setValue(currentTime, speed);
				prevTime = currentTime;
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
