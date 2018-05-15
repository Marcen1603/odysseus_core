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

public class AtMinFunction extends AbstractFunction<GenericTemporalType<?>> implements TemporalFunction {

	private static final long serialVersionUID = -8593497637917376057L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.OBJECT } };

	public AtMinFunction() {
		super("atMin", 2, accTypes, SDFDatatype.OBJECT);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public GenericTemporalType<?> getValue() {

		if (!(this.getInputValue(0) instanceof TemporalType<?>)) {
			return null;
		}

		TemporalType<?> temporalType = this.getInputValue(0);
		GenericTemporalType<Comparable> result = new GenericTemporalType<>();
		IValidTimes validTimes = this.getInputValue(1);

		List<Comparable> minValues = new ArrayList<>();
		List<PointInTime> pointsInTime = new ArrayList<>();

		for (IValidTime validTime : validTimes.getValidTimes()) {
			for (PointInTime currentTime = validTime.getValidStart(); currentTime
					.before(validTime.getValidEnd()); currentTime = currentTime.plus(1)) {
				Object value = temporalType.getValue(currentTime);
				if (value instanceof Comparable) {
					Comparable currentValue = (Comparable) value;
					if (minValues.isEmpty() || currentValue.compareTo(minValues.get(0)) < 0) {
						minValues.clear();
						pointsInTime.clear();
						minValues.add(currentValue);
						pointsInTime.add(currentTime);
					} else if (!minValues.isEmpty() && currentValue.compareTo(minValues.get(0)) == 0) {
						minValues.add(currentValue);
						pointsInTime.add(currentTime);
					}
				}
			}
		}

		for (int i = 0; i < minValues.size(); i++) {
			result.setValue(pointsInTime.get(i), minValues.get(i));
		}

		return result;
	}

	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		return TemporalDatatype.getTemporalConstraint();
	}

}
