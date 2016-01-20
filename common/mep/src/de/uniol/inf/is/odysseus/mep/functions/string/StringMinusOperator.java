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

    private static final long serialVersionUID = 1216499852673159550L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.STRING }, { SDFDatatype.STRING } };

	public StringMinusOperator() {
		super("-", accTypes, SDFDatatype.STRING);
	}
    
    @Override
    public int getPrecedence() {
        return 6;
    }

    @Override
    public String getValue() {
		String a = getInputValue(0);
		String b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
        return a.replace(b, "");
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


}
