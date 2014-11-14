package de.uniol.inf.is.odysseus.mep.functions.bit;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryNumberInputOperator;

public class BitAndOperator extends AbstractBinaryNumberInputOperator<Long> {
	
	private static final long serialVersionUID = -730023743642274236L;

	public BitAndOperator() {
		super("&", SDFDatatype.LONG);
	}

	@Override
	public int getPrecedence() {
		return 2;
	}
	
	@Override
	public Long getValue() {
		Long left = getNumericalInputValue(0).longValue();
		Long right  = getNumericalInputValue(1).longValue();
		
		return left & right;
	}

	


}
