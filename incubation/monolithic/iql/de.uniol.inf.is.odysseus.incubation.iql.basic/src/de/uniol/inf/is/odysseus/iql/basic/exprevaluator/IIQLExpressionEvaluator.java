package de.uniol.inf.is.odysseus.iql.basic.exprevaluator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;

public interface IIQLExpressionEvaluator {
	TypeResult eval(IQLExpression expr);

	TypeResult eval(IQLExpression expr, JvmTypeReference exptectedType);
	
	TypeResult getThisType(EObject obj);
	
	TypeResult getSuperType(EObject obj);
	
	boolean isThis(EObject obj);
	
	boolean isSuper(EObject obj);



}
