/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class StringDivisionOperator extends AbstractBinaryOperator<Integer> {

	private static final long serialVersionUID = -4338365198965283565L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.STRING } };

	public StringDivisionOperator() {
		super("/", accTypes, SDFDatatype.INTEGER);
	}

	@Override
	public int getPrecedence() {
		return 5;
	}

	@Override
	public Integer getValue() {
		String a = getInputValue(0);
		String b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		Pattern pattern = Pattern.compile(b, Pattern.LITERAL);
		Matcher matcher = pattern.matcher(a);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
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
	public boolean isLeftDistributiveWith(IOperator<Integer> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Integer> operator) {
		return operator.getClass() == StringPlusOperator.class
				|| operator.getClass() == StringMinusOperator.class;
	}

}
