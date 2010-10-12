package de.uniol.inf.is.odysseus.mep;

public interface IFunction extends IExpression {
	public int getArity();
	public void setArguments(IExpression... arguments);
	public IExpression[] getArguments();
	public String getSymbol();
}
