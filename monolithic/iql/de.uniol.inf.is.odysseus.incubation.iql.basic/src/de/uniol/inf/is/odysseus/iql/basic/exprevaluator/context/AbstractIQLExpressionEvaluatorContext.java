package de.uniol.inf.is.odysseus.iql.basic.exprevaluator.context;

import org.eclipse.xtext.common.types.JvmTypeReference;

public abstract class AbstractIQLExpressionEvaluatorContext implements IIQLExpressionEvaluatorContext {

	private JvmTypeReference expectedTypeRef;
	
	
	@Override
	public JvmTypeReference getExpectedTypeRef() {
		return this.expectedTypeRef;
	}

	@Override
	public void setExpectedTypeRef(JvmTypeReference typeRef) {
		this.expectedTypeRef = typeRef;
	}


}
