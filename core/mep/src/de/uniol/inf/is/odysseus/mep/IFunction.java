package de.uniol.inf.is.odysseus.mep;

public interface IFunction<T> extends IExpression<T> {
	public int getArity();

	public void setArguments(IExpression<?>... arguments);

	public IExpression<?>[] getArguments();

	public String getSymbol();

	public boolean isContextDependent();
}
