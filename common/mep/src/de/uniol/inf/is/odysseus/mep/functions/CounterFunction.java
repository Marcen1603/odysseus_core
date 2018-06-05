package de.uniol.inf.is.odysseus.mep.functions;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IStatefulFunction;

public class CounterFunction extends AbstractFunction<Long> implements IStatefulFunction{

	private static final long serialVersionUID = 7678906904911837795L;
	private Long lastValue = 0L;
	
	public CounterFunction() {
		super("counter",0,null,SDFDatatype.LONG,false);
	}
	
	private CounterFunction(CounterFunction other) {
		this();
		this.lastValue = other.lastValue;
	}

	@Override
	public Long getValue() {
		return lastValue++;
	}
	
	@Override
	public Serializable getState() {
		return lastValue;
	}
	
	@Override
	public void setState(Serializable state) {
		this.lastValue = (Long) state;
	}

	@Override
	public IMepExpression<Long> clone() {
		return new CounterFunction(this);
	}
	
}
