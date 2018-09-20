package de.uniol.inf.is.odysseus.temporaltypes.function;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

public class DerivativeFunction extends AbstractFunction<GenericTemporalType<?>> implements TemporalFunction {

	private static final long serialVersionUID = 624825705872252642L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.OBJECT } };

	public DerivativeFunction() {
		super("derivative", 2, accTypes, SDFDatatype.OBJECT);
	}

	@Override
	public GenericTemporalType<?> getValue() {

		if (!(this.getInputValue(0) instanceof TemporalType<?>)) {
			return null;
		}

		TemporalType<Double> temporalReal = this.getInputValue(0);
		IPredictionTimes predictionTimes = this.getInputValue(1);

		GenericTemporalType<Double> result = new GenericTemporalType<>();
		PointInTime prevTime = null;

		for (IPredictionTime predictionTime : predictionTimes.getPredictionTimes()) {
			for (PointInTime currentTime = predictionTime.getPredictionStart(); currentTime
					.before(predictionTime.getPredictionEnd()); currentTime = currentTime.plus(1)) {
				
				// Convert into stream time, cause temporal types always work in stream time
				PointInTime inStreamTime = new PointInTime(
						this.getBaseTimeUnit().convert(currentTime.getMainPoint(), predictionTimes.getPredictionTimeUnit()));

				/*
				 * For the very first real number there is no derivative (coming from where? 0?
				 * It's not known)
				 */
				if (prevTime == null) {
					prevTime = inStreamTime;
					result.setValue(inStreamTime, 0.0);
					continue;
				}

				// From the second real on the temporal real has a derivative
				double derivative = calculateDerivative(temporalReal, inStreamTime, prevTime);
				result.setValue(inStreamTime, derivative);
				prevTime = inStreamTime;
			}
		}

		return result;
	}

	private double calculateDerivative(TemporalType<Double> temporalReal, PointInTime currentTime,
			PointInTime prevTime) {
		double prevValue = temporalReal.getValue(prevTime);
		double currentValue = temporalReal.getValue(currentTime);
		double diff = currentValue - prevValue;
		long temporalDiff = currentTime.minus(prevTime).getMainPoint();
		double m = diff / temporalDiff;
		return m;
	}

}
