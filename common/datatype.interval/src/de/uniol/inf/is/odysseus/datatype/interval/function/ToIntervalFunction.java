/**
 * 
 */
package de.uniol.inf.is.odysseus.datatype.interval.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToIntervalFunction extends AbstractFunction<IntervalDouble> {
    /**
     * 
     */
    private static final long serialVersionUID = 8339004565543022768L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{SDFDatatype.NUMBERS, SDFDatatype.NUMBERS};

    public ToIntervalFunction() {
    	super("toInterval",2, accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
    }
    

    @Override
    public IntervalDouble getValue() {
        final double a = this.getNumericalInputValue(0);
        final double b = this.getNumericalInputValue(1);
        return new IntervalDouble(a, b);
    }


}
