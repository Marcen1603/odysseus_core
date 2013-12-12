/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TimelinessFunction extends AbstractFunction<Double> {

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
        Objects.requireNonNull(this.getInputValue(0));
        Objects.requireNonNull(getMetaAttribute());
        double frequency = this.getNumericalInputValue(0);
        PointInTime applicationTime = PointInTime.currentPointInTime();
        PointInTime streamTime = ((ITimeInterval) getMetaAttribute()).getStart();
        PointInTime difference = applicationTime.minus(streamTime);
        double timeliness = (1.0 - difference.getMainPoint() / (1000.0 / frequency));
        if (timeliness < 0.0) {
            timeliness = 0.0;
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean optimizeConstantParameter() {
        // We need access to the meta data of each tuple
        return false;
    }
}
