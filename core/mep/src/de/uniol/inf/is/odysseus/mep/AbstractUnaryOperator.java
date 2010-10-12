package de.uniol.inf.is.odysseus.mep;


public abstract class AbstractUnaryOperator<T> extends AbstractFunction<T> implements
		IOperator<T> {
	
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
