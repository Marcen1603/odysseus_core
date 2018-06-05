package de.uniol.inf.is.odysseus.iql.basic.exprevaluator;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;

public interface IIQLExpressionEvaluator {
	TypeResult eval(IQLExpression expr);

	TypeResult eval(IQLExpression expr, JvmTypeReference exptectedType);
	
}
