package de.uniol.inf.is.odysseus.server.opcua.func;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.server.opcua.core.OPCValue;
import de.uniol.inf.is.odysseus.server.opcua.core.SDFOPCUADatatype;

public class ToOPCValueFunction extends AbstractFunction<OPCValue<Double>>implements IFunction<OPCValue<Double>> {

	private static final long serialVersionUID = -4024328742545051468L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS,
			SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

	public ToOPCValueFunction() {
		super("ToOPCValue", 4, accTypes, SDFOPCUADatatype.OPCVALUE);
	}

	@Override
	public OPCValue<Double> getValue() {
		long timestamp = getNumericalInputValue(0).longValue();
		double value = getNumericalInputValue(1).doubleValue();
		int quality = getNumericalInputValue(2).shortValue();
		long error = getNumericalInputValue(3).intValue();
		return new OPCValue<Double>(timestamp, value, quality, error);
	}
}