package de.uniol.inf.is.odysseus.interval.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.MinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.PlusOperator;
import de.uniol.inf.is.odysseus.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalMultiplicationOperator extends
		AbstractBinaryOperator<Interval> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7746637195241980728L;

	@Override
	public int getPrecedence() {
		return 5;
	}

	@Override
	public String getSymbol() {
		return "*";
	}

	@Override
	public Interval getValue() {
		Interval a = getInputValue(0);
		Interval b = getInputValue(1);
		return getValueInternal(a, b);
	}

	protected Interval getValueInternal(Interval a, Interval b) {
		final double inf = Math.min(
				Math.min(a.inf() * b.inf(), a.inf() * b.sup()),
				Math.min(a.sup() * b.inf(), a.sup() * b.sup()));
		final double sup = Math.max(
				Math.max(a.inf() * b.inf(), a.inf() * b.sup()),
				Math.max(a.sup() * b.inf(), a.sup() * b.sup()));
		return new Interval(inf, sup);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFIntervalDatatype.INTERVAL_DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
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
	public boolean isLeftDistributiveWith(IOperator<Interval> operator) {
		return operator.getClass() == IntervalPlusOperator.class
				|| operator.getClass() == IntervalMinusOperator.class
				|| operator.getClass() == PlusOperator.class
				|| operator.getClass() == MinusOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Interval> operator) {
		return operator.getClass() == IntervalPlusOperator.class
				|| operator.getClass() == IntervalMinusOperator.class
				|| operator.getClass() == PlusOperator.class
				|| operator.getClass() == MinusOperator.class;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFIntervalDatatype.INTERVAL_BYTE,
			SDFIntervalDatatype.INTERVAL_SHORT,
			SDFIntervalDatatype.INTERVAL_INTEGER,
			SDFIntervalDatatype.INTERVAL_FLOAT,
			SDFIntervalDatatype.INTERVAL_DOUBLE,
			SDFIntervalDatatype.INTERVAL_LONG };

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity() - 1) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		}
		return accTypes;
	}

}
