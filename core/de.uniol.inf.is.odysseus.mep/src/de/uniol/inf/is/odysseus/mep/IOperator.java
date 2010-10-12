package de.uniol.inf.is.odysseus.mep;

public interface IOperator extends IExpression {

	public enum ASSOCIATIVITY {
		LEFT_TO_RIGHT,
		RIGHT_TO_LEFT
	}
	
	public int getPrecedence();
	
	public String getSymbol();
	
	public boolean isUnary();
	
	public boolean isBinary();
}
