package de.uniol.inf.is.odysseus.systemload.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.systemload.SystemLoadPlugIn;

public class NETLoadFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 1L;

	public NETLoadFunction() {
		super("NETLoad", 0, null, SDFDatatype.DOUBLE, false);
	}
	
	@Override
	public Double getValue() {
		return SystemLoadPlugIn.SIGAR_WRAPPER.getNetInputRate() + SystemLoadPlugIn.SIGAR_WRAPPER.getNetOutputRate();
	}

}
