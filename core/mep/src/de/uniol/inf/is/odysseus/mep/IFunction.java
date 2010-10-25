package de.uniol.inf.is.odysseus.mep;

public interface IFunction<T> extends IExpression<T> {
	public int getArity();

	public void setArguments(IExpression<?>... arguments);
	
	public void setArgument(int argumentPosition, IExpression<?> argument);

	public IExpression<?>[] getArguments();
	
	public IExpression<?> getArgument(int argumentPosition);
	
	public String getSymbol();

	public boolean isContextDependent();
}
