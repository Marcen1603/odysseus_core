package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * Combines two lists with the + operator. Appends the second list at the end of
 * the first one.
 * 
 * @author Tobias Brandt
 *
 */
public class CombineListsFunction extends AbstractBinaryOperator<List<?>> {

	private static final long serialVersionUID = -7979878191918739481L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.LIST }, { SDFDatatype.LIST } };

	public CombineListsFunction() {
		super("+", accTypes, SDFDatatype.LIST);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<?> getValue() {
		List newLeft = new ArrayList((List) getInputValue(0));
		List right = new ArrayList((List) getInputValue(1));
		newLeft.addAll(right);
		return newLeft;
	}

	@Override
	public IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<List<?>> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<List<?>> operator) {
		return false;
	}

	@Override
	public int getPrecedence() {
		return 6;
	}
}
