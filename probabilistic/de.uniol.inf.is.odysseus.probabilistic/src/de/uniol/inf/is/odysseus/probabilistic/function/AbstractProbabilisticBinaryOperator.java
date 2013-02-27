package de.uniol.inf.is.odysseus.probabilistic.function;

import de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public abstract class AbstractProbabilisticBinaryOperator<T> extends
		AbstractProbabilisticFunction<T> implements IBinaryOperator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2604513567977149416L;

	@Override
	final public int getArity() {
		return 2;
	}

	@Override
	final public boolean isBinary() {
		return true;
	}

	@Override
	final public boolean isUnary() {
		return false;
	}

	@Override
	public boolean isDistributiveWith(IOperator<T> operator) {
		return isLeftDistributiveWith(operator)
				&& isRightDistributiveWith(operator);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("" + getArgument(0));
		buffer.append(" ").append(getSymbol()).append(" " + getArgument(1));
		return buffer.toString();
	}

}
