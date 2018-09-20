package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.function.RemoveTemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.function.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * Converts a temporal point to a non-temporal trajectory.
 * 
 * @author Tobias Brandt
 *
 */
public class TrajectoryFunction extends AbstractFunction<GeometryWrapper>
		implements TemporalFunction, RemoveTemporalFunction {

	private static final long serialVersionUID = -6403419218488014090L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.OBJECT } };

	private GeometryFactory factory;

	public TrajectoryFunction() {
		super("Trajectory", 2, accTypes, SDFDatatype.OBJECT);
		this.factory = new GeometryFactory();
	}

	@Override
	public GeometryWrapper getValue() {

		if (!(this.getInputValue(0) instanceof TemporalType<?>)) {
			return null;
		}

		TemporalType<GeometryWrapper> temporalType = this.getInputValue(0);
		IPredictionTimes predictionTimes = this.getInputValue(1);

		TimeUnit predictionTimeUnit = predictionTimes.getPredictionTimeUnit();
		TimeUnit streamBaseTimeUnit = super.getBaseTimeUnit();

		/*
		 * It could be more than one LineString, because we could have multiple
		 * prediction time intervals
		 */
		LineString[] allTrajectories = new LineString[predictionTimes.getPredictionTimes().size()];
		int trajectoryCounter = 0;

		for (IPredictionTime predictionTime : predictionTimes.getPredictionTimes()) {
			PointInTime predictionStart = predictionTime.getPredictionStart();
			PointInTime predictionEnd = predictionTime.getPredictionEnd();

			int pointCounter = 0;
			int intervalLength = (int) predictionEnd.minus(predictionStart).getMainPoint();
			Coordinate[] coordinates = new Coordinate[intervalLength];
			for (PointInTime i = predictionStart.clone(); i.before(predictionEnd); i = i.plus(1)) {
				/*
				 * Convert this point in time to the stream time, as all prediction functions
				 * use the stream time.
				 */
				long streamTime = streamBaseTimeUnit.convert(i.getMainPoint(), predictionTimeUnit);
				PointInTime inStreamTime = new PointInTime(streamTime);
				Object value = temporalType.getValue(inStreamTime);

				// Collect the coordinates for this LineString
				if (value instanceof GeometryWrapper) {
					GeometryWrapper singlePoint = (GeometryWrapper) value;
					Coordinate pointCoordinate = singlePoint.getGeometry().getCoordinate();
					coordinates[pointCounter] = pointCoordinate;
					pointCounter++;
				}
			}

			// Create the LineString for this prediction time interval
			LineString singleTrajectory = factory.createLineString(coordinates);
			allTrajectories[trajectoryCounter] = singleTrajectory;
			trajectoryCounter++;
		}

		// Collect all LineStrings to a MultiLineString
		MultiLineString multiTrajectory = factory.createMultiLineString(allTrajectories);
		GeometryWrapper result = new GeometryWrapper(multiTrajectory);
		return result;
	}

	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		// In the default case, no constraints are added
		return new ArrayList<SDFConstraint>(0);
	}

}
