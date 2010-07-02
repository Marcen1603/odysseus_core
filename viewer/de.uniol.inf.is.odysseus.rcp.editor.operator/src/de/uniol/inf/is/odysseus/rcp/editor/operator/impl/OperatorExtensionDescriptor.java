package de.uniol.inf.is.odysseus.rcp.editor.operator.impl;

import de.uniol.inf.is.odysseus.rcp.editor.operator.ILogicalOperatorExtension;
import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;

public class OperatorExtensionDescriptor implements IOperatorExtensionDescriptor {

	private final String id;
	private String label;
	private String group;
	private ILogicalOperatorExtension extension;
	
	public OperatorExtensionDescriptor( String id ) {
		this.id = id;
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public ILogicalOperatorExtension getExtensionClass() {
		return extension;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setExtensionClass(ILogicalOperatorExtension extension) {
		this.extension = extension;
	}
}
