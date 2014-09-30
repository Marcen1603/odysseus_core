package de.uniol.inf.is.odysseus.mep.functions.compare;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.AbstractBinaryBooleanOperator;

public class BooleanEqualsOperator extends AbstractBinaryBooleanOperator implements IHasAlias {

	private static final long serialVersionUID = 5646511007554927109L;

	public BooleanEqualsOperator(){
		super("=");
	}
	
	public BooleanEqualsOperator(SDFDatatype[][] accTypes){
		super("=");
	}
	
	@Override
	public String getAliasName() {
		return "==";
	}
	
	@Override
	public int getPrecedence() {
		return 9;
	}

	@Override
	public Boolean getValue() {
		return getInputValue(0).equals(getInputValue(1));
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
		return false;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Boolean> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Boolean> operator) {
		return false;
	}

}
