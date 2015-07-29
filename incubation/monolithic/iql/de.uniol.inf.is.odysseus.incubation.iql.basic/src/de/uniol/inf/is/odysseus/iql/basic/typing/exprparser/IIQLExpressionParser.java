package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;

public interface IIQLExpressionParser {
	TypeResult getType(IQLExpression expr);

	TypeResult getType(IQLExpression expr, JvmTypeReference exptectedType);
	
	TypeResult getThisType(EObject obj);
	
	TypeResult getSuperType(EObject obj);
	
	boolean isThis(EObject obj);
	
	boolean isSuper(EObject obj);


}
