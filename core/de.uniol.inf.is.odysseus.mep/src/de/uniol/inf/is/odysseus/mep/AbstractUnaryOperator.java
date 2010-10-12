package de.uniol.inf.is.odysseus.mep;


public abstract class AbstractUnaryOperator extends AbstractFunction implements
		IOperator {
	
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
