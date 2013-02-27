package de.uniol.inf.is.odysseus.probabilistic.function;

import de.uniol.inf.is.odysseus.core.server.mep.IOperator;

public abstract class AbstractProbabilisticUnaryOperator<T> extends
		AbstractProbabilisticFunction<T> implements IOperator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5406076763880872083L;

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public boolean isUnary() {
		return true;
	}

	@Override
	public int getArity() {
		return 1;
	}
}
