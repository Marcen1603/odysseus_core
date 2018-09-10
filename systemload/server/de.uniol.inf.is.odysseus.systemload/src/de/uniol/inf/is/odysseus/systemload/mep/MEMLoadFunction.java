package de.uniol.inf.is.odysseus.systemload.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MEMLoadFunction extends AbstractFunction<Long> {

	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final long serialVersionUID = 1L;

	public MEMLoadFunction() {
		super("MEMLoad", 0, null, SDFDatatype.LONG, false);
	}
	
	@Override
	public Long getValue() {
		return RUNTIME.totalMemory() - RUNTIME.freeMemory();
	}

}
