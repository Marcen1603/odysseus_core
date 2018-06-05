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


	private static final long serialVersionUID = -6758609091849696249L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.STRING }, { SDFDatatype.STRING } };

	public StringPlusOperator() {
		super("+",accTypes, SDFDatatype.STRING);
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
		StringBuilder sb = new StringBuilder();
		sb.append(a);
		sb.append(b);
		return sb.toString();
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
