package de.uniol.inf.is.odysseus.server.opcua.func;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.server.opcua.core.OPCValue;
import de.uniol.inf.is.odysseus.server.opcua.core.SDFOPCUADatatype;

public class QualityFunction extends AbstractFunction<Integer>implements IFunction<Integer> {

	private static final long serialVersionUID = -5815276686533770744L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFOPCUADatatype.types };

	public QualityFunction() {
		super("Quality", 1, accTypes, SDFDatatype.SHORT);
	}

	@Override
	public Integer getValue() {
		OPCValue<?> value = getInputValue(0);
		return value.getQuality();
	}
}