package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;

public interface IIQLExpressionParser {
	TypeResult getType(IQLExpression expr);

	TypeResult getType(IQLExpression expr, JvmTypeReference exptectedType);
}
