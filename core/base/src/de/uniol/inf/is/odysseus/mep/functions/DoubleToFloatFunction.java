package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class DoubleToFloatFunction  extends AbstractFunction<Float> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6665008272296004433L;

	@Override
    public int getArity() {
        return 1;
    }

    @Override
    public String getSymbol() {
        return "doubleToFloat";
    }

    @Override
    public Float getValue() {
        return getNumericalInputValue(0).floatValue();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.FLOAT;
    }

    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DOUBLE};
    
    public SDFDatatype[] getAcceptedTypes(int argPos){
        if(argPos < 0){
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if(argPos > 0){
            throw new IllegalArgumentException("doubleToFloat has only 1 argument.");
        }
        else{
            return accTypes;
        }
    }

}
