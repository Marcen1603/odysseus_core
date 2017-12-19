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
public class StringMultiplicationOperator extends AbstractBinaryOperator<String> {


    private static final long serialVersionUID = -2822365268366862280L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.STRING }, { SDFDatatype.STRING } };

	public StringMultiplicationOperator() {
		super("*", accTypes, SDFDatatype.STRING);
	}
	
    @Override
    public int getPrecedence() {
        return 5;
    }

    @Override
    public String getValue() {
		String a = getInputValue(0);
		Long b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b; i++) {
            sb.append(a);
        }
        return sb.toString();
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean isAssociative() {
        return true;
    }

    @Override
    public boolean isLeftDistributiveWith(IOperator<String> operator) {
        return operator.getClass() == StringPlusOperator.class || operator.getClass() == StringMinusOperator.class;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<String> operator) {
        return operator.getClass() == StringPlusOperator.class || operator.getClass() == StringMinusOperator.class;
    }

}
