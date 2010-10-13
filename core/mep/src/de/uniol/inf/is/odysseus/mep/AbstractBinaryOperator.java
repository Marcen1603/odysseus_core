package de.uniol.inf.is.odysseus.mep;

public abstract class AbstractBinaryOperator<T> extends AbstractFunction<T>
		implements IBinaryOperator<T> {
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
}
