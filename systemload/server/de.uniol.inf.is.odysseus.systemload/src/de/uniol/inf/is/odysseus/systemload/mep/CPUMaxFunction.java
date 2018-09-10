package de.uniol.inf.is.odysseus.systemload.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.systemload.SystemLoadPlugIn;

public class CPUMaxFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 1L;

	public CPUMaxFunction() {
		super("CPUMax", 0, null, SDFDatatype.DOUBLE, true);
	}
	
	@Override
	public Double getValue() {
		return SystemLoadPlugIn.SIGAR_WRAPPER.getCpuMax(); // constant, since # of processors
	}

}
