package de.uniol.inf.is.odysseus.rcp.editor.model;

import org.eclipse.gef.requests.CreationFactory;

import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;

public class OperatorFactory implements CreationFactory {

	private IOperatorExtensionDescriptor desc;
	
	public OperatorFactory(IOperatorExtensionDescriptor desc) {
		this.desc = desc;
	}
	
	@Override
	public Object getNewObject() {
		Operator op = new Operator(desc);
		return op;
	}

	@Override
	public Object getObjectType() {
		return Operator.class;
	}

	
}
