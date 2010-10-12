package de.uniol.inf.is.odysseus.mep;



public abstract class AbstractBinaryOperator<T> extends AbstractFunction<T> implements IOperator<T>{
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
}
