package de.uniol.inf.is.odysseus.mep.functions.time;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryDiscreteInputFunction;

public class ToTimestampFunction extends AbstractUnaryDiscreteInputFunction<Long> {

	private static final long serialVersionUID = -3154573090831273673L;
	 
	protected ToTimestampFunction(String name) {
    	super(name,SDFDatatype.TIMESTAMP);
    }
    
    public ToTimestampFunction(){
    	this("toTimestamp");
    }
	
	@Override
	public Long getValue() {
		return getNumericalInputValue(0).longValue();
	}

}
