package de.uniol.inf.is.odysseus.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class StringPlusOperator extends AbstractBinaryOperator<String> {



	/**
	 * 
	 */
	private static final long serialVersionUID = -6758609091849696249L;

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public String getSymbol() {
		return "+";
	}

	@Override
	public String getValue() {
		StringBuilder sb = new StringBuilder();
		sb.append(getInputValue(0));
		sb.append(getInputValue(1));
		return sb.toString();
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.STRING;
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

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.STRING};
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos >= this.getArity()){
			throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
		}
        //			accTypes[1] = String.class; // string concatenation
        return accTypes;
	}

}
