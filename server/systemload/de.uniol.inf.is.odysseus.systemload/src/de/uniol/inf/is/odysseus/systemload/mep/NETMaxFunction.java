package de.uniol.inf.is.odysseus.systemload.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.systemload.SystemLoadPlugIn;

public class NETMaxFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 1L;

	public NETMaxFunction() {
		super("NETMax", 0, null, SDFDatatype.DOUBLE, true);
	}
	
	@Override
	public Double getValue() {
		return Double.valueOf(SystemLoadPlugIn.SIGAR_WRAPPER.getNetMax());
	}

}
