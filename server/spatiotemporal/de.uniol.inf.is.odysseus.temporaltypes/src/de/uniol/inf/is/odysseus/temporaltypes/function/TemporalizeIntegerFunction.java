package de.uniol.inf.is.odysseus.temporaltypes.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.IntegerFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.LinearIntegerFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalInteger;

public class TemporalizeIntegerFunction extends AbstractFunction<TemporalInteger> {
	
	private static final long serialVersionUID = -3345543531201341289L;
	
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.INTEGER }
	};

	public TemporalizeIntegerFunction() {
		super("TemporalizeInteger", 1, accTypes, TemporalDatatype.TEMPORAL_INTEGER);
	}

	@Override
	public TemporalInteger getValue() {
		IntegerFunction function = new LinearIntegerFunction(0, 1);
		return new TemporalInteger(function);
	}

}
