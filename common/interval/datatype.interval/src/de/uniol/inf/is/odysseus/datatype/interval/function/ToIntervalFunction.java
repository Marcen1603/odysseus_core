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
    public static final SDFDatatype[] accTypes = SDFDatatype.NUMBERS;

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): the interval inf and sup");
        }
        return accTypes;
    }

    @Override
    public String getSymbol() {
        return "toInterval";
    }

    @Override
    public IntervalDouble getValue() {
        final double a = this.getNumericalInputValue(0);
        final double b = this.getNumericalInputValue(1);
        return new IntervalDouble(a, b);
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFIntervalDatatype.INTERVAL_DOUBLE;
    }

}
