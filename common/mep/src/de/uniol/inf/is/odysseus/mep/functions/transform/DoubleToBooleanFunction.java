/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DoubleToBooleanFunction extends AbstractFunction<Boolean> {

    /**
     * 
     */
    private static final long serialVersionUID = -5506465071311430245L;

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public String getSymbol() {
        return "doubleToBoolean";
    }

    @Override
    public Boolean getValue() {
        return getNumericalInputValue(0).doubleValue() != 0.0;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.BOOLEAN;
    }

    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DOUBLE };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > 0) {
            throw new IllegalArgumentException("doubleToBoolean has only 1 argument.");
        }
        return accTypes;
    }

}
