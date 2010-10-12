package de.uniol.inf.is.odysseus.mep;

public class ExpressionOptimizer implements IExpressionVisitor {

	public static IExpression<?> simplifyExpression(IExpression<?> expression) {
		ExpressionOptimizer simplificator = new ExpressionOptimizer();
		if ((Boolean) expression.acceptVisitor(simplificator, null)) {
			return new Constant<Object>(expression.getValue());
		} else {
			return expression;
		}
	}

	@Override
	public Boolean visit(Variable variable, Object data) {
		return false;
	}

	@Override
	public Boolean visit(Constant<?> constant, Object data) {
		return true;
	}

	@Override
	public Boolean visit(IFunction<?> function, Object data) {
		if (function.getArity() == 0) {
			return false;
		}
		
		boolean isConstant = true;
		for (int i = 0; i < function.getArity(); ++i) {
			if ((Boolean) function.getArguments()[i].acceptVisitor(this, data)) {
				function.getArguments()[i] = new Constant<Object>(function
						.getArguments()[i].getValue());
			} else {
				isConstant = false;
			}
		}
		return isConstant;
	}

}
