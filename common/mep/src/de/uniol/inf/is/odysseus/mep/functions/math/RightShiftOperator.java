package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryNumberInputOperator;

public class RightShiftOperator extends AbstractBinaryNumberInputOperator<Long> {
	

	private static final long serialVersionUID = -1860681482039136473L;

	public RightShiftOperator() {
		super(">>", SDFDatatype.LONG);
	}

	@Override
	public int getPrecedence() {
		return 2;
	}
	
	@Override
	public Long getValue() {
		Long in = getNumericalInputValue(0).longValue();
		Integer shift = getNumericalInputValue(1).intValue();
		
		return in >> shift;
	}

	


}
