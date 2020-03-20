/**
 * 
 */
package de.uniol.inf.is.odysseus.datatype.interval.function;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToIntervalFunction2 extends AbstractFunction<IntervalDouble> {
    /**
     * 
     */
    private static final long serialVersionUID = 8339004565543022768L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{SDFDatatype.getLists()};

    public ToIntervalFunction2() {
    	super("toInterval",1, accTypes, SDFIntervalDatatype.INTERVAL_DOUBLE);
    }
    

    @Override
    public IntervalDouble getValue() {
    	final List<Number> l = getInputValue(0);
        return new IntervalDouble(l.get(0).doubleValue(), l.get(1).doubleValue());
    }


}
