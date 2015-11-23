package de.uniol.inf.is.odysseus.server.opcua.func;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.server.opcua.core.OPCValue;
import de.uniol.inf.is.odysseus.server.opcua.core.SDFOPCUADatatype;

public class ValueFunction<T> extends AbstractFunction<T>implements IFunction<T> {

	private static final long serialVersionUID = -354603068556899719L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFOPCUADatatype.types };

	public ValueFunction() {
		super("Value", 1, accTypes, SDFDatatype.DOUBLE);
	}

	@Override
	public T getValue() {
		OPCValue<T> value = getInputValue(0);
		return value.getValue();
	}

	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

	@Override
	public SDFDatatype determineType(IExpression<?>[] args) {
		if (args.length == 1)
			return args[0].getReturnType().getSubType();
		return null;
	}
}