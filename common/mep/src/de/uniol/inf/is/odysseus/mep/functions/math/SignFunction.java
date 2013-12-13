/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class SignFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 5921326492861280908L;

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public String getSymbol() {
        return "sign";
    }

    @Override
    public Double getValue() {
        return Math.signum(getNumericalInputValue(0));
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DOUBLE;
    }

    public static final SDFDatatype[] accTypes =  SDFDatatype.NUMBERS;
    
    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos){
        if(argPos < 0){
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if(argPos > 0){
            throw new IllegalArgumentException("MatrixTrans has only 1 argument.");
        }
        return accTypes;
    }

}
