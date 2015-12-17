package de.uniol.inf.is.odysseus.mep.functions.dstring;

import de.uniol.inf.is.odysseus.core.datatype.DString;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class StringPlusOperator extends AbstractBinaryOperator<DString> {


	private static final long serialVersionUID = -6758609091849696249L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.DSTRING }, { SDFDatatype.STRING, SDFDatatype.DSTRING } };

	public StringPlusOperator() {
		super("+",accTypes, SDFDatatype.DSTRING);
	}

	@Override
	public int getPrecedence() {
		return 6;
	}


	@Override
	public DString getValue() {
		StringBuilder sb = new StringBuilder();
		// We know, this must be a string!
		sb.append((DString.valueOf(getInputValue(0))));
		sb.append((DString.valueOf(getInputValue(1))));
		return new DString(sb.toString());
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
	public boolean isLeftDistributiveWith(IOperator<DString> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<DString> operator) {
		return false;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

}
