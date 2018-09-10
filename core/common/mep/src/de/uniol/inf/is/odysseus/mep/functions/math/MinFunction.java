package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryNumberInputFunction;

public class MinFunction extends AbstractBinaryNumberInputFunction<Number> {

	private static final long serialVersionUID = -4935976205476611352L;

	public MinFunction(){
		super("min", SDFDatatype.DOUBLE);
	}
	
	@Override
	public Number getValue() {
		Number n1 = getNumericalInputValue(0);
		Number n2 = getNumericalInputValue(1);
		if (n1.doubleValue()<n2.doubleValue()){
			return n1;
		}
		return n2;
	}

}
