package de.uniol.inf.is.odysseus.server.opcua.func;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.server.opcua.core.OPCValue;
import de.uniol.inf.is.odysseus.server.opcua.core.SDFOPCUADatatype;

public class ErrorFunction extends AbstractFunction<Long>implements IFunction<Long> {

	private static final long serialVersionUID = -2291416120805547628L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFOPCUADatatype.types };

	public ErrorFunction() {
		super("Error", 1, accTypes, SDFDatatype.INTEGER);
	}

	@Override
	public Long getValue() {
		OPCValue<?> value = getInputValue(0);
		return value.getError();
	}
}