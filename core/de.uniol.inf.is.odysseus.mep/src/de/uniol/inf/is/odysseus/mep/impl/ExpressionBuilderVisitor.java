package de.uniol.inf.is.odysseus.mep.impl;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.mep.Constant;
import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.Variable;

public class ExpressionBuilderVisitor implements MEPImplVisitor {

	private Map<String, Variable> symbolTable = new HashMap<String, Variable>();

	@Override
	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTFunction node, Object data) {
		String symbol = node.getSymbol();
		if (!MEP.containsFunction(symbol)) {
			throw new IllegalArgumentException("no such function: " + symbol);
		}
		IFunction function = MEP.getFunction(symbol);

		int arity = node.jjtGetNumChildren();
		IExpression[] expressions = new IExpression[arity];
		for (int i = 0; i < arity; ++i) {
			expressions[i] = (IExpression) node.jjtGetChild(i).jjtAccept(this,
					data);
		}
		function.setArguments(expressions);

		return function;
	}

	@Override
	public Object visit(ASTConstant node, Object data) {
		return new Constant(node.getValue());
	}

	@Override
	public Object visit(ASTVariable node, Object data) {
		String identifier = node.getIdentifier();
		if (symbolTable.containsKey(identifier)) {
			return symbolTable.get(identifier);
		} else {
			Variable variable = new Variable(identifier);
			symbolTable.put(identifier, variable);
			return variable;
		}
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		this.symbolTable.clear();
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

}
