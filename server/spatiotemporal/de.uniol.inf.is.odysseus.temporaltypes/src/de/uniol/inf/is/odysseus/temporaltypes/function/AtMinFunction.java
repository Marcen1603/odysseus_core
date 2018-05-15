package de.uniol.inf.is.odysseus.temporaltypes.function;

import java.util.Collection;

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

	@Override
	public GenericTemporalType<?> getValue() {

		if (!(this.getInputValue(0) instanceof TemporalType<?>)) {
			return null;
		}

		TemporalType<?> temporalType = this.getInputValue(0);
		GenericTemporalType<Comparable> result = new GenericTemporalType<>();
		IValidTimes validTimes = this.getInputValue(1);

		Comparable minValue = null;
		PointInTime minPointInTime = null;
		for (IValidTime validTime : validTimes.getValidTimes()) {
			for (PointInTime currentTime = validTime.getValidStart(); currentTime
					.before(validTime.getValidEnd()); currentTime = currentTime.plus(1)) {
				Object value = temporalType.getValue(currentTime);
				if (value instanceof Comparable) {
					Comparable currentValue = (Comparable) value;
					if (minValue == null || currentValue.compareTo(minValue) < 0) {
						minValue = currentValue;
						minPointInTime = currentTime;
					}
					// TODO If = 0 -> Add to list, as atMin can have multiple results
				}
			}
		}
		
		result.setValue(minPointInTime, minValue);

		/*
		 * TODO Create new fitting temporal type OR (probably better) create new
		 * metadata with changed valid time and same temporal type
		 */
		return result;
	}

	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		return TemporalDatatype.getTemporalConstraint();
	}

}
