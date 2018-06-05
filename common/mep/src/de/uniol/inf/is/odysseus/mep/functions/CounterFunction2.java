package de.uniol.inf.is.odysseus.mep.functions;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IStatefulFunction;

public class CounterFunction2 extends AbstractFunction<Long> implements IStatefulFunction{

	private static final long serialVersionUID = 7678906904911837795L;
	private Long lastValue = 0L;
	private boolean init = false;
	
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		new SDFDatatype[] { SDFDatatype.BYTE, SDFDatatype.INTEGER,
				SDFDatatype.LONG },
		new SDFDatatype[] { SDFDatatype.BYTE, SDFDatatype.INTEGER,
				SDFDatatype.LONG } };

	
	public CounterFunction2() {
		super("counter",2,accTypes,SDFDatatype.LONG,false);
	}
	
	private CounterFunction2(CounterFunction2 counterFunction2) {
		this();
		this.lastValue = counterFunction2.lastValue;
		this.init = counterFunction2.init;
	}

	@Override
	public Long getValue() {
		if (!init){
			lastValue = getNumericalInputValue(0).longValue();
			init = true;
		}else{
			if (lastValue < getNumericalInputValue(1).longValue()){
				lastValue++;
			}else{
				lastValue = getNumericalInputValue(0).longValue();
			}
		}
		return lastValue;
	}

	@Override
	public Serializable getState() {
		Pair<Long,Boolean> state = new Pair<>();
		state.setE1(lastValue);
		state.setE2(init);
		return state;
	}
	
	@Override
	public void setState(Serializable state) {
		@SuppressWarnings("unchecked")
		Pair<Long,Boolean> p = (Pair<Long,Boolean>) state;
		this.lastValue = p.getE1();
		this.init = p.getE2();
	}
	
	@Override
	public IMepExpression<Long> clone() {
		return new CounterFunction2(this);
	}
	
}
