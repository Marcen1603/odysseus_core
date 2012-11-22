package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * Returns a new string that is a substring of this string
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SubStringFunction extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long            serialVersionUID = 2270358376473789092L;
    private static final SDFDatatype[][] accTypes         = new SDFDatatype[][] { { SDFDatatype.STRING },
            { SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG },
            { SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG } };

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): a string and the begin and end index");
        }
        return accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "substring";
    }

    @Override
    public String getValue() {
        return ((String) getInputValue(0)).substring(getNumericalInputValue(1).intValue(), getNumericalInputValue(2)
                .intValue());
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.STRING;
    }
}
