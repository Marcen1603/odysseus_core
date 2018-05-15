package de.uniol.inf.is.odysseus.temporaltypes.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * Represents the atMin / atMax function of the Moving Object Algebra. Can be
 * used within a select operator to reduce the values to the min or max values.
 * 
 * @author Tobias Brandt
 *
 */
public abstract class AtMinMaxFunction extends AbstractFunction<GenericTemporalType<?>> implements TemporalFunction {

	private static final long serialVersionUID = -8593497637917376057L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.OBJECT } };

	public AtMinMaxFunction(String functionName) {
		super(functionName, 2, accTypes, SDFDatatype.OBJECT);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public GenericTemporalType<?> getValue() {

		if (!(this.getInputValue(0) instanceof TemporalType<?>)) {
			return null;
		}

		TemporalType<?> temporalType = this.getInputValue(0);
		GenericTemporalType<Comparable> result = new GenericTemporalType<>();
		IValidTimes validTimes = this.getInputValue(1);

		// These lists will contain the values and times with the min / max values
		List<Comparable> minValues = new ArrayList<>();
		List<PointInTime> pointsInTime = new ArrayList<>();

		// Loop through all available values and pick the min / max ones
		for (IValidTime validTime : validTimes.getValidTimes()) {
			for (PointInTime currentTime = validTime.getValidStart(); currentTime
					.before(validTime.getValidEnd()); currentTime = currentTime.plus(1)) {
				Object value = temporalType.getValue(currentTime);
				if (value instanceof Comparable) {
					Comparable currentValue = (Comparable) value;
					if (isMinMaxValue(minValues, currentValue)) {
						startNewLists(minValues, pointsInTime, currentValue, currentTime);
					} else if (isEqualValue(minValues, currentValue)) {
						// There can be multiple min / max values
						addToExistingLists(minValues, pointsInTime, currentValue, currentTime);
					}
				}
			}
		}

		// Construct the temporal type only with the min / max values
		for (int i = 0; i < minValues.size(); i++) {
			result.setValue(pointsInTime.get(i), minValues.get(i));
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	private void addToExistingLists(List<Comparable> values, List<PointInTime> pointsInTime, Comparable currentValue,
			PointInTime currentTime) {
		values.add(currentValue);
		pointsInTime.add(currentTime);
	}

	@SuppressWarnings("rawtypes")
	private void startNewLists(List<Comparable> values, List<PointInTime> pointsInTime, Comparable currentValue,
			PointInTime currentTime) {
		/*
		 * We found a value that is smaller / bigger than the ones we stored. Clear all
		 * and start from the beginning.
		 */
		values.clear();
		pointsInTime.clear();
		values.add(currentValue);
		pointsInTime.add(currentTime);
	}

	@SuppressWarnings("rawtypes")
	protected abstract boolean isMinMaxValue(List<Comparable> minValues, Comparable currentValue);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean isEqualValue(List<Comparable> minValues, Comparable currentValue) {
		return !minValues.isEmpty() && currentValue.compareTo(minValues.get(0)) == 0;
	}

	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		return TemporalDatatype.getTemporalConstraint();
	}

}
