package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context;

import org.eclipse.xtext.common.types.JvmTypeReference;

public interface IIQLExpressionParserContext {

	public IIQLExpressionParserContext cleanCopy();
	
	public JvmTypeReference getExpectedTypeRef();
	
	public void setExpectedTypeRef(JvmTypeReference typeRef);

}
