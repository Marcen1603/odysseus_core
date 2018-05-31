package de.uniol.inf.is.odysseus.temporaltypes.function;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.integer.LinearIntegerFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.integer.TemporalInteger;

/**
 * An example for a map function that converts an integer to a TemporalInteger.
 * 
 * @author Tobias Brandt
 *
 */
public class TemporalizeIntegerFunction extends AbstractFunction<TemporalInteger> {

	private static final long serialVersionUID = -3345543531201341289L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.INTEGER } };

	public TemporalizeIntegerFunction() {
		super("TemporalizeInteger", 1, accTypes, SDFDatatype.INTEGER);
	}

	@Override
	public TemporalInteger getValue() {
		TemporalFunction<Integer> function = new LinearIntegerFunction(0, 1);
		return new TemporalInteger(function);
	}
	
	@Override
	public Collection<SDFConstraint> getConstraintsToAdd() {
		return TemporalDatatype.getTemporalConstraint();
	}

}
