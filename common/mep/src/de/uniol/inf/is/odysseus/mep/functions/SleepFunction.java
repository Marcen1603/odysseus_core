package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class SleepFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 2154239054532126650L;
    private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG };

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException(
                    "negative argument index not allowed");
        }
        if (argPos > 0) {
            throw new IllegalArgumentException(this.getSymbol() + " has only "
                    + this.getArity() + " argument(s).");
        }
        return accTypes;
    }

    @Override
    public String getSymbol() {
        return "sleep";
    }

    @Override
    public Double getValue() {
        int time = getNumericalInputValue(0).intValue();
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 1.0;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DOUBLE;
    }
    
	@Override
	public boolean optimizeConstantParameter() {
		return false;
	}

}
