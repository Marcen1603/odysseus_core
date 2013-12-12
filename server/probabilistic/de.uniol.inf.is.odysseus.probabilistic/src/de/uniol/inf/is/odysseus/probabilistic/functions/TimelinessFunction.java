/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.functions;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TimelinessFunction extends AbstractProbabilisticFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 4703040530419998760L;

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getArity()
     */
    @Override
    public final int getArity() {
        return 1;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
     */
    @Override
    public final String getSymbol() {
        return "timeliness";
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final Double getValue() {
        double frequency = this.getNumericalInputValue(0);
        long applicationTime = System.currentTimeMillis();
        long streamTime = ((ITimeInterval) getMetaAttribute()).getStart().getMainPoint();

        double timeliness = (1.0 - (applicationTime - streamTime) / frequency);
        return timeliness;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
     */
    @Override
    public final SDFDatatype getReturnType() {
        return SDFProbabilisticDatatype.PROBABILISTIC_BYTE;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[] ACC_TYPES = SDFDatatype.NUMBERS;

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
     */
    @Override
    public final SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return TimelinessFunction.ACC_TYPES;
    }

}
