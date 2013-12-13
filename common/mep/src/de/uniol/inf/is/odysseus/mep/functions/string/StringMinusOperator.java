/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class StringMinusOperator extends AbstractBinaryOperator<String> {

    /**
     * 
     */
    private static final long serialVersionUID = 1216499852673159550L;

    @Override
    public int getPrecedence() {
        return 6;
    }

    @Override
    public String getSymbol() {
        return "-";
    }

    @Override
    public String getValue() {
        String a = getInputValue(0);
        String b = getInputValue(1);
        return a.replace(b, "");
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.STRING;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(IOperator<String> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<String> operator) {
        return false;
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.STRING };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return accTypes;
    }

}
