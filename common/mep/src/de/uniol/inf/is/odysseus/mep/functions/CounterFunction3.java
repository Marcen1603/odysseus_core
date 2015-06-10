package de.uniol.inf.is.odysseus.mep.functions;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IStatefulFunction;

public class CounterFunction3 extends AbstractFunction<Long> implements IStatefulFunction{

	private static final long serialVersionUID = 7678906904911837795L;
	private Long lastValue = 0L;
	
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		new SDFDatatype[] { SDFDatatype.BOOLEAN}};

	
	public CounterFunction3() {
		super("counter",1,accTypes,SDFDatatype.LONG,false);
	}
	
	@Override
	public Long getValue() {
		Boolean count = (Boolean)getInputValue(0);
		if (count){
			lastValue++;
		}else{
			lastValue = 0L;
		}
		return lastValue;
	}

	@Override
	public Serializable getState() {
		return lastValue;
	}
	
	@Override
	public void setState(Serializable state) {
		this.lastValue = (Long) state;
	}
	
}
