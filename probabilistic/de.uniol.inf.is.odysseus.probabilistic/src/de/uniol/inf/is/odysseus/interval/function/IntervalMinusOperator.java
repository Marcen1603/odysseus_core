package de.uniol.inf.is.odysseus.interval.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalMinusOperator extends AbstractBinaryOperator<Interval> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7665159518653289431L;

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public String getSymbol() {
		return "-";
	}

	@Override
	public Interval getValue() {
		Interval a = getInputValue(0);
		Interval b = getInputValue(1);
		return getValueInternal(a, b);
	}

	protected Interval getValueInternal(Interval a, Interval b) {
		return new Interval(a.inf() - b.sup(), a.sup() - b.inf());
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
		return false;
	}

	@Override
	public boolean isAssociative() {
		return false;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Interval> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Interval> operator) {
		return false;
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
