package de.uniol.inf.is.odysseus.server.opcua.func;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.server.opcua.core.OPCValue;
import de.uniol.inf.is.odysseus.server.opcua.core.SDFOPCUADatatype;

public class TimestampFunction extends AbstractFunction<Long>implements IFunction<Long> {

	private static final long serialVersionUID = -1156408325270899097L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFOPCUADatatype.types };

	public TimestampFunction() {
		super("Timestamp", 1, accTypes, SDFDatatype.TIMESTAMP);
	}

	@Override
	public Long getValue() {
		OPCValue<?> value = getInputValue(0);
		return value.getTimestamp();
	}
}