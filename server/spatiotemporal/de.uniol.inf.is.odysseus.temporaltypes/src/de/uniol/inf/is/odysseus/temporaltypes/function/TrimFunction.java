package de.uniol.inf.is.odysseus.temporaltypes.function;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * Trims a GenericTemporalType to the given ValidTimes so that no more uncovered
 * values are stored in that object
 * 
 * @author Tobias Brandt
 *
 */
public class TrimFunction extends AbstractFunction<TemporalType<?>> implements TemporalFunction {

	private static final long serialVersionUID = 2645182798670576378L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.OBJECT } };

	public TrimFunction() {
		super("TrimTemporal", 2, accTypes, SDFDatatype.OBJECT);
	}

	@Override
	public TemporalType<?> getValue() {
		if (!(this.getInputValue(0) instanceof GenericTemporalType<?>)) {
			return null;
		}
		GenericTemporalType<?> temporalType = this.getInputValue(0);
		GenericTemporalType<?> workingObject = temporalType.clone();
		IValidTimes validTimes = this.getInputValue(1);
		workingObject.trim(validTimes, this.getBaseTimeUnit());
		return workingObject;
	}

	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		return TemporalDatatype.getTemporalConstraint();
	}

}
