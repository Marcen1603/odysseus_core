package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class CounterFunction2 extends AbstractFunction<Long> {

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

}
