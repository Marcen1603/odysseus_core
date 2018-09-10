package de.uniol.inf.is.odysseus.systemload.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MEMMaxFunction extends AbstractFunction<Long> {

	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final long serialVersionUID = 1L;

	public MEMMaxFunction() {
		super("MEMMax", 0, null, SDFDatatype.LONG, true);
	}
	
	@Override
	public Long getValue() {
		return RUNTIME.totalMemory(); // constant
	}

}
