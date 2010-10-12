package de.uniol.inf.is.odysseus.mep;

public interface IExpressionVisitor {
	public Object visit(Variable variable, Object data);
	public Object visit(Constant constant, Object data);
	public Object visit(IFunction function, Object data);
}
