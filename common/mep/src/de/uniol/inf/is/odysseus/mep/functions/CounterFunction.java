package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class CounterFunction extends AbstractFunction<Long> {

	private static final long serialVersionUID = 7678906904911837795L;
	private Long lastValue = 0L;
	
	public CounterFunction() {
		super("counter",0,null,SDFDatatype.LONG,false);
	}
	
	@Override
	public Long getValue() {
		return lastValue++;
	}

}
