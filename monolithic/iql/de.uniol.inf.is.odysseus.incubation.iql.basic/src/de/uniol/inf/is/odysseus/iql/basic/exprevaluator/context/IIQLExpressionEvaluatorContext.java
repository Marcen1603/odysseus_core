package de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context;

import org.eclipse.xtext.common.types.JvmTypeReference;

public interface IIQLExpressionEvaluatorContext {

	public IIQLExpressionEvaluatorContext cleanCopy();
	
	public JvmTypeReference getExpectedTypeRef();
	
	public void setExpectedTypeRef(JvmTypeReference typeRef);

}
