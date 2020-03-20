package de.uniol.inf.is.odysseus.datatype.interval.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ElementAtFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 1L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFIntervalDatatype.TYPES, SDFDatatype.DISCRETE_NUMBERS };

    public ElementAtFunction() {
        super("elementAt", 2, ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final IntervalDouble a = this.getInputValue(0);
        final Number pos= this.getNumericalInputValue(1);
        if (pos.intValue() == 0) {
        	return a.inf();
        }else {
        	return a.sup();
        }
    }
	

}
