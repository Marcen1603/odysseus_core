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
		Number a = getInputValue(0);
		Number b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		Long in = a.longValue();
		Integer shift = b.intValue();
		
		return in >> shift;
	}

	


}
